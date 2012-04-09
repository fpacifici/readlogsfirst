/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.logreader.basereader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import org.readlogsfirst.api.logreader.basereader.MultiFileIterator;
import java.util.TreeSet;
import java.util.regex.Pattern;
import org.junit.Test;
import org.readlogsfirst.api.logreader.LogLine;
import org.readlogsfirst.api.logreader.logitemparser.TimestampIndexParse;
import org.readlogsfirst.api.logreader.logsplitter.SingleLineSplittingStrategy;
import org.readlogsfirst.api.util.SystemOutLogger;

import static org.junit.Assert.*;
import org.readlogsfirst.api.logreader.UnstructuredLogLine;

/**
 * Tests the multifile base iterator
 * @author pyppo
 */
public class MultiFileIteratorTest {
    
    /**
     * Loads one single file.
     * @throws Exception 
     */
    @Test
    public void testSingleFileIterator() throws Exception {
        
        LinkedList<BufferedReader> files = new LinkedList<BufferedReader>();
        URL fUrl = this.getClass().getResource("/system.log.0");
        
        BufferedReader reader = new BufferedReader(
                new FileReader(fUrl.getFile()));
        
        Date d = new Date(System.currentTimeMillis());
        files.add(reader);
        
        MultiFileIterator iterator = new MultiFileIterator(files,
                new SingleLineSplittingStrategy(), 
                new TimestampIndexParse(
                Pattern.compile("\\w\\w\\w\\s[\\d\\s]\\d\\s\\d\\d.\\d\\d.\\d\\d")
                ,"MMM dd HH:mm:ss"), 
                new SystemOutLogger());
        
        //reads first line
        assertTrue(iterator.hasNext());
        UnstructuredLogLine<Date> first = (UnstructuredLogLine<Date>)iterator.next();
        Date dindex = first.getIndex();
        assertNotNull(dindex);
        
        //check date
        SimpleDateFormat format = new SimpleDateFormat("MMM dd HH:mm:ss");
        Date comparison = format.parse("Mar 30 00:27:06");
        assertEquals(comparison, dindex);
        
        //check iteation to the end
        int cont = 0;
        UnstructuredLogLine<Date> l = null;
        while(iterator.hasNext()){
            l = (UnstructuredLogLine<Date>)iterator.next();
            cont++;
        }
        assertEquals(6020, cont);
        assertEquals("Apr  3 00:01:00 OddId newsyslog[18602]: logfile turned over", l.getLine());
    }
    
    /**
     * Tests multifile
     * @throws Exception 
     */
    @Test
    public void testMultiFile() throws Exception {
        LinkedList<BufferedReader> files = new LinkedList<BufferedReader>();
        
        URL fUrl = this.getClass().getResource("/system.log.0");
        URL fUrl2 = this.getClass().getResource("/system.log.1");
        
        
        BufferedReader reader = new BufferedReader(
                new FileReader(fUrl.getFile()));
        BufferedReader reader2 = new BufferedReader(
                new FileReader(fUrl2.getFile()));
        
        
        Date d = new Date(System.currentTimeMillis());
        Date d2 = new Date(System.currentTimeMillis() + 1);
        
        files.add(reader);
        files.add(reader2);
        
        MultiFileIterator iterator = new MultiFileIterator(files,
                new SingleLineSplittingStrategy(), 
                new TimestampIndexParse(
                Pattern.compile("\\w\\w\\w\\s[\\d\\s]\\d\\s\\d\\d.\\d\\d.\\d\\d")
                ,"MMM dd HH:mm:ss"), 
                new SystemOutLogger());
        
        int cont = 0;
        UnstructuredLogLine<Date> l = null;
        while(iterator.hasNext()){
            l = (UnstructuredLogLine<Date>)iterator.next();
            cont++;
        }
        
        assertEquals(6151, cont);
        assertEquals("Mar 30 00:27:07 OddId vmnet-bridge[1552]: Preferences changed called", l.getLine());
        
    }
}
