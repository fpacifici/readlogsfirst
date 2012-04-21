package org.readlogsfirst.api.logreader.logsplitter;

/**
 * Multiline splitter. In order to recognize log items containing \n character.
 * Each log item is recognized through a prefix like for weblogic system log
 * lines which always start with ###
 * 
 * @author fpacifici
 * 
 */
public class MultiLineLimitsSplitter implements LineSplittingStrategy {

	private final String prefix;

	private final String end;

	private StringBuilder readyBuffer = new StringBuilder();

	private String newBuffer;

	private ConsumptionStatus status = ConsumptionStatus.EMPTY;

	private boolean notab = true;

	private boolean nothingAfterEnd = true;

	/**
	 * Provides the prefix to be used.
	 * 
	 * @param prefix
	 * @param notab
	 *            , if true means that the prefix must always be at the
	 *            beginning of the line.
	 * @param nothingAfterEnd
	 *            means that once the end is found a \n is expected. A new log
	 *            items starts always ona new line.
	 */
	public MultiLineLimitsSplitter(String prefix, String end, boolean notab,
			boolean nothingAfterEnd) {
		super();
		this.notab = notab;
		this.prefix = prefix;
		this.end = end;
		this.nothingAfterEnd = nothingAfterEnd;
	}

	/**
	 * Provides only the prefix
	 * 
	 * @param prefix
	 * @param notab
	 */
	public MultiLineLimitsSplitter(String prefix, boolean notab) {
		this(prefix, null, notab, true);
	}

	/**
	 * {@inheritDoc}
	 * Consumes one line.
	 * First it searches for beginning and end.
	 * If the status is empty, the line is added and the status is set to consumed if the end is found.
	 * if the status is consuming, stores the buffer for the new line if the new line is found.
	 * otherwise I cannot consume thus IllegalStateException.
	 */
	public ConsumptionStatus consumeLine(String line) {
		if (line  == null){
			throw new IllegalArgumentException("Cannot consume a null line");
		}
		boolean pfound = isPrefixFound(line);
		boolean efound = isEndFound(line);
		if (status == ConsumptionStatus.EMPTY){
			if (pfound){
				readyBuffer.append(line);
				if (efound){
					status = ConsumptionStatus.CONSUMED;
				}else {
					status = ConsumptionStatus.CONSUMING;
				}
			}
		}else if (status == ConsumptionStatus.CONSUMING){
			if (pfound){
				newBuffer = line;
				status = ConsumptionStatus.BUFFERED;
			}else {
				readyBuffer.append(line);
				if (efound){
					status = ConsumptionStatus.CONSUMED;
				}
			}
		}else {
			throw new IllegalStateException("Cannot consume a line in status " + status);
		}
		return status;
	}

	/**
	 * Checks if the prefix is found in the input line.
	 * 
	 * @param line
	 * @return
	 */
	private boolean isPrefixFound(String line) {
		if (notab) {
			return line.startsWith(prefix);
		} else {
			return line.contains(prefix);
		}
	}

	/**
	 * Checks if the line ends with the end.
	 * 
	 * @param line
	 * @return
	 */
	private boolean isEndFound(String line) {
		if (end != null) {
			if (nothingAfterEnd) {
				return line.endsWith(end);
			} else {
				return line.contains(end);
			}
		} else {
			return false;
		}
	}

	/**
	 * Consumes the buffered line.
	 * @deprecated
	 */
	public ConsumptionStatus consumeBuffer() {
		if (status == ConsumptionStatus.BUFFERED){
			String buffered = newBuffer;
			newBuffer = null;
			status = ConsumptionStatus.EMPTY;
			consumeLine(buffered);
			return status;
		}else {
			throw new IllegalStateException("There is no buffer to consume.");
		}
	}

	/**
	 * {@inheritDoc}
	 * Returns a consumed log item.
	 * If we are not in CONSUMED or BUFFERED state it means there is no log itme ready to provision.
	 * In this case the content is purged and the splitter is reset.
	 *
	 * IF an item is ready, it is returned, and then, if there was some additional data, the state is set
	 * to CONSUMING otherwise it is set to EMPTY. 
	 */
	public String getConsumed() {
		String ret = null;
		if (status == ConsumptionStatus.CONSUMED || status == ConsumptionStatus.BUFFERED){
			//there is a ready line
			ret = readyBuffer.toString();
			readyBuffer = new StringBuilder();
			
			if (status == ConsumptionStatus.BUFFERED){
				status = ConsumptionStatus.EMPTY;
				String bufferedLine = newBuffer;
				newBuffer = null;
				consumeLine(bufferedLine);
			}else {
				status = ConsumptionStatus.EMPTY;
			}
			return ret;
		}else {
			ret = readyBuffer.toString();
			reset();
			return ret;
		}
	}

	/**
	 * return consumtpion status.
	 */
	public ConsumptionStatus getCurrentStatus() {
		return status;
	}

	/**
	 * Completely reset the LineSplittingStatus
	 */
	public void reset() {
		newBuffer = null;
		readyBuffer = new StringBuilder();
		status = ConsumptionStatus.EMPTY;
	}

}
