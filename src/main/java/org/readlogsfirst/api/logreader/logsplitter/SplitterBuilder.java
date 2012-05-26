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
package org.readlogsfirst.api.logreader.logsplitter;

/**
 * To be used to build a splitter
 * @author pyppo
 *
 */
public class SplitterBuilder {
	private LineSplittingStrategy splitter;
	
	public SplitterBuilder withSingleLineSplitter(){
		splitter = new SingleLineSplittingStrategy();
		return this;
	}
	
	public SplitterBuilder multiLineWithPrefix(String start, boolean notab){
		splitter = new MultiLineLimitsSplitter(start, notab);
		return this;
	}
	
	public SplitterBuilder multiLineWithPrefix(String start, String end, boolean notab, boolean nothingAfter){
		splitter = new MultiLineLimitsSplitter(start, end, notab, nothingAfter);
		return this;
	}
	
	public LineSplittingStrategy build() {
		return splitter;
	}
}
