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
