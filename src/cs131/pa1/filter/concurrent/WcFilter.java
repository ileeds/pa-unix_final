package cs131.pa1.filter.concurrent;

public class WcFilter extends ConcurrentFilter {
	private int linecount;
	private int wordcount;
	private int charcount;
	
	public WcFilter() {
		super();
	}
	
	public void process() {
		if(isDone()) {
			output.add(processLine("b2ab17ef-d4d6-4f98-a90c-4c192d72bc1f"));
			//pass on "stop" signal to next filter
			output.add("b2ab17ef-d4d6-4f98-a90c-4c192d72bc1f");
		} else {
			super.process();
		}
	}
	
	public String processLine(String line) {
		//prints current result if sees "stop signal"
		if(line.equals("b2ab17ef-d4d6-4f98-a90c-4c192d72bc1f")) {
			return linecount + " " + wordcount + " " + charcount;
		}
		//originally called isDone here, but that removed from input queue
		//change to check if input queue empty
		if(this.input.size()==1) {
			String[] wct = line.split(" ");
			wordcount += wct.length;
			String[] cct = line.split("|");
			charcount += cct.length;
			return ++linecount + " " + wordcount + " " + charcount;
		} else {
			linecount++;
			String[] wct = line.split(" ");
			wordcount += wct.length;
			String[] cct = line.split("|");
			charcount += cct.length;
			return null;
		}
	}
}
