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

import org.junit.Test;
import static org.junit.Assert.*;
import org.readlogsfirst.api.logreader.logsplitter.LineSplittingStrategy.ConsumptionStatus;

/**
 * Tests the splitting of multilines.
 * @author pyppo
 *
 */
public class MultiLineSplitterTest {

	/**
	 * Tests the load of a singele line
	 * Then it tries to add a second one.
	 * At the end it resets everything.
	 * @throws Exception
	 */
	@Test
	public void testSingleLinesWithStart() throws Exception {
		String logString = "####<Apr 11, 2012 12:06:59 PM CEST> <Info> <MessagingBridge> <MacBook-Pro-di-Filippo.local> " +
				"<AdminServer> <Thread-2> <<WLS Kernel>> <> <> <1334138819676> <BEA-200001> " +
				"<The messaging bridge service has successfully shut down.> ";
		String nextLine = "####<Apr 11, 2012 12:06:59 PM CEST> <Info> <JMS> <MacBook-Pro-di-Filippo.local> <AdminServer> " +
				"<Thread-2> <<WLS Kernel>> <> <> <1334138819710> <BEA-040308> <JMS service is suspending.> ";
		
		MultiLineLimitsSplitter splitter = new MultiLineLimitsSplitter("####", true);
		assertEquals(ConsumptionStatus.EMPTY, splitter.getCurrentStatus());
		ConsumptionStatus st = splitter.consumeLine(logString);
		
		assertEquals(ConsumptionStatus.CONSUMING, splitter.getCurrentStatus());
		
		splitter.consumeLine(nextLine);
		assertEquals(ConsumptionStatus.BUFFERED, splitter.getCurrentStatus());
		String consumed = splitter.getConsumed();
		assertEquals(logString, consumed);
		assertEquals(ConsumptionStatus.CONSUMING, splitter.getCurrentStatus());
		
		assertEquals(ConsumptionStatus.CONSUMING, splitter.getCurrentStatus());
		String end = splitter.getConsumed();
		assertEquals(nextLine, end);
		assertEquals(ConsumptionStatus.EMPTY, splitter.getCurrentStatus());
	}
	
	/**
	 * Tests the load a line splitted by \n
	 * @throws Exception
	 */
	@Test
	public void testMultiLinesWithStart() throws Exception {
		String logString = "####<Apr 11, 2012 12:06:59 PM CEST> <Info> <MessagingBridge> <MacBook-Pro-di-Filippo.local> ";
		String logString2 =	"<AdminServer> <Thread-2> <<WLS Kernel>> <> <> <1334138819676> <BEA-200001> ";
		String logString3 =	"<The messaging bridge service has successfully shut down.> ";
		String nextLine = "####<Apr 11, 2012 12:06:59 PM CEST> <Info> <JMS> <MacBook-Pro-di-Filippo.local> <AdminServer> ";
		String nextLine2 = "<Thread-2> <<WLS Kernel>> <> <> <1334138819710> <BEA-040308> <JMS service is suspending.> ";
		
		MultiLineLimitsSplitter splitter = new MultiLineLimitsSplitter("####", true);
		ConsumptionStatus st = splitter.consumeLine(logString);
		assertEquals(ConsumptionStatus.CONSUMING, st);
		assertEquals(st, splitter.getCurrentStatus());
		
		st = splitter.consumeLine(logString2);
		assertEquals(ConsumptionStatus.CONSUMING, st);
		assertEquals(st, splitter.getCurrentStatus());
		
		st = splitter.consumeLine(logString3);
		assertEquals(ConsumptionStatus.CONSUMING, st);
		assertEquals(st, splitter.getCurrentStatus());
		
		st = splitter.consumeLine(nextLine);
		assertEquals(ConsumptionStatus.BUFFERED, st);
		assertEquals(st, splitter.getCurrentStatus());
		
		String recovered = splitter.getConsumed();
		assertEquals(logString+ logString2 + logString3, recovered);
		assertEquals(ConsumptionStatus.CONSUMING, splitter.getCurrentStatus());
		
		st = splitter.consumeLine(nextLine2);
		assertEquals(ConsumptionStatus.CONSUMING, st);
		assertEquals(st, splitter.getCurrentStatus());
	
		String end = splitter.getConsumed();
		assertEquals(nextLine + nextLine2, end);
	}
}
