/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.descriptors;

import org.readlogsfirst.api.logreader.logitemparser.LogItemParser;
import org.readlogsfirst.api.logreader.logsplitter.LineSplittingStrategy;

/**
 * Basic log descriptor hosting a parser and a split strategy.
 * @author pyppo
 */
public class BaseLogDescriptor implements LogDescriptor{

    private LogItemParser parser;
    
    private LineSplittingStrategy splitter;
    
    
    
    /**
     * returns the parser
     * @return 
     */
    public LogItemParser getParser() {
        return parser;
    }

    /**
     * returns the splitting strategy
     * @return 
     */
    public LineSplittingStrategy getSplittingStrategy() {
        return splitter;
    }

    public void setParser(LogItemParser parser) {
        this.parser = parser;
    }

    public void setSplitter(LineSplittingStrategy splitter) {
        this.splitter = splitter;
    }
    
    
    
}
