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
package org.readlogsfirst.api.configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.readlogsfirst.api.descriptors.BaseLogDescriptor;
import org.readlogsfirst.api.descriptors.LogDescriptor;
import org.readlogsfirst.api.logreader.logitemparser.LogItemParser;
import org.readlogsfirst.api.logreader.logitemparser.LogParserBuilder;
import org.readlogsfirst.api.logreader.loglines.MapLogLine;
import org.readlogsfirst.api.logreader.loglines.UnstructuredLogLine;
import org.readlogsfirst.api.logreader.logsplitter.LineSplittingStrategy;
import org.readlogsfirst.api.logreader.logsplitter.SplitterBuilder;

/**
 *
 * @author pyppo
 */
public class DescriptorParser {
    private XMLStreamReader reader;
    
    public DescriptorParser (XMLStreamReader reader){
        this.reader = reader;
    }
    
    Class logItemClass = null;
    
    /**
     * Parses one log descriptor.
     * It expects to be at the beginning of the element and goes up to the end
     * If the descriptor is not complete it fails.
     * @return
     * @throws XMLStreamException 
     */
    public LogDescriptor parseDescriptor() throws XMLStreamException{
        int event = reader.next();
        boolean splitterDone = false;
        boolean parserDone = false;
        LineSplittingStrategy splitter = null;
        LogItemParser parser = null;
        while(!foundItemEnd(event,"logDescriptor")){
            if (event == XMLStreamConstants.START_ELEMENT){
                String elemName = reader.getLocalName();
                if ("splitter".equals(elemName)){
                    splitter = parseSplitter();
                    if (splitter != null){
                        splitterDone = true;
                    }
                }else if ("parser".equals(elemName)){
                    parser = parseParser();
                    if (parser != null){
                        parserDone = true;
                    }
                }else {
                    throw new XMLStreamException("Found invalid element when searching for splitter or parser : " + elemName);
                }
            }
            event = reader.next();
        }
        if (!splitterDone || !parserDone){
            throw new XMLStreamException("Log descriptor incomplete");
        }
        
        BaseLogDescriptor desc = new BaseLogDescriptor();
        desc.setParser(parser);
        desc.setSplitter(splitter);
        return desc;
    }
    
    public Class getLogItemClass(){
        return logItemClass;
    }
    
    /**
     * parses a splitter.
     * @return
     * @throws XMLStreamException 
     */
    public LineSplittingStrategy parseSplitter() throws XMLStreamException {
        int event = reader.next();
        boolean splitterDone = false;
        LineSplittingStrategy splitter = null;
        SplitterBuilder builder = new SplitterBuilder();
        while (!foundItemEnd(event, "splitter")){
            if (event == XMLStreamConstants.START_ELEMENT){
                //searches for the splitter tyep
                String elemName = reader.getLocalName();
                if ("singlelinesplitter".equals(elemName)){
                   builder.withSingleLineSplitter();
                   splitter = builder.build();
                   splitterDone = true;
                }else if ("multilinesplitter".equals(elemName)){
                   //get attributes
                   String prefix = reader.getAttributeValue(null, "prefix");
                   String suffix = reader.getAttributeValue(null, "suffix");
                   String nothingAfter = reader.getAttributeValue(null, "nothingAfter");
                   String notab = reader.getAttributeValue(null, "notab");
                   
                   boolean bnotab = notab != null ? Boolean.valueOf(notab) : false; 
                   boolean bnothingafter = nothingAfter != null ? Boolean.valueOf(nothingAfter) : false; 
                   
                   if (suffix == null) {
                       splitter = builder.multiLineWithPrefix(prefix, bnotab).build();
                   }else {
                       splitter = builder.multiLineWithPrefix(prefix, suffix, bnotab, bnothingafter).build();
                   }
                   splitterDone = true;
                }else {
                    throw new XMLStreamException("Invalid element found instead of a splitter : " + elemName);
                }
            }
            event = reader.next();
        }
        if (!splitterDone) {
            throw new XMLStreamException("Did not find a splitter inside a splitter element");
        }
        return splitter;
    }
    
    /**
     * Parses the configuration of a parser.
     * It manages directly the timestamp indexed and delegates 
     * @return
     * @throws XMLStreamException 
     */
    public LogItemParser parseParser() throws XMLStreamException {
        int event = reader.next();
        LogItemParser parser = null;
        LogParserBuilder builder = new LogParserBuilder();
        while (!foundItemEnd(event, "parser")){
            if (event == XMLStreamConstants.START_ELEMENT){
                String elemName = reader.getLocalName();
                if ("timestampIndex".equals(elemName)){
                    //gets attributes
                    String dateRegExp = reader.getAttributeValue(null, "dateRegExp");
                    String dateStart = reader.getAttributeValue(null, "posStart");
                    String dateEnd = reader.getAttributeValue(null, "posEnd");
                    String dateFormat = reader.getAttributeValue(null, "format");
                    
                    if (dateStart != null){
                        builder.unstructuredTimestamp(Integer.parseInt(dateStart), Integer.parseInt(dateEnd), 
                                dateFormat);
                    }else {
                        builder.unstructuredTimestamp(Pattern.compile(dateRegExp), dateFormat);
                    }
                    parser = builder.build();
                    logItemClass = UnstructuredLogLine.class;
                }else if ("complex".equals(elemName)){
                    parser = parseComplexParser();
                } else {
                    throw new XMLStreamException("Did not find any parser where expected");
                }
            }
            event = reader.next();
        }
        if (parser == null){
            throw new XMLStreamException("Did not find a parser element where expected");
        }
        return parser;
    }
    
    private LogItemParser parseComplexParser() throws XMLStreamException{
        int event = reader.next();
        LogItemParser parser = null;
        LogParserBuilder builder = new LogParserBuilder();
        while (!foundItemEnd(event, "complex")){
            if (event == XMLStreamConstants.START_ELEMENT){
                String elemName = reader.getLocalName();
                if ("simplesplittokenizer".equals(elemName)){
                    //split tokenizer
                    String splitStr = reader.getAttributeValue(null, "splitStr");
                    String start = reader.getAttributeValue(null, "start");
                    String stop = reader.getAttributeValue(null, "stop");
                    if (splitStr != null){
                        builder.withSimpleSplitTokenizer(splitStr);
                    }else {
                        builder.withSimpleSplitTokenizer(start, stop);
                    }
                }else if ("dateindexfinder".equals(elemName)){
                    String pattern = reader.getAttributeValue(null, "pattern");
                    String pos = reader.getAttributeValue(null, "position");
                    if (pos != null){
                        builder.withDateFinder(pattern,Integer.parseInt(pos));
                    }else {
                        builder.withDateFinder(pattern);
                    }
                }else if ("maplinebuilder".equals(elemName)){
                    List<String> keys = parseKeys();
                    builder.withDateMapBuilder(keys);
                }
            }
            
            event = reader.next();
        }
        parser = builder.build();
        logItemClass = MapLogLine.class;
        if (parser == null){
            throw new XMLStreamException("Did not find a complete complex parser element");
        }
        return parser;
    }
    
    private List<String> parseKeys() throws XMLStreamException{
        int event = reader.next();
        List<String> ret = new LinkedList<String>();
        while(!foundItemEnd(event, "maplinebuilder")){
            if (event == XMLStreamConstants.START_ELEMENT){
                String elemName = reader.getLocalName();
                if ("key".equals(elemName)){
                    event = reader.next();
                    ret.add(reader.getText());
                }
            }
            event = reader.next();
        }
        return ret;
    }
    
    private boolean foundItemEnd(int event, String elemName){
        return (event == XMLStreamConstants.END_ELEMENT && elemName.equals(
            reader.getLocalName()));
    }

    
}
