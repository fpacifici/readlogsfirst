/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.logreader;

/**
 * Represents a line in a log file.
 * The role of implementations is to store the content of a log line. Parsing
 * logic is out of this scope, in that it is part of LogDescriptor.
 * 
 * @author pyppo
 */
public interface LogLine<T extends Comparable> {
    public Comparable<T> getIndex();
}
