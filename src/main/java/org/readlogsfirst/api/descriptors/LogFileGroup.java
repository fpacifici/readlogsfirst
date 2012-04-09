/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.descriptors;

import java.io.File;
import java.util.List;

/**
 * Provides the list of log files that are managed by a LogReader.
 * 
 * @author pyppo
 */
public interface LogFileGroup {
   
    /**
     * @return the list of files for a LogReader
     */
    public List<File> getFiles();
}
