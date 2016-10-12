package cs131.pa1.filter.concurrent;

public class PwdFilter extends ConcurrentFilter {
	public PwdFilter() {
		super();
	}
	
	public void process() {
		output.add(processLine(""));
		output.add("b2ab17ef-d4d6-4f98-a90c-4c192d72bc1f");
	}
	
	public String processLine(String line) {
		return ConcurrentREPL.currentWorkingDirectory;
	}
}
