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

import java.io.File;
import java.net.URL;
import java.util.TreeSet;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.readlogsfirst.api.descriptors.BaseLogDescriptor;
import org.readlogsfirst.api.descriptors.LogDescriptor;
import org.readlogsfirst.api.descriptors.LogFileGroup;
import org.readlogsfirst.api.descriptors.RegExpFileFilter;
import org.readlogsfirst.api.logreader.LogDataSet;
import org.readlogsfirst.api.logreader.LogLine;
import org.readlogsfirst.api.logreader.LogReader;
import org.readlogsfirst.api.logreader.logitemparser.LogItemParser;
import org.readlogsfirst.api.logreader.logitemparser.LogItemParsingException;
import org.readlogsfirst.api.logreader.logitemparser.TimestampIndexParse;
import org.readlogsfirst.api.logreader.logsplitter.LineSplittingStrategy;
import org.readlogsfirst.api.logreader.logsplitter.SingleLineSplittingStrategy;
import org.readlogsfirst.api.util.IssueLogger;
import org.readlogsfirst.api.util.SystemOutLogger;

/**
 * Tests te BaseLogReader.
 * @author pyppo
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SystemOutLogger.class)
public class BaseLogReaderTest {
    
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
        splitter = new SingleLineSplittingStrategy();
        parser = new TimestampIndexParse(
                Pattern.compile("\\w\\w\\w\\s[\\d\\s]\\d\\s\\d\\d.\\d\\d.\\d\\d")
                ,"MMM dd HH:mm:ss");
        
        URL url = this.getClass().getResource("/");
        File base = new File(url.getFile());
        logGroup = new RegExpFileFilter(base,false,Pattern.compile("system.*"));
        descriptor = new BaseLogDescriptor();
        descriptor.setParser(parser);
        descriptor.setSplitter(splitter);
    }
    
    /**
     * Tests the creation of the LogReader and checks that the files are in the correct
     * order.
     * @throws Exception 
     */
    @Test
    public void testFileSet() throws Exception {
        IssueLogger il = new SystemOutLogger();
        logger = PowerMockito.spy(il);
        
        BaseLogReader reader = new BaseLogReader(logger);
        reader.configure(descriptor, logGroup);
        
        
        TreeSet<FileEntry> set = reader.getFileSet();
        assertNotNull(set);
        assertEquals(4 , set.size());
        int cont = 0;
        for (FileEntry fe : set){
            File f = fe.file;
            String filename = f.getName();
            String expectedName = "system.log." + (3-cont);
            assertEquals(expectedName, filename);
            cont++;
        }
        
        Mockito.verify(logger,times(0)).genericIssue((Exception)Mockito.anyObject());
        Mockito.verify(logger,times(0)).parsingException(
                (LogItemParsingException)Mockito.anyObject(),Mockito.anyString());
    }
    
    /**
     * Iterate on the whole dataset.
     * @throws Exception 
     */
    @Test
    public void fullIterator() throws Exception {
        IssueLogger il = new SystemOutLogger();
        logger = PowerMockito.spy(il);
        Mockito.verify(logger,times(0)).genericIssue((Exception)Mockito.anyObject());
        
        
        BaseLogReader reader = new BaseLogReader(logger);
        reader.configure(descriptor, logGroup);
        
        int cont = 0;
        for (LogLine l : reader.getLogContent()){
            cont ++;
        }
        
        Mockito.verify(logger,times(1)).parsingException(
                (LogItemParsingException)Mockito.anyObject(),Mockito.anyString());
        assertEquals(6631, cont);
    }
   
}
