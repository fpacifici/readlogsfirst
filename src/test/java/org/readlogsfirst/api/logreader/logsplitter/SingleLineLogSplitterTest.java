/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.logreader.logsplitter;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests a simple log splitter
 * 
 * @author fpacifici
 */
public class SingleLineLogSplitterTest {
    
    @Test
    public void testSingleLineSplitter() throws Exception {
        LineSplittingStrategy strategy = new SingleLineSplittingStrategy();
        LineSplittingStrategy.ConsumptionStatus status = strategy.consumeLine("AAABBB");
        assertEquals(LineSplittingStrategy.ConsumptionStatus.CONSUMED, status);
        String consumed = strategy.getConsumed();
        assertEquals("AAABBB", consumed);
        
        try{
            strategy.getConsumed();
            fail("must throw illegal state");
        }catch(IllegalStateException e){
            
        }
    }
}
