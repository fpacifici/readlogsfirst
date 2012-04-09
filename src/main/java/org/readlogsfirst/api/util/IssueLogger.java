/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.util;

import org.readlogsfirst.api.logreader.logitemparser.LogItemParsingException;

/**
 * Used to allow log readers to communicate anomalies in the log files that
 * bring log lines to be discarded, but such that the iterator should not stop
 * while keeping track of the issues themselves.
 * 
 * This is not to be considered a logger since, according to the implementation
 * it can log the issue, discard the issue, or report the issue to the user in
 * a different way.
 * 
 * @author pyppo
 */
public interface IssueLogger {
   
    /**
     * Signals a parsing exception on a logLine.
     * @param e 
     */
    public void parsingException(LogItemParsingException e, String line);
    
    public void genericIssue(Exception e);
}
