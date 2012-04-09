/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.logreader.logitemparser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.readlogsfirst.api.logreader.LogLine;
import org.readlogsfirst.api.logreader.UnstructuredLogLine;

/**
 * This parser generates unstructured log lines with a date as index.
 * The date can be found either through regexp or searching for a specific 
 * part of the line. Date format can be passed.
 * @author pyppo
 */
public class TimestampIndexParse implements LogItemParser{

    private final int dateStart;
    
    private final int dateEnd;
    
    private final Pattern dateRegExp;
    
    private final SimpleDateFormat dateFormatter;

    /**
     * Creates parser passing expected date position.
     * @param dateStart
     * @param dateEnd
     * @param dateFormat 
     */
    public TimestampIndexParse(int dateStart, int dateEnd, String dateFormat) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.dateRegExp = null;
        this.dateFormatter = new SimpleDateFormat(dateFormat);
    }

    /**
     * Creates a parser passing date regular expression.
     * @param dateRegExp
     * @param dateFormat 
     */
    public TimestampIndexParse(Pattern dateRegExp, String dateFormat) {
        this.dateRegExp = dateRegExp;
        this.dateFormatter = new SimpleDateFormat(dateFormat);
        this.dateStart = -1;
        this.dateEnd = -1;
    }
    
    /**
     * Parses a line which is supposed to have a timestamp index.
     * It returns an UnstructuredLogLine generic on Date.
     * @param item
     * @return 
     */
    public LogLine<Date> parseLine(String item) throws LogItemParsingException{
        Date d = getDate(item);
        return new UnstructuredLogLine<Date>(item,d); 
    }
    
    /**
     * Finds the date on log item.
     * @param item
     * @return 
     */
    private Date getDate(String item) throws LogItemParsingException{
        String dateToParse = null;
        if (dateRegExp != null){
            Matcher m = dateRegExp.matcher(item);
            boolean found = m.find();
            if (!found)
                throw new LogItemParsingException(item,"Date not found in input item");
            else {
                dateToParse = m.group();
            }
        }else {
            dateToParse = item.substring(dateStart,dateEnd);
        }
        
        //parsing now
        try{
            return dateFormatter.parse(dateToParse);
        }catch(ParseException e){
            throw new LogItemParsingException(item,"Cannot parse date",e);
        }
    }
}
