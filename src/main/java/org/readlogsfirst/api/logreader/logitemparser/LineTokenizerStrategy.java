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

import java.util.List;

/**
 * Splits a log line given as a string into tokens.
 * 
 * @author pyppo
 *
 */
public interface LineTokenizerStrategy {
	/**
	 * Gets a line from the splitter and tokenize it according to the policy 
	 * provided by the implementation.
	 * @param line is the input line
	 * @return the list of tokens.
	 */
	public List<?> tokenize(String line) throws LogItemParsingException;
}
