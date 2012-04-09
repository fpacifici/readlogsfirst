/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.descriptors;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * File group identifier based on regular expression.
 * Object of this class are used to identify a group of log files 
 * that have a common base directory and which name respect a regular expression.
 * 
 * 
 * @author fpacifici
 */
public class RegExpFileFilter implements LogFileGroup{

    private final File baseDirectory;
    
    private final boolean recursive;
    
    private final Pattern filter;
    
    public RegExpFileFilter(File baseDirectory){
        this(baseDirectory, true);
    }
    
    public RegExpFileFilter(File baseDirectory, boolean recursive){
        this(baseDirectory,recursive,null);
    }
    
    public RegExpFileFilter(File baseDirectory, boolean recursive, Pattern regexp){
        if (baseDirectory == null || !baseDirectory.isDirectory()){
            throw new IllegalArgumentException("Base directory cannot be null and must be a directory");
        }
        this.baseDirectory = baseDirectory;
        this.recursive = recursive;
        this.filter = regexp;
    }
    
    /**
     * @return the list of files satisfying the regular expression and 
     * starting at baseDirectory.
     */
    public List<File> getFiles() {
        List<File> ret = new LinkedList<File>();
        if (!recursive){
            for (File f : getDirectoryContent(baseDirectory)){
                if (! f.isDirectory()){
                    ret.add(f);
                }
            }
            return ret;
        }else {
            return getRecursiveFiles(baseDirectory);
        }
    }
    
    private List<File> getRecursiveFiles(File base){
        List<File> tempList = new LinkedList<File>();
        File[] currents = getDirectoryContent(base);
        for (File f : currents){
            if (f.isDirectory()){
                tempList.addAll(getRecursiveFiles(f));
            }else {
                tempList.add(f);
            }
        }
        return tempList;
    }
    
    private File[] getDirectoryContent(File directory){
        File[] ret = null;
        if (filter == null){
            ret = directory.listFiles();
        }else{
            ret = directory.listFiles(new FileFilter(filter));
        }
        return ret;
    }
    
    /**
     * matches files from a list given a regexp,
     */
    private class FileFilter implements FilenameFilter{

        private final Pattern regexp;
        
        public FileFilter(Pattern regexp) {
            if (regexp == null || baseDirectory == null){
                throw new IllegalArgumentException("Cannot initialize FileFilter without regexp and baseDirectory");
            }
            this.regexp = regexp;
        }
        
        public boolean accept(File file, String filename) {
            Matcher m = regexp.matcher(filename);
            File completeName = new File(file.getPath() + File.separatorChar + filename);
            return m.matches() || completeName.isDirectory();
        }       
    }
}
