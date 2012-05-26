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

import org.readlogsfirst.api.logreader.LogLine;
import org.readlogsfirst.api.logreader.logitemparser.LogItemParser;
import org.readlogsfirst.api.logreader.logsplitter.LineSplittingStrategy;

/**
 * Provides the infrastructures needed t read and parse a log file.
 * 
 * @author fpacifici
 */
public interface LogDescriptor {
    /**
     * Splitting strategy provides the strategy pattern for splitting the log file
     * into log items. It does not parse the line but it knows how to split 
     * lines.
     * @return 
     */
    public LineSplittingStrategy getSplittingStrategy();
    
    /**
     * Parser provides the strategy pattern for parsing a log item split through SplittingStrategy.
     * 
     * @return 
     */
    public LogItemParser getParser();
}
