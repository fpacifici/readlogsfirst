/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.logreader.basereader;

import java.io.BufferedReader;
import java.io.File;

/**
 *
 * @author pyppo
 */
/**
 * Keeps track of an open file.
 */
class FileEntry implements Comparable<FileEntry> {

    Comparable startIndex;
    java.io.File file;

    public FileEntry(Comparable startIndex, File file) {
        this.startIndex = startIndex;
        this.file = file;
    }

    public int compareTo(FileEntry t) {
        return startIndex.compareTo(t.startIndex);
    }
}
