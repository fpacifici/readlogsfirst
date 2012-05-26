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
package org.readlogsfirst.api.logreader.logitemparser.tokenizers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.readlogsfirst.api.logreader.logitemparser.LineTokenizerStrategy;
import org.readlogsfirst.api.logreader.logitemparser.LogItemParsingException;

/**
 * Tokenizer that splits the log line through a series of regular expressions.
 * Regular expresisons are provided as a list of strings.
 * They are eavluated one at a time.
 * 
 * @author fpacifici
 *
 */
public class RegExpTokenizer implements LineTokenizerStrategy {

	private List<Pattern> regexps;
	
	/**
	 * Compiles every reg exp passed as input.
	 * @param sregexps
	 */
	public RegExpTokenizer(List<String> sregexps){
		regexps = new LinkedList<Pattern>();
		for (String s : sregexps){
			regexps.add(Pattern.compile(s));
		}
	}
	
	/**
	 * Easier way to pass the regular expressions
	 * @param sregexp
	 */
	public RegExpTokenizer(String[] sregexp){
		this(Arrays.asList(sregexp));
	}
	
	/**
	 * {@inheritDoc}
	 * Tokenizes the string according to the list of regular expressions given during initialization.
	 * 
	 * each expression is searched for and the result is added to the output.
	 */
	public List<?> tokenize(String line) throws LogItemParsingException {
		List<String> out = new LinkedList<String>();
		if (line == null) {
			throw new IllegalArgumentException("Cannot tokenize a null line");
		}
		Iterator<Pattern> it = regexps.iterator();
		int position = 0;
		while (it.hasNext()){
			Pattern p = it.next();
			Matcher m = p.matcher(line);
			boolean found = m.find(position);
			if (!found){
				throw new LogItemParsingException(line, "Did not contain " + p.pattern());
			}else {
				int begin = m.start();
				int end = m.end();
				String token = line.substring(begin, end);
				out.add(token);
				position = end;
			}
		}
		
		return out;
	}

}
