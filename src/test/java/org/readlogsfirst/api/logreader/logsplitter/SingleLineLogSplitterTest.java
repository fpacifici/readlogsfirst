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
