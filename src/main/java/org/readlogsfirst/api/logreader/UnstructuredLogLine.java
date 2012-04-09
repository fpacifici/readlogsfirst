/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.logreader;

/**
 * Represents an instance of a log line. This line is considered as a simple
 * unstructured string with a String index.
 * 
 * @author fpacifici
 */
public class UnstructuredLogLine<T extends Comparable> implements LogLine{
   private String line;
   
   private T index;

    public UnstructuredLogLine(String line, T index) {
        this.line = line;
        this.index = index;
    }

    public T getIndex() {
        return index;
    }

    public String getLine() {
        return line;
    }
   
   public String toString(){
       return (line);
   }
}
