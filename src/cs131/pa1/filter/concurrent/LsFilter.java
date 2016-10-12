package cs131.pa1.filter.concurrent;
import java.io.File;

public class LsFilter extends ConcurrentFilter{
	int counter;
	File folder;
	File[] flist;
	
	public LsFilter() {
		super();
		counter = 0;
		folder = new File(ConcurrentREPL.currentWorkingDirectory);
		flist = folder.listFiles();
	}
	
	@Override
	public void process() {
		while(counter < flist.length) {
			output.add(processLine(""));
		}
		output.add("b2ab17ef-d4d6-4f98-a90c-4c192d72bc1f");
	}
	
	@Override
	public String processLine(String line) {
		return flist[counter++].getName();
	}
}
