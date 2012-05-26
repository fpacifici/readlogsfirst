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

import java.io.InputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.readlogsfirst.api.descriptors.LogDescriptor;

/**
 *
 * @author pyppo
 */
class ConfigurationParser {
    private static final String LOG_DESCRIPTORS_TAG = "logDescriptors";
    private static final String LOG_DESCRIPTOR_TAG = "logDescriptor";

    XMLStreamReader reader;
    
    private boolean readyForElement = false;
    
    /**
     * Instantiates the reader 
     * @param xmlStream
     * @throws XMLStreamException 
     */
    public ConfigurationParser(InputStream xmlStream) throws XMLStreamException{
        if (xmlStream == null) {
            throw new IllegalArgumentException("Cannot parse a null stream");
        }
        XMLInputFactory factory = XMLInputFactory.newFactory();
        reader = factory.createXMLStreamReader(xmlStream);
        boolean foundStart = false;
        while(reader.hasNext() && !foundStart){
            int event = reader.nextTag();
            String name = reader.getLocalName();
            if (LOG_DESCRIPTORS_TAG.equals(name)){
                foundStart = true;
                if(goToElement()){
                    readyForElement = true;
                }
            }
        }
        
    }
    
    /**
     * 
     * @return the following element asking for parsing to DescriptorParser
     */
    public ConfigurationEntry getNextEntry() throws XMLStreamException{
        if (!readyForElement){
            boolean nextPresent = goToElement();
            if (!nextPresent){
                throw new XMLStreamException("No more elements in the configuration");
            }
        }
        String descName = reader.getAttributeValue(null, "name");
        DescriptorParser p = new DescriptorParser(reader);
        LogDescriptor desc = p.parseDescriptor();
        Class c = p.getLogItemClass();
        
        ConfigurationEntry entity = new ConfigurationEntry(descName,c, desc);
        readyForElement = false;
        return entity;
    }
    
    /**
     * 
     * @return true if a new logDescriptor element is present.
     * @throws XMLStreamException 
     */
    public boolean hasNext() throws XMLStreamException{
        if (readyForElement) {
            return true;
        }else {
            if(goToElement()) {
                readyForElement = true;
                return true;
            }else {
                return false;
            }
            
        }
    }
    
    /**
     * Gets to the following descriptor start element
     * @return
     * @throws XMLStreamException 
     */
    private boolean goToElement() throws XMLStreamException{
        String element = null;
        
        while(reader.hasNext() && !(LOG_DESCRIPTOR_TAG.equals(element))){
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT){
                element = reader.getLocalName();
                if (LOG_DESCRIPTOR_TAG.equals(element)){
                    return true;
                }
            }
        }
        return false;
    }
}
