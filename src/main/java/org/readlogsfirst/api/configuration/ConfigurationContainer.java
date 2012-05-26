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
package org.readlogsfirst.api.configuration;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.media.jai.operator.LogDescriptor;
import javax.xml.stream.XMLStreamException;

/**
 * Represents the configuration repository as a singleton. It allows to populate
 * it by parsing XML files or by adding manually objects to the content.
 *
 * The set of files is controlled by the user of this class.
 *
 * @author fpacifici
 */
public class ConfigurationContainer {

    private Map<String, ConfigurationEntry> content;
    private static ConfigurationContainer singleton;

    /**
     * Returns singleton instance
     *
     * @return
     */
    public static synchronized ConfigurationContainer getInstance() throws XMLStreamException{
        if (singleton == null) {
            singleton = new ConfigurationContainer();
        }
        return singleton;
    }

    /**
     * Private constructor t oavoid external initializations.
     */
    private ConfigurationContainer() throws XMLStreamException{
        content = new HashMap<String, ConfigurationEntry>();
        initializeFromMetaInf();
    }

    /**
     *
     * @return the content as an immutable set
     */
    public Set<Map.Entry<String, ConfigurationEntry>> getContent() {
        return java.util.Collections.unmodifiableSet(content.entrySet());
    }

    /**
     * Adds elements by parsing an XML file.
     *
     * @param file
     * @throws XMLStreamException
     */
    public void parseConfigFile(InputStream file) throws XMLStreamException {

        ConfigurationParser parser = new ConfigurationParser(file);
        while (parser.hasNext()) {
            ConfigurationEntry e = parser.getNextEntry();
            content.put(e.getName(), e);
        }

    }
    
    private void initializeFromMetaInf() throws XMLStreamException {
        URL url = this.getClass().getClassLoader().getResource("/META-INF/descriptors");
        File baseDir = new File(url.getFile());
        File[] filesToConfigure = baseDir.listFiles(new FileFilter());
        for (File f : filesToConfigure){
            try{
                FileInputStream is = new FileInputStream(f);
                parseConfigFile(is);
            }catch(IOException e){
                throw new XMLStreamException("Error opening " + f.getName(),e);
            }
        }
    }

    /**
     * Adds an external element to the configuration
     *
     * @param name
     * @param entry
     */
    public void addConfigEntry(String name, ConfigurationEntry entry) {
        content.put(name, entry);
    }

    /**
     * Returns a specific entry present in the configuration
     *
     * @param name
     * @return
     */
    public ConfigurationEntry getConfigEntry(String name) {
        return content.get(name);
    }
    
    /**
     * matches XML files
     */
    private class FileFilter implements FilenameFilter{

        public boolean accept(File file, String filename) {
            return filename.endsWith(".xml");
        }       
    }
}
