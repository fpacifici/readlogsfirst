package org.readlogsfirst.api.logreader.logitemparser;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.readlogsfirst.api.descriptors.RegExpFileFilter;
import org.readlogsfirst.api.logreader.logitemparser.builders.MapLineBuilder;
import org.readlogsfirst.api.logreader.logitemparser.indexFinders.DateIndexFinder;
import org.readlogsfirst.api.logreader.logitemparser.tokenizers.RegExpTokenizer;
import org.readlogsfirst.api.logreader.logitemparser.tokenizers.SimpleSplitTokenizer;

/**
 * This is used to build the parser.
 * 
 * It is parametric with respect to the index type.
 * 
 * @author fpacifici
 *
 */
public class LogParserBuilder<T extends Comparable<T>> {

	//Strategy based parser
	private LineTokenizerStrategy tokenizer;	
	private IndexFindingStrategy<?> finder;	
	private LineFixerStrategy fixer;	
	private ContentBuildingStrategy<?> outputBuilder;
	
	//Simple parser
	private LogItemParser parser;
	
	public LogParserBuilder<T> unstructuredTimestamp(int dateStart, int dateEnd, String dateFormat){
		if (tokenizer != null || finder != null || fixer != null || outputBuilder != null || parser != null){
			throw new IllegalStateException("Parser has already been prepared");
		}
		parser = new TimestampIndexParse(dateStart, dateEnd, dateFormat);
		return this;
	}
	
	public LogParserBuilder<T> unstructuredTimestamp(Pattern dateRegExp, String dateFormat){
		if (tokenizer != null || finder != null || fixer != null || outputBuilder != null || parser != null){
			throw new IllegalStateException("Parser has already been prepared");
		}
		parser = new TimestampIndexParse(dateRegExp, dateFormat);
		return this;
	}
	
	//strategy based parser
	//tokenizers
	private void checkTokenizerPresent() {
		if (tokenizer != null || parser != null){
			throw new IllegalStateException("Cannot set tokenizer since already set.");
		}
	}
	
	public LogParserBuilder<T> withRegExpTokenizer(List<String> sregexps){
		checkTokenizerPresent();
		tokenizer = new RegExpTokenizer(sregexps);
		return this;
	}
	
	public LogParserBuilder<T> withRegExpTokenizer(String[] sregexps){
		checkTokenizerPresent();
		tokenizer = new RegExpTokenizer(sregexps);
		return this;
	} 
	
	public LogParserBuilder<T> withSimpleSplitTokenizer(String splitter){
		checkTokenizerPresent();
		tokenizer = new SimpleSplitTokenizer(splitter);
		return this;
	}
	
	public LogParserBuilder<T> withSimpleSplitTokenizer(String start, String end){
		checkTokenizerPresent();
		tokenizer = new SimpleSplitTokenizer(start, end);
		return this;
	}
	
	//builds index finder
	private void checkIndex(){
		if (finder != null || parser != null){
			throw new IllegalStateException("Cannot set index finder since already set.");
		}
	}
	
	public LogParserBuilder<T> withDateFinder(String pattern, int position){
		checkIndex();
		finder = new DateIndexFinder(pattern, position);
		return this;
	}
	
	public LogParserBuilder<T> withDateFinder(String pattern){
		checkIndex();
		finder = new DateIndexFinder(pattern);
		return this;
	}
	
	//build the line builder.
	private void checkBuilder(){
		if (outputBuilder != null || parser != null){
			throw new IllegalStateException("Cannot set builder since it is already set.");
		}
	}
	
	public LogParserBuilder<T> withDateMapBuilder(List<String> keys){
		checkBuilder();
		outputBuilder = new MapLineBuilder(keys);
		return this;
	}
	
	public LogParserBuilder<T> withDateMapBuilder(String keys[]){
		checkBuilder();
		outputBuilder = new MapLineBuilder(keys);
		return this;
	}
	
	public LogParserBuilder<T> withDateMapBuilder(){
		checkBuilder();
		outputBuilder = new MapLineBuilder();
		return this;
	}
	
	public LogItemParser build(){
		if (parser != null){
			return parser;
		}else {
			if (tokenizer == null || outputBuilder == null){
				throw new IllegalStateException("Cannot build parser without tokenizer and outputbuilder");
			}
			return new LogParserContext<T>(tokenizer, (IndexFindingStrategy<T>)finder, fixer, (ContentBuildingStrategy<T>)outputBuilder);
		}
	}
}