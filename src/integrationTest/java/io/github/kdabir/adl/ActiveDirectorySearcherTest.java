package io.github.kdabir.adl;

import io.github.kdabir.adl.ActiveDirectoryConfig;
import io.github.kdabir.adl.ActiveDirectorySearcher;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author kdabir
 */
public class ActiveDirectorySearcherTest {
    
    static ActiveDirectorySearcher searcher;
    static ActiveDirectoryConfig config;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        config = new ActiveDirectoryConfig();
        searcher = new ActiveDirectorySearcher(config.getUrl(), config.getDomain(), config.getSearchBase(), config.getUsername(), config.getPassword(),config.getLookupAttrs());
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testSearch_String() throws Exception {
        TestCase.assertTrue(true);
    }

}
