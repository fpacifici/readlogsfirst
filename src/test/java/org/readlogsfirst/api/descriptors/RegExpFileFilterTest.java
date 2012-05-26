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
