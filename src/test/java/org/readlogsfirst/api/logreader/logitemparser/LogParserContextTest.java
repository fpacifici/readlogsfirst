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
package org.readlogsfirst.api.logreader.logitemparser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.readlogsfirst.api.logreader.LogLine;
import org.readlogsfirst.api.logreader.logitemparser.builders.MapLineBuilder;
import org.readlogsfirst.api.logreader.logitemparser.indexFinders.DateIndexFinder;
import org.readlogsfirst.api.logreader.logitemparser.tokenizers.SimpleSplitTokenizer;
import org.readlogsfirst.api.logreader.loglines.MapLogLine;

import static org.junit.Assert.*;

/**
 * Tests a strategy based log parser
 * 
 * @author fpacifici
 *
 */
public class LogParserContextTest {
	private String logString = "####<Apr 11, 2012 12:06:59 PM CEST> <Info> <MessagingBridge> <MacBook-Pro-di-Filippo.local> " +
			"<AdminServer> <Thread-2> <<WLS Kernel>> <> <> <1334138819676> <BEA-200001> " +
			"<The messaging bridge service has successfully shut down.> ";

	/**
	 * Instantiate the parser and provides the line for parsing
	 * @throws Exception
	 */
	@Test
	public void strategyParserTest() throws  Exception{
		SimpleSplitTokenizer tokenizer = new SimpleSplitTokenizer("<",">");
		DateIndexFinder finder = new DateIndexFinder("MMM dd, yyyy hh:mm:ss a z", 0);
		List<String> keys = new ArrayList<String>();
		keys.add("timestamp");
		keys.add("level");
		keys.add("origin");
		keys.add("host");
		keys.add("servername");
		keys.add("thread");
		keys.add("component");
		keys.add("empty1");
		keys.add("empty2");
		keys.add("code");
		keys.add("messagecode");
		keys.add("message");
		MapLineBuilder builder = new MapLineBuilder(keys);
		
		LogParserContext<Date> parser = new LogParserContext<Date>(tokenizer,finder,null,builder);
	
		LogLine<Date> line = parser.parseLine(logString);
		MapLogLine<Date> mline = (MapLogLine<Date>)line;
		
		assertEquals(12, mline.size());
		
		assertEquals("Info", mline.get("level"));
		assertEquals("AdminServer", mline.get("servername"));
		assertEquals("<WLS Kernel", mline.get("component"));		
	}
	
}
