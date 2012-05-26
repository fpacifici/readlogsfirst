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
package org.readlogsfirst.api.logreader.basereader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;
import java.io.File;
import java.util.LinkedList;
import org.readlogsfirst.api.descriptors.LogDescriptor;
import org.readlogsfirst.api.descriptors.LogFileGroup;
import org.readlogsfirst.api.logreader.LogDataSet;
import org.readlogsfirst.api.logreader.LogLine;
import org.readlogsfirst.api.logreader.LogReader;
import org.readlogsfirst.api.logreader.logitemparser.LogItemParser;
import org.readlogsfirst.api.logreader.logitemparser.LogItemParsingException;
import org.readlogsfirst.api.logreader.logsplitter.LineSplittingStrategy;
import org.readlogsfirst.api.query.LogQuery;
import org.readlogsfirst.api.util.IssueLogger;

/**
 * Basic Log reader.
 * It is configured through a LogDescriptor which provides a splitter and a parser.
 * 
 * @author pyppo
 */
public class BaseLogReader implements LogReader{

    private LineSplittingStrategy splitter;
    private LogItemParser parser;
    private IssueLogger  logger;
    
    
    private TreeSet<FileEntry> fileSet;

    public BaseLogReader(IssueLogger logger) {
        this.logger = logger;
    }
    
    
    
    /**
     * Provide log descriptor.
     * It also initializes the log files list opening each file, finding the first
     * line and ordering them through the index.
     * @param desc 
     */
    public void configure(LogDescriptor desc, LogFileGroup files) throws IOException{
        if (desc == null){
            throw new IllegalArgumentException("Cannot initialize log reader without splitter and parser");
        }
        splitter = desc.getSplittingStrategy();
        parser = desc.getParser();
        initFileGroup(files);
    }
    
    protected TreeSet<FileEntry> getFileSet(){
        return fileSet;
    }

    /**
     * Initialize the files list. 
     * @param files 
     */
    private void initFileGroup(LogFileGroup files) throws IOException{
        fileSet = new TreeSet<FileEntry>();
        for (File f : files.getFiles()){
            BufferedReader reader = new BufferedReader(
                    new FileReader(f)
                    );
            LogLine l = findFirstLine(reader);
            reader.close();
            if (l != null){
                Comparable c = l.getIndex();
                FileEntry e = new FileEntry(c, f);
                fileSet.add(e);
            }
        }
    }
    
    /**
     * Searches for the first LogLine in the file to extract the index.
     * If the first line is not parsable, it invokes it back recursively.
     * @param reader
     * @return 
     */
    private LogLine findFirstLine(BufferedReader reader){
        splitter.reset();
        LineSplittingStrategy.ConsumptionStatus status = splitter.getCurrentStatus();
        
        while (status != LineSplittingStrategy.ConsumptionStatus.CONSUMED &&
                status != LineSplittingStrategy.ConsumptionStatus.BUFFERED){
            String fileLine;
            try {                
                fileLine = reader.readLine();
                status = splitter.consumeLine(fileLine);
            }catch(IOException e){
                logger.genericIssue(e);
                return null;
            }
        }
        
        //parse line
        try {
            LogLine l = parser.parseLine(splitter.getConsumed());
            return l;
        }catch(LogItemParsingException e){
            logger.parsingException(e, "Cannot parse fisrt line in file");
            return findFirstLine(reader);
        }
    }
    
    /**
     * Returns the data set for this logger without queries.
     * It opens the buffered readers as well.
     * @return the LogDataSet
     */
    public LogDataSet getLogContent() {
        LinkedList<BufferedReader> filesList = new LinkedList<BufferedReader>();
        for (FileEntry entry : fileSet){
            try{
                BufferedReader reader = new BufferedReader(
                        new FileReader(entry.file));
                reader.mark(0);
                filesList.addLast(reader);
            }catch(IOException e){
                logger.genericIssue(e);
            }
        }
        return new BaseLogDataSet(splitter, parser, logger, filesList);
    }

    public LogDataSet getLogContent(LogQuery query) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
