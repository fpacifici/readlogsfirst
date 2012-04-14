package org.readlogsfirst.api.logreader.logitemparser;

import java.util.List;

/**
 * Normalize common issues in log lines (like badly formatted lines) when possible
 * @author pyppo
 *
 */
public interface LineFixerStrategy {
	/**
	 * Tries to fix the line.
	 * @param tokens
	 * @return
	 */
	public List<?> fixLine(List<?> tokens) throws LogItemParsingException;
}
