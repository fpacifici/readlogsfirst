/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.logreader.basereader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.readlogsfirst.api.logreader.LogLine;
import org.readlogsfirst.api.logreader.logitemparser.LogItemParser;
import org.readlogsfirst.api.logreader.logitemparser.LogItemParsingException;
import org.readlogsfirst.api.logreader.logsplitter.LineSplittingStrategy;
import org.readlogsfirst.api.util.IssueLogger;

/**
 * Base iterator on a group of files. It does not provide any caching or
 * buffering. Files are supposed to be already open
 *
 * @author fpacifici
 */
public class MultiFileIterator implements Iterator<LogLine> {

    private TreeSet<FileEntry> openFileSet;
    private Iterator<BufferedReader> listPointer;
    BufferedReader current;
    private LineSplittingStrategy lineSplitting;
    private LogItemParser itemParser;
    private IssueLogger logger;
    
    private LogLine currentLine = null;

    public MultiFileIterator(LinkedList<BufferedReader> readers, LineSplittingStrategy splitterStrategy,
            LogItemParser itemParser, IssueLogger logger){
        
        listPointer = readers.iterator();
        if (listPointer.hasNext()) {
            current = listPointer.next();
        }
        this.lineSplitting = splitterStrategy;
        this.itemParser = itemParser;
        this.logger = logger;
    }

    /**
     * If a currentLine is ready it returns true, otherwise it tries to load one.
     * If a new can be loaded true is returned otherwise false is returned.
     * As long as a parsing exception is thrown, the exception is logged and a new line is searched.
     * A parsing exception does not stop the iterator.
     * @return 
     */
    public boolean hasNext() {
        if (currentLine != null) {
            return true;
        } else {
            boolean err = true;
            while (err){
                try {
                currentLine = readLine();
                err = false;
                } catch (IOException e) {
                    logger.genericIssue(e);
                    return false;
                } catch (LogItemParsingException e) {
                    logger.parsingException(e, e.getBadLine());
                }
            }
            
            if (currentLine != null) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * @return the next alement in the iterator. 
     * @throws NoSuchElementException if there are no more valid elements in the array.
     */
    public LogLine next() {
        if (hasNext()) {
            LogLine l = currentLine;
            currentLine = null;
            return l;
        } else {
            throw new NoSuchElementException("There are no more elements in this iterator");
        }
    }

    /**
     * Remove does not exist.
     */
    public void remove() {
        throw new UnsupportedOperationException("This is a log miner. Cannot modify the log");
    }

    /**
     * Reads a full log line recursively.
     *
     * Keeps the state in the line splitting strategy, if a line is ready to
     * return. It parses and resets linSplitting strategy. If it is not ready,
     * it tries to read something from current file, passing to the next one if
     * nothing is readable.
     *
     * If we are at the last file, and at the last line. It stays there
     * returnning null (a new line may be added later on). If instead we are at
     * the last line of an intermediate file in the set it switches to the next
     * to go on.
     *
     * @return the LogLine found or null if nothing is found or current logLine
     * is incomplete
     * @throws IOException
     * @throws LogItemParsingException if a complete line is identified (with
     * correct boundaries) but the format is not correct.
     */
    private LogLine readLine() throws IOException, LogItemParsingException {
        if (lineSplitting.getCurrentStatus() == LineSplittingStrategy.ConsumptionStatus.CONSUMED
                || lineSplitting.getCurrentStatus() == LineSplittingStrategy.ConsumptionStatus.BUFFERED) {
            String fulleLine = lineSplitting.getConsumed();
            return itemParser.parseLine(fulleLine);
        } else {
            if (current != null) {
                String fullLine = current.readLine();
                if (fullLine == null) {
                    //tries next file
                    if (listPointer.hasNext()) {
                        current = listPointer.next();
                        return readLine();
                    } else {
                        return null;
                    }
                } else {
                    lineSplitting.consumeLine(fullLine);
                    return readLine();
                }
            } else {
                return null;
            }
        }
    }
}

