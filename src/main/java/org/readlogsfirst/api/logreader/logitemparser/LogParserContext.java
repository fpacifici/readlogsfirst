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

import org.readlogsfirst.api.logreader.LogLine;

/**
 * A generic parser that uses four strategies to build up a LogLine structure. 
 * These four strategies are:
 * 
 * <b>LineTokenizer</b>, which takes the full (multiline) log line as a string and knows
 * how to split it into several tokens, no matter which is the meaning. It anyway knows 
 * tokens datatypes.
 * 
 * <b>IndexFinding</b>, which given the list of tokens finds the one corresponding to the index
 * (to be expanded in case of multi-index logs)
 * 
 * <b>LineFixer</b>, which tries to fix lines in cases they are not compliant. It can have maning 
 * for complex structures log file types like weblogic one.
 * 
 * <b>ContentBuilding</b>, which , given the list of tokens and the index, builds the LogLine.
 * 
 * @author fpacifici
 *
 */
public class LogParserContext<T extends Comparable<T>> implements LogItemParser{

	private final LineTokenizerStrategy tokenizer;
	
	private final IndexFindingStrategy<T> indexer;
	
	private final LineFixerStrategy fixer;
	
	private final ContentBuildingStrategy<T> builder;
	
	/**
	 * Base constructor.
	 * @param tokenizer
	 * @param indexer
	 * @param fixer
	 * @param builder
	 */
	public LogParserContext(LineTokenizerStrategy tokenizer,
			IndexFindingStrategy<T> indexer, LineFixerStrategy fixer,
			ContentBuildingStrategy<T> builder) {
		super();
		if (tokenizer == null){
			throw new IllegalArgumentException("Cannot instantiate a parser without tokenizer");
		}
		if (builder == null){
			throw new IllegalArgumentException("Cannot instantiate a parser without builder");
		}
		this.tokenizer = tokenizer;
		this.indexer = indexer;
		this.fixer = fixer;
		this.builder = builder;
	}

	/**
	 * Simpler constructor. not setting up an indexer or a fixer.
	 * @param tokenizer
	 * @param builder
	 */
	public LogParserContext(LineTokenizerStrategy tokenizer, ContentBuildingStrategy<T> builder){
		this(tokenizer,null,null,builder);
	}


	/**
	 * Main algorithm to parse a log item (where an item can be a set of lines in the file 
	 * identifying a log event.
	 * 
	 * Parsing process goes through tokenizing the event. This generates a list of tokens.
	 * Extracting the index. If an indexer is not provided, the first token is considered as 
	 * index. For some logs a fixer can be used to rebuild badly formatted lines. If the fixer 
	 * is not present, no fixing is performed. Once this is done, the builder creates the log
	 * line to be returned starting from the tokens.
	 * 
	 * @param item is the multiline or single line log event as got from the file
	 * @return the LogLine object. It is generic: the parameter is the type to be used as index.
	 */
	public LogLine<T> parseLine(String item) throws LogItemParsingException {
		T index;
		
		//tokenizes
		List<?> tokens = tokenizer.tokenize(item);
		if (tokens == null || tokens.size()  == 0)
			throw new LogItemParsingException(item, "Found 0 or null token");
		//extract index
		if (indexer != null){
			index = indexer.getIndex(tokens);
		}else {
			index = (T) tokens.get(0);
		}
		
		//fix it if needed
		if (fixer != null){
			tokens = fixer.fixLine(tokens);
		}
		
		return builder.buildLine(tokens, index);
		
	}
	
}
