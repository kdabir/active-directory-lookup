package io.github.kdabir.adl.api;

import java.util.Map;
import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

import io.github.kdabir.adl.exceptions.NotFoundException;
import io.github.kdabir.adl.util.ActiveDirectoryConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 
 * 
 * 
 * @author Kunal Dabir
 */
public class ActiveDirectorySearchIntgTest {

    static ActiveDirectoryConfig config;

    @BeforeClass
    public static void setUpClass() throws Exception {
        config = new ActiveDirectoryConfig(); // declared here as it throws exception
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // clean up so that net result of running this test is zero
    }

    @Before
    public void setUp() {
        // set up anything thats needed for every test case
    }

    @After
    public void tearDown() {
        // clean up after test case is run
    }


    @Test
    public void testSearch_happyPath() throws NamingException, NotFoundException{

        LdapContext ldapContext = ActiveDirectoryAutheticator.getDefaultActiveDirectoryBinder().getLdapContext(config.getUrl(), config.getDomain(), config.getUsername(), config.getPassword());
        String searchBase = config.getSearchBase();
        String searchUsername = config.getUsername();

        Map result = new ActiveDirectorySearcher(ldapContext, searchBase).search(searchUsername);
        assertNotNull(result);
    }

    @Test(expected=IllegalStateException.class)
    public void testSearch_badLdapContext() throws NamingException, NotFoundException {
        String searchBase = config.getSearchBase();
        String searchUsername = config.getUsername();
        new ActiveDirectorySearcher(null, searchBase).search(searchUsername);
    }

    @Test(expected = NotFoundException.class)
    public void testSearch_badSearchBase() throws NamingException, NotFoundException {
        LdapContext ldapContext = ActiveDirectoryAutheticator.getDefaultActiveDirectoryBinder().getLdapContext(config.getUrl(), config.getDomain(), config.getUsername(), config.getPassword());
        String searchBase = "dc=fake";
        String searchUsername = config.getUsername();
        new ActiveDirectorySearcher(ldapContext, searchBase).search(searchUsername);
    }

    @Test(expected = NotFoundException.class)
    public void testSearch_badUsername() throws NamingException,NotFoundException  {
        LdapContext ldapContext = ActiveDirectoryAutheticator.getDefaultActiveDirectoryBinder().getLdapContext(config.getUrl(), config.getDomain(), config.getUsername(), config.getPassword());
        String searchBase = config.getSearchBase();
        String searchUsername = "faker";
        new ActiveDirectorySearcher(ldapContext, searchBase).search(searchUsername);
    }
}
