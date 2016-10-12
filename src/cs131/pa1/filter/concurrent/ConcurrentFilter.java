package cs131.pa1.filter.concurrent;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import cs131.pa1.filter.Filter;


public abstract class ConcurrentFilter extends Filter implements Runnable {
	
	protected LinkedBlockingDeque<String> input;
	protected LinkedBlockingDeque<String> output;
	
	@Override
	public void setPrevFilter(Filter prevFilter) {
		prevFilter.setNextFilter(this);
	}
	
	@Override
	public void setNextFilter(Filter nextFilter) {
		if (nextFilter instanceof ConcurrentFilter){
			ConcurrentFilter sequentialNext = (ConcurrentFilter) nextFilter;
			this.next = sequentialNext;
			sequentialNext.prev = this;
			if (this.output == null){
				//to support concurrent operations
				//if a thread tries to read from this while empty...
				//the thread will wait until it is no longer empty
				this.output = new LinkedBlockingDeque<String>();
			}
			sequentialNext.input = this.output;
		} else {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
	}
	
	public void process(){
		//will wait for input until finds "stop" signal
		while (!isDone()){
			String line = input.poll();
			String processedLine = processLine(line);
			if (processedLine != null){
				output.add(processedLine);
			}
		}
		//random UUID crypto-graphically generated; will never occur again
		//acts as signal that process is done
		//is added at end of output queue of first filter
		output.add("b2ab17ef-d4d6-4f98-a90c-4c192d72bc1f");
	}
	
	@Override
	public boolean isDone() {
		//finished when "stop" signal is found to be next in input queue
		//(this queue is done, as well as all previous queues)
		String check=null;
		try {
			check = input.take();
		} catch (InterruptedException e) {
			
		}
		if (check.equals("b2ab17ef-d4d6-4f98-a90c-4c192d72bc1f")){
			return true;
		}else{
			//input taken from "take" method, needs to be put back at start of queue
			input.addFirst(check);
		}
		return false;
	}
	
	//makes the thread eligible to run - thread is terminated upon exit
	//calls process() to execute functionality of thread
	public void run(){
		process();
	}
	
	protected abstract String processLine(String line);
	
}
