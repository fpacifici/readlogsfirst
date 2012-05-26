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

import org.readlogsfirst.api.descriptors.LogDescriptor;






    /**
     * Manages the configuration of one descriptor.
     */
    class ConfigurationEntry {
        private final Class expectedLineType;
        
        private final LogDescriptor descriptor;
        
        private final String name;

        public ConfigurationEntry(String name,Class expectedLineType, LogDescriptor descriptor) {
            this.expectedLineType = expectedLineType;
            this.descriptor = descriptor;
            this.name = name;
        }

        public LogDescriptor getDescriptor() {
            return descriptor;
        }

        public Class getExpectedLineType() {
            return expectedLineType;
        }

    public String getName() {
        return name;
    }
        
        
    
}
