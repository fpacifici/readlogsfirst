/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.descriptors;

import org.readlogsfirst.api.logreader.LogLine;
import org.readlogsfirst.api.logreader.logitemparser.LogItemParser;
import org.readlogsfirst.api.logreader.logsplitter.LineSplittingStrategy;

/**
 * Provides the infrastructures needed t read and parse a log file.
 * 
 * @author fpacifici
 */
public interface LogDescriptor {
    /**
     * Splitting strategy provides the strategy pattern for splitting the log file
     * into log items. It does not parse the line but it knows how to split 
     * lines.
     * @return 
     */
    public LineSplittingStrategy getSplittingStrategy();
    
    /**
     * Parser provides the strategy pattern for parsing a log item split through SplittingStrategy.
     * 
     * @return 
     */
    public LogItemParser getParser();
}
