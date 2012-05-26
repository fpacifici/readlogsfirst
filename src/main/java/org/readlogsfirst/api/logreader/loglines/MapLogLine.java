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
package org.readlogsfirst.api.logreader.loglines;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.readlogsfirst.api.logreader.LogLine;

/**
 * This is a generic structured logline providing the elements as a map of any kind of object.
 * Object are retained as a unmodifiable map.
 * 
 * @author fpacifici
 *
 * @param <T>
 */
public class MapLogLine<T extends Comparable<T>> implements Map<String, Object>, LogLine<T> {

	private final Map<String, Object> underlyingMap;
	
	private final T index;
	
	/**
	 * Initialize this line passing the content as a map.
	 * I am not making a copy of the map, this is supposed to be instantiated only by the parser
	 * which is supposed to know what is doing thus not passing anything that may be modified.
	 * 
	 * Makes a check on the right type of index and that it is not null.
	 * 
	 * @param content the map with the content 
	 * @param indexKey
	 */
	public MapLogLine(Map<String,Object> content, T index) {
		if (content == null || index == null || content.size() == 0)
			throw new IllegalArgumentException("Cannot initialize line without content or index");
		this.index = index;
		underlyingMap = content;
		//I am fine to have a ClassCast thrown in case the content is not good.
		//this is supposed to be managed by the parser.
	}
	
	/**
	 * returns the index. I have already checked it is not null and of the correct type.
	 */
	public Comparable<T> getIndex() {
		
		return index;
	}

	/**
	 * No modificaitons.
	 */
	public void clear() {
		throw new UnsupportedOperationException("This is a log line. It is too late to modify it");
	}

	public boolean containsKey(Object arg0) {
		return underlyingMap.containsKey(arg0);
	}

	public boolean containsValue(Object arg0) {
		return underlyingMap.containsValue(arg0);
	}

	/**
	 * @return an unmodifieable version of the entryset.
	 */
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		Set<Map.Entry<String, Object>> entrySet = underlyingMap.entrySet();
		return Collections.unmodifiableSet(entrySet);
	}

	public Object get(Object arg0) {
		return underlyingMap.get(arg0);
	}

	public boolean isEmpty() {
		return false;
	}

	/**
	 * @return an unmodifiable version of the keySet.
	 */
	public Set<String> keySet() {
		Set<String> keySet = underlyingMap.keySet();
		return Collections.unmodifiableSet(keySet);
	}

	public Object put(String arg0, Object arg1) {
		throw new UnsupportedOperationException("This is a log line. It is too late to modify it");
	}

	public void putAll(Map<? extends String, ? extends Object> arg0) {
		throw new UnsupportedOperationException("This is a log line. It is too late to modify it");
	}

	public Object remove(Object arg0) {
		throw new UnsupportedOperationException("This is a log line. It is too late to modify it");
	}

	public int size() {
		return underlyingMap.size();
	}

	/**
	 * @return the unmodifiable version of values collection.
	 */
	public Collection<Object> values() {
		Collection<Object> vals = underlyingMap.values();
		return Collections.unmodifiableCollection(vals);
	}

}
