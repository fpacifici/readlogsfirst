/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.logreader.logitemparser;

import org.readlogsfirst.api.logreader.LogLine;

/**
 * Log item parser get a log item as a String and parse it producing
 * a LogLine object.
 * @author fpacifici
 */
public interface LogItemParser {
    
    /**
     * Parses a log item (normally got through a LogSplitter).
     * 
     * @param item
     * @return 
     */
    public LogLine parseLine(String item) throws LogItemParsingException;
}
