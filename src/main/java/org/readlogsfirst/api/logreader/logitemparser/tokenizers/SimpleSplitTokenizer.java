package org.readlogsfirst.api.logreader.logitemparser.tokenizers;

import java.util.LinkedList;
import java.util.List;

import javax.swing.text.Position;

import org.readlogsfirst.api.logreader.logitemparser.LineTokenizerStrategy;
import org.readlogsfirst.api.logreader.logitemparser.LogItemParsingException;

/**
 * Tokenizer that splits the line according to a character or a string.
 * If a single character/sequence is provided the lsit is based on that.
 * It is possible to provide as well start/end of each field instead.
 * 
 * If no list of type converters is provided, tokens are provided as strings.
 * 
 * 
 * @author fpacifici
 *
 */
public class SimpleSplitTokenizer implements LineTokenizerStrategy {

	/**
	 * Splitter to be used
	 */
	private final String splitter;
	
	/**
	 * limits of each field.
	 */
	private final String fieldStarter, fieldStopper;
	
	/**
	 * Instantiate a simple tokenizer that uses a splitter to define tokens
	 * @param splitter
	 */
	public SimpleSplitTokenizer(String splitter) {
		super();
		this.splitter = splitter;
		fieldStarter = null;
		fieldStopper = null;
	}
	
	/**
	 * Instantiates a simple tokenizer providing start and stop for each field.
	 * @param fieldStarter
	 * @param fieldStopper
	 */
	public SimpleSplitTokenizer(String fieldStarter,
			String fieldStopper) {
		super();
		this.fieldStarter = fieldStarter;
		this.fieldStopper = fieldStopper;
		splitter = null;
	}

	/**
	 * Splits the line according to a character sequence splitter or to a starter/stopper strategy.
	 */
	public List<?> tokenize(String line) throws LogItemParsingException {
		if (line  == null){
			throw new IllegalArgumentException("Cannot parse a null line");
		}
		List<String> ret = new LinkedList<String>();
		int position = 0;
		while (position < line.length()-1){
			int[] newTokePositions = getToken(line, position);
			position = newTokePositions[2];
			if (newTokePositions[0] < line.length()){
				String token = line.substring(newTokePositions[0],newTokePositions[1]);
				ret.add(token);
				if (newTokePositions[0] == newTokePositions[1] && 
						newTokePositions[2] == line.length()
						&& splitter != null){
					//there is an empty string at the end of the line. I have to add it
					ret.add("");
				}
			}
		}
		return ret;
		
	}

	/**
	 * Starts from the startingPoint character, then it tries to find a token knowing the policy.
	 * @param line
	 * @param startingPoint
	 * @return the limits of the token. <aa><bbb><aa> will return 5,7,9 (start, stop, position after delimiter)
	 */
	private int[] getToken(String line, int startingPoint){
		int[] ret = new int[3];
		
		if (splitter != null){
			//finds next splitting point
			int nextSplit = line.indexOf(splitter,startingPoint);
			ret[0]=startingPoint;
			ret[1]=nextSplit >= 0 ? nextSplit : line.length();
			ret[2]=nextSplit >= 0 ? nextSplit + splitter.length()  : line.length() -1;
		}else {
			//finds a start and an end
			int startBeginner = line.indexOf(fieldStarter,startingPoint);
			ret[0] = startBeginner >= 0 ? startBeginner + fieldStarter.length()  : line.length();
			int startEnder = line.indexOf(fieldStopper, ret[0]);
			ret[1] = startEnder >= 0 ? startEnder  : line.length() -1 ;
			ret[2] = startEnder >= 0 ? startEnder + fieldStopper.length() : line.length() -1;
		}
		return ret;
	}
}
