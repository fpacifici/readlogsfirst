package org.readlogsfirst.api.logreader.logitemparser.builders;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.readlogsfirst.api.logreader.LogLine;
import org.readlogsfirst.api.logreader.logitemparser.ContentBuildingStrategy;
import org.readlogsfirst.api.logreader.logitemparser.LogItemParsingException;
import org.readlogsfirst.api.logreader.loglines.MapLogLine;

/**
 * Builds a LogLine as a Map.
 * At each build method invocation the list of tokens is passed together with the index value.
 * At the same time the keys of the map are passed at instantiation. If not list of keys
 * is given, keys will be sequence numbers of the tokens.
 * 
 * @author fpacifici
 *
 */
public class MapLineBuilder implements ContentBuildingStrategy<Date> {

	private static List<String> keys;
	
	/**
	 * Builds the builder with the list of keys.
	 * @param keys
	 */
	public MapLineBuilder(List<String> keys){
		this.keys = keys;
	}
	
	/**
	 * Builds the builder without keys.
	 */
	public MapLineBuilder() {
		this.keys = null;
	}
	
	/**
	 * Returns a Map log line including the tokens passed as input.
	 */
	public LogLine<Date> buildLine(List<?> tokens, Date index)
			throws LogItemParsingException {
		if (tokens == null){
			throw new IllegalArgumentException("Cannot build a line if tokens are null");
		}
		Map<String,Object> content = new HashMap<String, Object>(tokens.size());
		int cont = 0;
		for (Object t : tokens){
			String colName; 
			if (keys != null){
				colName = keys.get(cont);
			}else {
				colName = Integer.toString(cont);
			}
			
			content.put(colName, t);
			cont++;
		}
		
		return new MapLogLine<Date>(content, index);
	}
	
}
