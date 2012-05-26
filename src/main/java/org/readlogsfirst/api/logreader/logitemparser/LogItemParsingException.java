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
package org.readlogsfirst.api.logreader.logitemparser;

/**
 * Points out an issue in parsing. 
 * It is a checked exception since it may happen that a log line is badly formatted.
 * It is needed to find this case and to manage it.
 * @author pyppo
 */
public class LogItemParsingException extends Exception{

    private final String badLine;
    
    public LogItemParsingException(String badLine,String string, Throwable thrwbl) {
        super(string, thrwbl);
        this.badLine = badLine;
    }

    public LogItemParsingException(String badLine, String string) {
        super(string);
        this.badLine = badLine;
    }

    public String getBadLine() {
        return badLine;
    }
    
    
    
    
}
