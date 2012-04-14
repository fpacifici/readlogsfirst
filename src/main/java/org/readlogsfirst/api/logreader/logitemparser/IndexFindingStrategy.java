package org.readlogsfirst.api.logreader.logitemparser;

import java.util.List;

/**
 * Strategy used to extract the index from a list of tokens.
 * @author pyppo
 *
 */
public interface IndexFindingStrategy<T> {
	/**
	 * extracts the index from the token.
	 * @param tokens
	 * @return
	 */
	public T getIndex(List<?> tokens) throws LogItemParsingException;
}
