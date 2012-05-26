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
package org.readlogsfirst.api.logreader;

import java.io.IOException;
import org.readlogsfirst.api.descriptors.LogDescriptor;
import java.util.Iterator;
import org.readlogsfirst.api.descriptors.LogFileGroup;
import org.readlogsfirst.api.query.LogQuery;

/**
 * Base interface of any log abstraction.
 * Classes implmenting this interface put an abstraction on the real log 
 * system, like single file based, multi file based, database, ....
 * 
 * This provides a way of iterating over the log source obtaining entities 
 * of LogLine elements.
 * 
 * Each instance is supposed to be configured through a LogDescriptor instance
 * that provides the way to parse the log.
 * 
 * @author fpacifici
 */
public interface LogReader{
    
    /**
     * Set the configuration to this Reader.
     * @param desc 
     */
    public void configure(LogDescriptor desc, LogFileGroup files) throws IOException;

    /**
     * @return the full content of the logger. 
     */
    public LogDataSet getLogContent();
    
    /**
     * @param query
     * @return the queried log content
     */
    public LogDataSet getLogContent(LogQuery query);
    
}
