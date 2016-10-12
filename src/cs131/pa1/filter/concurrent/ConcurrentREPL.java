package cs131.pa1.filter.concurrent;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cs131.pa1.filter.Message;

public class ConcurrentREPL {

	static String currentWorkingDirectory;
	static Map <Thread, String> background;
	private static ExecutorService pool;
	
	public static void main(String[] args){
		currentWorkingDirectory = System.getProperty("user.dir");
		//will not throw concurrentModificationException - stores alive threads
		background = new ConcurrentHashMap ();
		//executor so background threads will run sequentially
		pool = Executors.newSingleThreadExecutor();
		Scanner s = new Scanner(System.in);
		System.out.print(Message.WELCOME);
		String command;
		while(true) {
			//obtaining the command from the user
			System.out.print(Message.NEWCOMMAND);
			command = s.nextLine();
			if(command.equals("exit")) {
				//stops still executing background processes
				pool.shutdown();
				break;
			}else if(command.equals("repl_jobs")){
				int i=1;
				//iterates over map of background processes
				for (Map.Entry pair:background.entrySet()){
					//prints out alive threads and removes dead threads from map
					if ((((Thread) pair.getKey()).getState()!=Thread.State.TERMINATED)){
						System.out.println("\t"+i+". "+pair.getValue());
						i++;
					}else{
						background.remove((Thread)pair.getKey());
					}
				}
			} else if(!command.trim().equals("")) {
				//building the filters list from the command
				List<ConcurrentFilter> filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command);
				//checking to see if construction was successful. If not, prompt user for another command
				if(filterlist != null) {
					//iterates over each filter
					Iterator<ConcurrentFilter> iter = filterlist.iterator();
					while(iter.hasNext()){
						//makes thread for each filter
						Thread thread = new Thread(iter.next());
						//calls run on each filter, which calls process
						thread.start();
						//if not a background process
						if (command.charAt(command.length()-1)!='&'){
							//runs until thread.join is finished (entire command is processed)
							while (true) {
								try {
									thread.join();
									break;
								} catch (InterruptedException e) {
									
								}
							}
						//if is a background process
						}else{
							//add background thread to executor
							pool.submit(thread);
							if (!iter.hasNext()){
								background.put(thread, command);
							}
						}
					}
				}
			}
		}
		s.close();
		System.out.print(Message.GOODBYE);
	}

}
