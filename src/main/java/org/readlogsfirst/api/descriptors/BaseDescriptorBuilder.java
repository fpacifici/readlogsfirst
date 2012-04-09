/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.descriptors;

import java.util.regex.Pattern;
import org.readlogsfirst.api.logreader.logitemparser.LogItemParser;
import org.readlogsfirst.api.logreader.logitemparser.TimestampIndexParse;
import org.readlogsfirst.api.logreader.logsplitter.LineSplittingStrategy;
import org.readlogsfirst.api.logreader.logsplitter.SingleLineSplittingStrategy;

/**
 *
 * @author pyppo
 */
public class BaseDescriptorBuilder {
    LineSplittingStrategy splitter = null;
    
    LogItemParser parser=null;
    
    public BaseDescriptorBuilder withSingleLineSplit(){
        splitter = new SingleLineSplittingStrategy();
        return this;
    }
    
    public BaseDescriptorBuilder withTimeStampIndex(String regexp, String dateFormat){
        Pattern p = Pattern.compile(regexp);
        parser = new TimestampIndexParse(p,dateFormat);
        return this;
    }
    
    public BaseDescriptorBuilder withTimeStampIndex(int start, int end, String dateFormat){
        parser = new TimestampIndexParse(start, end, dateFormat);
        return this;
    }
    
    public LogDescriptor build(){
        BaseLogDescriptor desc = new BaseLogDescriptor();
        desc.setParser(parser);
        desc.setSplitter(splitter);
        return desc;
    }
    
}
