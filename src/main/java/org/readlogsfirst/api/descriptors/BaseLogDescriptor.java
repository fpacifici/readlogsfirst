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
