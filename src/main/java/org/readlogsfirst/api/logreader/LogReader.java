/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.logreader;

import java.io.IOException;
import org.readlogsfirst.api.descriptors.LogDescriptor;
import java.util.Iterator;
import org.readlogsfirst.api.descriptors.LogFileGroup;
import org.readlogsfirst.api.query.LogQuery;

/**
 * Base interface of any log abstraction.
 * Classes implmenting this interface put an abstraction on the real log 
 * system, like single file based, multi file based, database, ....
 * 
 * This provides a way of iterating over the log source obtaining entities 
 * of LogLine elements.
 * 
 * Each instance is supposed to be configured through a LogDescriptor instance
 * that provides the way to parse the log.
 * 
 * @author fpacifici
 */
public interface LogReader{
    
    /**
     * Set the configuration to this Reader.
     * @param desc 
     */
    public void configure(LogDescriptor desc, LogFileGroup files) throws IOException;

    /**
     * @return the full content of the logger. 
     */
    public LogDataSet getLogContent();
    
    /**
     * @param query
     * @return the queried log content
     */
    public LogDataSet getLogContent(LogQuery query);
    
}
