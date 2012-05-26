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
package org.readlogsfirst.api.logreader.logitemparser.tokenizers;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;
import org.readlogsfirst.api.logreader.logitemparser.LineTokenizerStrategy;

/**
 * Tests the simple splits tokenizer
 * @author fpacifici
 *
 */
public class SimpleTokenizerTest {

	/**
	 * Tests several possible cases of splitter based tokenizer.
	 * @throws Exception
	 */
	@Test
	public void testSplitterBasedTokenizer() throws Exception {
		//split regular case
		LineTokenizerStrategy tokenizer = new SimpleSplitTokenizer(";;");
		
		String regularLine = "abc;;def;;ghi;;xyz";
		List<?> tokens = tokenizer.tokenize(regularLine);
		
		assertEquals(4,tokens.size());
		assertEquals("abc", tokens.get(0));
		assertEquals("def", tokens.get(1));
		assertEquals("ghi", tokens.get(2));
		assertEquals("xyz", tokens.get(3));
		
		//split case with empty fields
		regularLine = "abc;;;;;;xyz";
		tokens = tokenizer.tokenize(regularLine);
		
		assertEquals(4,tokens.size());
		assertEquals("abc", tokens.get(0));
		assertEquals("", tokens.get(1));
		assertEquals("", tokens.get(2));
		assertEquals("xyz", tokens.get(3));
		
		//completely empty line
		regularLine = ";;;;;;";
		tokens = tokenizer.tokenize(regularLine);
		
		assertEquals(4,tokens.size());
		assertEquals("", tokens.get(0));
		assertEquals("", tokens.get(1));
		assertEquals("", tokens.get(2));
		assertEquals("", tokens.get(3));
	}
	
	/**
	 * Tests line based on delimiters
	 * @throws Exception
	 */
	@Test
	public void testSplitterDelimited() throws Exception {
		//split regular case
		LineTokenizerStrategy tokenizer = new SimpleSplitTokenizer("<",">");
		
		String regularLine = "<abc><def><ghi><xyz>";
		
		List<?> tokens = tokenizer.tokenize(regularLine);
		
		assertEquals(4,tokens.size());
		assertEquals("abc", tokens.get(0));
		assertEquals("def", tokens.get(1));
		assertEquals("ghi", tokens.get(2));
		assertEquals("xyz", tokens.get(3));
	
		//add junk in the middle
		regularLine = "<abc>   <def>XXXX<ghi> >>>><xyz>   ";
		tokens = tokenizer.tokenize(regularLine);
		
		assertEquals(4,tokens.size());
		assertEquals("abc", tokens.get(0));
		assertEquals("def", tokens.get(1));
		assertEquals("ghi", tokens.get(2));
		assertEquals("xyz", tokens.get(3));
		
		//empty fields
		
		regularLine = "<><><><>";
		tokens = tokenizer.tokenize(regularLine);
		
		assertEquals(4,tokens.size());
		assertEquals("", tokens.get(0));
		assertEquals("", tokens.get(1));
		assertEquals("", tokens.get(2));
		assertEquals("", tokens.get(3));
		
	}
}
