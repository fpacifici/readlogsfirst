/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.logreader.basereader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import org.readlogsfirst.api.logreader.LogDataSet;
import org.readlogsfirst.api.logreader.LogLine;
import org.readlogsfirst.api.logreader.logitemparser.LogItemParser;
import org.readlogsfirst.api.logreader.logsplitter.LineSplittingStrategy;
import org.readlogsfirst.api.util.IssueLogger;

/**
 *
 * @author pyppo
 */
public class BaseLogDataSet implements LogDataSet{
    private LineSplittingStrategy splitter;
    private LogItemParser parser;
    private IssueLogger  logger;

    private LinkedList<BufferedReader> files;

    public BaseLogDataSet(LineSplittingStrategy splitter, LogItemParser parser, IssueLogger logger, LinkedList<BufferedReader> files) {
        this.splitter = splitter;
        this.parser = parser;
        this.logger = logger;
        this.files = files;
    }

    
    /**
     * @return the iterator over the set of files.
     * It resets all buffered readers in order to be able to reuse the same 
     * Logdataset more than once.
     */
    public Iterator<LogLine> iterator() {
        for (BufferedReader reader : files){
            try {
                reader.reset();
            }catch (IOException e){
                logger.genericIssue(e);
            }
        }
        return new MultiFileIterator(files, splitter, parser, logger);
    }
    
    
}
