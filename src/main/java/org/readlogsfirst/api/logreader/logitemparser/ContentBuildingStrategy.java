package org.readlogsfirst.api.logreader.logitemparser;

import java.util.List;

import org.readlogsfirst.api.logreader.LogLine;

/**
 * Finally builds the log line from the tokens
 * @author pyppo
 *
 */
public interface ContentBuildingStrategy<T extends Comparable<T>> {

	public LogLine<T> buildLine(List<?> tokens, T index) throws LogItemParsingException;
}
