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
 * Logs issues on standard out.
 * TODO: this abomination should be removed.
 * @author pyppo
 */
public class SystemOutLogger implements IssueLogger{

    /**
     * Prints the error.
     * @param e 
     */
    public void parsingException(LogItemParsingException e, String line) {
        System.out.println("Bad input line: "+ line);
        e.printStackTrace();
    }

    public void genericIssue(Exception e) {
        e.printStackTrace();
    }
    
    
}
