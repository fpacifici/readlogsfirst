/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.util;

import org.readlogsfirst.api.logreader.logitemparser.LogItemParsingException;

/**
 * Logs issues on standard out.
 * TODO: this abomination should be removed.
 * @author pyppo
 */
public class SystemOutLogger implements IssueLogger{

    /**
     * Prints the error.
     * @param e 
     */
    public void parsingException(LogItemParsingException e, String line) {
        System.out.println("Bad input line: "+ line);
        e.printStackTrace();
    }

    public void genericIssue(Exception e) {
        e.printStackTrace();
    }
    
    
}
