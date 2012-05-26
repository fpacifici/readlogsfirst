/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.readlogsfirst.api.configuration;

import static org.junit.Assert.*;
import org.junit.Test;
/**
 * Tests the default load process for the configuration from META-INF
 * @author fpacifici
 */
public class TestDefaultConfigLoad {
    
    @Test
    public void testDefaultLoad() throws Exception {
        ConfigurationContainer container = ConfigurationContainer.getInstance();
        assertEquals(2, container.getContent().size());
    }
}
