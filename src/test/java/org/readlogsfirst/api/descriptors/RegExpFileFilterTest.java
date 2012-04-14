/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.descriptors;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the ability of searching agroup of files
 * 
 * @author pyppo
 */
public class RegExpFileFilterTest {
    
    public RegExpFileFilterTest() {
    }

    /**
     * Tests getting the simple list of files.
     * @throws Exception 
     */
    @Test
    public void testNonRecursiveNonRegexp() throws Exception {   
        URL url = this.getClass().getResource("/fileSet");
        File base = new File(url.getFile());
        LogFileGroup filter1 = new RegExpFileFilter(base,false);
        List<File> ret = filter1.getFiles();
        
        for (File f: ret){
            if (!f.isDirectory() && !f.getName().startsWith(".")){
                assertTrue(f.getName().startsWith("testFile"));
                assertTrue(f.getPath().contains("fileSet"));
            }
        }
    }
    
    /**
     * Tests getting a list of files recursively
     * @throws Exception 
     */
    @Test
    public void testRecursiveNonRegexp() throws Exception {
        URL url = this.getClass().getResource("/fileSet");
        File base = new File(url.getFile());
        LogFileGroup filter1 = new RegExpFileFilter(base,true);
        List<File> ret = filter1.getFiles();
        
        assertTrue(ret.size() >= 4);
        for (File f : ret){
            assertFalse(f.isDirectory());
        }
    }
    
    /**
     * Tests getting a list of file with regular expression
     * @throws Exception 
     */
    @Test
    public void testRecursiveRegexp() throws Exception {
        URL url = this.getClass().getResource("/fileSet");
        File base = new File(url.getFile());
        LogFileGroup filter1 = new RegExpFileFilter(base,true,Pattern.compile("testFile.*"));
        List<File> ret = filter1.getFiles();
        
        assertTrue(ret.size() == 3);
        for (File f: ret){
            assertTrue(f.getName().startsWith("testFile"));
            assertTrue(f.getPath().contains("fileSet"));
        }
    }
}
