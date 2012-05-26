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

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * test a tokenizer based on a series of regular expressions
 * @author pyppo
 *
 */
public class RegExpTokenizerTest {

	/**
	 * Tests the reg exp based tokenizer
	 * @throws Exception
	 */
	@Test
	public void testSystemLine() throws Exception {
		String line = "Mar 30 00:27:13 OddId kernel[0]: vmnet: VNetUserIfFree: freeing userIf at 0xb7e9d00.";
		List<String> regexps = new LinkedList<String>();
		regexps.add("\\w\\w\\w\\s[\\d\\s]\\d\\s\\d\\d.\\d\\d.\\d\\d\\s");
		regexps.add("\\S+\\s");
		regexps.add("\\S+\\s");
		regexps.add("\\S+\\s");
		regexps.add("[\\S\\s]+\\z");

		RegExpTokenizer tokenizer = new RegExpTokenizer(regexps);
		
		List<?> tokens = tokenizer.tokenize(line);
		
		assertEquals(5, tokens.size());
		assertEquals("Mar 30 00:27:13 ", tokens.get(0));
		assertEquals("OddId ", tokens.get(1));
		assertEquals("kernel[0]: ", tokens.get(2));
		assertEquals("vmnet: ", tokens.get(3));
		assertEquals("VNetUserIfFree: freeing userIf at 0xb7e9d00.", tokens.get(4));
	}
}
