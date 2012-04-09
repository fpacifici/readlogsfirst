/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.logreader.logsplitter;

/**
 * Splitting strategy for a single line log.
 * Each line from the file is by default considered a log item.
 * @author fpacifici
 */
public class SingleLineSplittingStrategy implements LineSplittingStrategy{

    private String buffer;
    
    private boolean bufferReady = false;
    
    /**
     * 
     * @return 
     */
    public ConsumptionStatus consumeBuffer() {
       throw new IllegalStateException("SingleLineSplittingStrategy cannot store a buffer to consume");     
    }

    /**
     * Stores the line in the buffer and returns COMPLETED.
     * @param line
     * @return 
     */
    public ConsumptionStatus consumeLine(String line) {
        this.buffer = line;
        bufferReady = true;
        return ConsumptionStatus.CONSUMED;
    }

    public String getConsumed() {
        if (bufferReady){
            bufferReady = false;
            return buffer;
        }else {
            throw new IllegalStateException("Nothing is buffered. Cannot provide anything");
        }
    }

    public ConsumptionStatus getCurrentStatus() {
        if (bufferReady){
            return ConsumptionStatus.CONSUMED;
        }else {
            return ConsumptionStatus.EMPTY;
        }
    }

    public void reset() {
        buffer = null;
        bufferReady = false;
    }
    
    
    
}
