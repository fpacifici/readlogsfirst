/*  
 * Read the logs first logminer
 * Copyright 2012, Filippo Pacifici
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
