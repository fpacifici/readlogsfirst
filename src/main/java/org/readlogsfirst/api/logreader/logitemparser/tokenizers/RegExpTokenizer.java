package org.readlogsfirst.api.logreader.logitemparser.tokenizers;

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
	 * 
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
				int begin = m.regionStart();
				int end = m.regionEnd();
				String token = line.substring(begin, end);
				out.add(token);
			}
		}
		
		return out;
	}

}
