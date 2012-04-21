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
