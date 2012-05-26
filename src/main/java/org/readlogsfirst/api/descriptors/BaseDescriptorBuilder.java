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
