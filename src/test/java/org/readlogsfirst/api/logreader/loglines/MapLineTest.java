package org.readlogsfirst.api.logreader.loglines;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Simple test of the map based structured line
 * @author pyppo
 *
 */
public class MapLineTest {

	
	/**
	 * Tests initialization.
	 */
	@Test
	public void testInit(){
		Map<String,Object> content = new HashMap<String, Object>();
		Date d = new Date(System.currentTimeMillis());
		content.put("tstamp",d);
		content.put("content", "BLABLA");
		
		MapLogLine<Date> line = new MapLogLine<Date>(content,d);
		
		assertEquals(d, line.getIndex());
		assertEquals("BLABLA", line.get("content"));
	}
}
