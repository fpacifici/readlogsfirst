/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.logreader.logsplitter;

/**
 * Strategy pattern that provide the parsing facility for log file lines.
 * It is provided a set of lines coming from the log file and once the item
 * is completed it composes the LogLine object and returns it
 * 
 * @author fpacifici
 */
public interface LineSplittingStrategy {
 
    /**
     * Defines the result of a line consumption.
     * CONSUMING means that the log item is not complete (for multiline items)
     * CONSUMED means that log item is complete and nothing is buffered
     * BUFFERED means that the log item is recognized as complete since the beginning
     * of the following one has been found. The first line of the next item 
     * is kept in the buffer.
     */
    enum ConsumptionStatus {EMPTY,CONSUMING,CONSUMED,BUFFERED};
    
    /**
     * Consumes a line from the file storing in LineSplitting buffer
     * @param line
     * @return The Consumption Status
     */
    public ConsumptionStatus consumeLine(String line);
    
    /**
     * Consumes the line that is buffered as part of the new line.
     * @return 
     */
    public ConsumptionStatus consumeBuffer();
    
    /**
     * @return the content of the consumed and recognized line.
     */
    public String getConsumed();
    
    /**
     * 
     * @return is a buffered line exists. 
     */
    public ConsumptionStatus getCurrentStatus();
    
    /**
     * Resets current buffer.
     */
    public void reset();
    
}