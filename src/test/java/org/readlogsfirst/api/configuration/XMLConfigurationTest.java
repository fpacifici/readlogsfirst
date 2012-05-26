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
package org.readlogsfirst.api.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.junit.Assert.*;
import org.junit.Test;
import org.readlogsfirst.api.descriptors.LogDescriptor;
import org.readlogsfirst.api.logreader.logitemparser.LogItemParser;
import org.readlogsfirst.api.logreader.logitemparser.LogParserContext;
import org.readlogsfirst.api.logreader.loglines.MapLogLine;
import org.readlogsfirst.api.logreader.loglines.UnstructuredLogLine;
import org.readlogsfirst.api.logreader.logsplitter.MultiLineLimitsSplitter;
import org.readlogsfirst.api.logreader.logsplitter.SingleLineSplittingStrategy;

/**
 * Tests the load of the XML configuration
 * 
 * @author fpacifici
 */

public class XMLConfigurationTest {
    
    ConfigurationParser parser;
    
    /**
     * Parses the first line.
     * @throws Exception 
     */
    @Test
    public void testParse() throws Exception {
        URL url = this.getClass().getResource("/exampleConfig.xml");
	File base = new File(url.getFile());
        FileInputStream stream = new FileInputStream(base);
        parser = new ConfigurationParser(stream);
        
        assertTrue(parser.hasNext());
        ConfigurationEntry desc1 = parser.getNextEntry();
        assertEquals(UnstructuredLogLine.class, desc1.getExpectedLineType());
        LogDescriptor d1 = desc1.getDescriptor();
        assertTrue(d1.getSplittingStrategy() instanceof SingleLineSplittingStrategy);
        String logline = "Mar 30 00:27:06 OddId newsyslog[17363]: logfile turned over";
        LogItemParser lparser = d1.getParser();
        
        UnstructuredLogLine<Date> s = (UnstructuredLogLine<Date>) lparser.parseLine(logline);
        assertNotNull(s);
        assertEquals(logline, s.getLine());
        Date d = s.getIndex();
        SimpleDateFormat format = new SimpleDateFormat("MMM dd HH:mm:ss");
        
        String ret = format.format(d);
        assertEquals(ret, "Mar 30 00:27:06");
    }
    
    /**
     * Parses both the lines
     * @throws Exception 
     */
    @Test
    public void testComplex() throws Exception {
        URL url = this.getClass().getResource("/exampleConfig.xml");
	File base = new File(url.getFile());
        FileInputStream stream = new FileInputStream(base);
        parser = new ConfigurationParser(stream);
        
        assertTrue(parser.hasNext());
        assertTrue(parser.hasNext());
        parser.getNextEntry();
        assertTrue(parser.hasNext());
        
        ConfigurationEntry desc1 = parser.getNextEntry();
        assertFalse(parser.hasNext());
        
        assertEquals(MapLogLine.class, desc1.getExpectedLineType());
        assertEquals("weblogicLog", desc1.getName());
        LogDescriptor descriptor = desc1.getDescriptor();
        assertTrue(descriptor.getSplittingStrategy() instanceof MultiLineLimitsSplitter);
        assertTrue(descriptor.getParser() instanceof LogParserContext);
    }
}
