/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
