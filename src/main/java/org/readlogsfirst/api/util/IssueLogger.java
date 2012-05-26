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
package org.readlogsfirst.api.util;

import org.readlogsfirst.api.logreader.logitemparser.LogItemParsingException;

/**
 * Used to allow log readers to communicate anomalies in the log files that
 * bring log lines to be discarded, but such that the iterator should not stop
 * while keeping track of the issues themselves.
 * 
 * This is not to be considered a logger since, according to the implementation
 * it can log the issue, discard the issue, or report the issue to the user in
 * a different way.
 * 
 * @author pyppo
 */
public interface IssueLogger {
   
    /**
     * Signals a parsing exception on a logLine.
     * @param e 
     */
    public void parsingException(LogItemParsingException e, String line);
    
    public void genericIssue(Exception e);
}
