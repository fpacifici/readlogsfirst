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
package org.readlogsfirst.api.logreader.basereader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.readlogsfirst.api.descriptors.BaseLogDescriptor;
import org.readlogsfirst.api.descriptors.LogFileGroup;
import org.readlogsfirst.api.descriptors.RegExpFileFilter;
import org.readlogsfirst.api.logreader.LogLine;
import org.readlogsfirst.api.logreader.logitemparser.LogItemParser;
import org.readlogsfirst.api.logreader.logitemparser.LogItemParsingException;
import org.readlogsfirst.api.logreader.logitemparser.LogParserContext;
import org.readlogsfirst.api.logreader.logitemparser.TimestampIndexParse;
import org.readlogsfirst.api.logreader.logitemparser.builders.MapLineBuilder;
import org.readlogsfirst.api.logreader.logitemparser.indexFinders.DateIndexFinder;
import org.readlogsfirst.api.logreader.logitemparser.tokenizers.SimpleSplitTokenizer;
import org.readlogsfirst.api.logreader.loglines.MapLogLine;
import org.readlogsfirst.api.logreader.logsplitter.LineSplittingStrategy;
import org.readlogsfirst.api.logreader.logsplitter.MultiLineLimitsSplitter;
import org.readlogsfirst.api.logreader.logsplitter.SingleLineSplittingStrategy;
import org.readlogsfirst.api.util.IssueLogger;
import org.readlogsfirst.api.util.SystemOutLogger;

/**
 * Tests the parsing of a weblogic log file.
 * @author pyppo
 *
 */
public class WeblogicLoggerTest {

	 private LineSplittingStrategy splitter;
	    private LogItemParser parser;
	    private LogFileGroup logGroup;
	    private BaseLogDescriptor descriptor; 
	    private IssueLogger logger;
	    
	    /**
	     * Set up the splitter and the aprser for test file
	     * @throws Exception 
	     */
	    @Before
	    public void setUp() throws Exception {
	        splitter = new MultiLineLimitsSplitter("####", true);
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
			
			parser = new LogParserContext<Date>(tokenizer,finder,null,builder);
	        
	        URL url = this.getClass().getResource("/");
	        File base = new File(url.getFile());
	        logGroup = new RegExpFileFilter(base,false,Pattern.compile("AdminServer.log"));
	        descriptor = new BaseLogDescriptor();
	        descriptor.setParser(parser);
	        descriptor.setSplitter(splitter);
	    }
	    
	/**
	 * Reads a whole weblogic server log line.
	 * @throws Exception
	 */
	@Test
	public void readLogFile() throws Exception {
		IssueLogger il = new SystemOutLogger();
        logger = PowerMockito.spy(il);
        
        BaseLogReader reader = new BaseLogReader(logger);
        reader.configure(descriptor, logGroup);
        
        int cont = 0;
        MapLogLine<Date> m = null;
        for (LogLine l : reader.getLogContent()){
            cont ++;
            m = (MapLogLine<Date>)l;
        }
        
        Mockito.verify(logger,times(0)).parsingException(
                (LogItemParsingException)Mockito.anyObject(),Mockito.anyString());
        assertEquals(146, cont);
        assertEquals("BEA-000236", m.get("messagecode"));
	}
}
