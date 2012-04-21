package org.readlogsfirst.api.logreader.logsplitter;

/**
 * To be used to build a splitter
 * @author pyppo
 *
 */
public class SplitterBuilder {
	private LineSplittingStrategy splitter;
	
	public SplitterBuilder withSingleLineSplitter(){
		splitter = new SingleLineSplittingStrategy();
		return this;
	}
	
	public SplitterBuilder multiLineWithPrefix(String start, boolean notab){
		splitter = new MultiLineLimitsSplitter(start, notab);
		return this;
	}
	
	public SplitterBuilder multiLineWithPrefix(String start, String end, boolean notab, boolean nothingAfter){
		splitter = new MultiLineLimitsSplitter(start, end, notab, nothingAfter);
		return this;
	}
	
	public LineSplittingStrategy build() {
		return splitter;
	}
}
