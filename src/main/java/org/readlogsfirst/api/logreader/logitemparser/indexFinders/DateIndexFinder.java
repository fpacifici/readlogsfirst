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
package org.readlogsfirst.api.logreader.logitemparser.indexFinders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.readlogsfirst.api.logreader.logitemparser.IndexFindingStrategy;
import org.readlogsfirst.api.logreader.logitemparser.LogItemParsingException;

/**
 * Extracts the timestamp from the list of tokens from the position passed during
 * initialization. It formats the string according to the pattern passed through initializaiton.
 * and substitutes the String with the Date
 * 
 * @author fpacifici
 *
 */
public class DateIndexFinder implements IndexFindingStrategy<Date> {

	private final SimpleDateFormat formatter;
	
	private final int position;
	
	/**
	 * Instantiates the finder with a pattern.
	 * @param pattern
	 * @param position
	 */
	public DateIndexFinder(String pattern, int position) {
		super();
		this.formatter = new SimpleDateFormat(pattern);
		this.position = position;
	}

	/**
	 * Instantiates the finder with the pattern and 0 as position.
	 * @param pattern
	 */
	public DateIndexFinder(String pattern){
		this(pattern,0);
	}

	/**
	 * Locates the index through position and formats it as a date.
	 */
	public Date getIndex(List<?> tokens) throws LogItemParsingException {
		if (tokens == null){
			throw new IllegalArgumentException("Cannot search the index on a null list");
		}
		if (position >= tokens.size()){
			throw new LogItemParsingException("", "Tokens set is not big enough - " + tokens.size() + " - while position is " + position);
		}
		
		String dateString = (String)tokens.get(position);
		try {
			return formatter.parse(dateString);
		}catch(ParseException e){
			throw new LogItemParsingException("", "Timestamp is not pèarsable", e);
		}
	}

	
}
