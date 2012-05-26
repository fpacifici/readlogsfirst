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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;
import org.junit.Test;
import org.readlogsfirst.api.logreader.LogLine;
import static org.junit.Assert.*;

import org.readlogsfirst.api.logreader.loglines.UnstructuredLogLine;

/**
 * Perfors some parsing tests for timestamp parser
 * @author pyppo
 */
public class TimestampParserTest {
    
    /**
     * Tests parsing with a date provided as regular expression
     * @throws Exception 
     */
    @Test
    public void testRegExpDate() throws Exception {
        String logline = "Mar 30 00:27:06 OddId newsyslog[17363]: logfile turned over";
        TimestampIndexParse parser = new TimestampIndexParse(
                Pattern.compile("\\w\\w\\w\\s\\d\\d\\s\\d\\d.\\d\\d.\\d\\d")
                ,"MMM dd HH:mm:ss");
        
        UnstructuredLogLine<Date> s = (UnstructuredLogLine<Date>) parser.parseLine(logline);
        assertNotNull(s);
        assertEquals(logline, s.getLine());
        Date d = s.getIndex();
        SimpleDateFormat format = new SimpleDateFormat("MMM dd HH:mm:ss");
        
        String ret = format.format(d);
        assertEquals(ret, "Mar 30 00:27:06");
    }
    
    /**
     * Tests the parser with date defined through limits
     * @throws Exception 
     */
    @Test
    public void testDateLimit() throws Exception {
        String logline = "Mar 30 00:27:06 OddId newsyslog[17363]: logfile turned over";
        TimestampIndexParse parser = new TimestampIndexParse(0,15
                ,"MMM dd HH:mm:ss");
        
        UnstructuredLogLine<Date> s = (UnstructuredLogLine<Date>) parser.parseLine(logline);
        assertNotNull(s);
        assertEquals(logline, s.getLine());
        Date d = s.getIndex();
        SimpleDateFormat format = new SimpleDateFormat("MMM dd HH:mm:ss");
        
        String ret = format.format(d);
        assertEquals(ret, "Mar 30 00:27:06");
    }
    
    /**
     * Tries to parse something which is not valid.
     * @throws Exception 
     */
    @Test(expected=LogItemParsingException.class)
    public void testParsingError() throws Exception {
        String logline = "Mar30002706 OddId newsyslog[17363]: logfile turned over";
        TimestampIndexParse parser = new TimestampIndexParse(0,15
                ,"MMM dd HH:mm:ss");
        
        UnstructuredLogLine<Date> s = (UnstructuredLogLine<Date>) parser.parseLine(logline);
        
    }
}
