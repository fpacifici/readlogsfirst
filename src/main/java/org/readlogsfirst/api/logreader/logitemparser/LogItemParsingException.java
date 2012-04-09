/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.logreader.logitemparser;

/**
 * Points out an issue in parsing. 
 * It is a checked exception since it may happen that a log line is badly formatted.
 * It is needed to find this case and to manage it.
 * @author pyppo
 */
public class LogItemParsingException extends Exception{

    private final String badLine;
    
    public LogItemParsingException(String badLine,String string, Throwable thrwbl) {
        super(string, thrwbl);
        this.badLine = badLine;
    }

    public LogItemParsingException(String badLine, String string) {
        super(string);
        this.badLine = badLine;
    }

    public String getBadLine() {
        return badLine;
    }
    
    
    
    
}
