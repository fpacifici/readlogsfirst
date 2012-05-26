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
