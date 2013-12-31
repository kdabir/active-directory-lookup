package io.github.kdabir.adl.api;

import java.util.Map;
import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

import io.github.kdabir.adl.api.ActiveDirectoryLdapService;
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
public class ActiveDirectoryServiceTest {

    static ActiveDirectoryConfig config;
    ActiveDirectoryLdapService activeDirectoryService = new ActiveDirectoryLdapService();

    @BeforeClass
    public static void setUpClass() throws Exception {
        // set up anything thats needed to test this class
        System.out.println("Testing the ActiveDirectoryService ...");
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
    public void testGetLdapContext_happyPath() throws NamingException {
        LdapContext result = activeDirectoryService.getLdapContext(
                config.getUrl(), config.getDomain(),
                config.getUsername(), config.getPassword());
        assertNotNull(result);
    }

    // javax.naming.CommunicationException --  when url is unreachable
    @Test(expected = javax.naming.CommunicationException.class)
    public void testGetLdapContext_badUrl() throws NamingException {
        LdapContext result = activeDirectoryService.getLdapContext(
                "ldap://fakeurl.com", config.getDomain(),
                config.getUsername(), config.getPassword());
        assertNull(result);
    }

    @Test(expected = javax.naming.AuthenticationException.class)
    public void testGetLdapContext_badDomain() throws NamingException {
        LdapContext result = activeDirectoryService.getLdapContext(
                config.getUrl(), "dc=fake",
                config.getUsername(), config.getPassword());
        assertNull(result);
    }

    @Test(expected = javax.naming.AuthenticationException.class)
    public void testGetLdapContext_badUsername() throws NamingException {
        LdapContext result = activeDirectoryService.getLdapContext(
                config.getUrl(), config.getDomain(),
                "fake", config.getPassword());
        assertNull(result);
    }

    @Test(expected = javax.naming.AuthenticationException.class)
    public void testGetLdapContext_badPassword() throws NamingException {
        LdapContext result = activeDirectoryService.getLdapContext(
                config.getUrl(), config.getDomain(),
                config.getUsername(), "fake");
        assertNull(result);
    }

    @Test
    public void testSearch_happyPath() throws NamingException {
        LdapContext ldapContext = activeDirectoryService.getLdapContext(
                config.getUrl(), config.getDomain(), 
                config.getUsername(), config.getPassword());
        String searchBase = config.getSearchBase();
        String searchUsername = config.getUsername();

        Map result = activeDirectoryService.search(ldapContext, searchBase, searchUsername);
        assertNotNull(result);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSearch_badLdapContext() throws NamingException {
        String searchBase = config.getSearchBase();
        String searchUsername = config.getUsername();
        Map result = activeDirectoryService.search(null, searchBase, searchUsername);
        assertNull(result);
    }

    @Test
    public void testSearch_badSearchBase() throws NamingException {
        LdapContext ldapContext = activeDirectoryService.getLdapContext(
                config.getUrl(), config.getDomain(), 
                config.getUsername(), config.getPassword());
        String searchBase = "dc=fake";
        String searchUsername = config.getUsername();
        Map result = activeDirectoryService.search(ldapContext, searchBase, searchUsername);
        assertNull(result);
    }

    @Test
    public void testSearch_badUsername() throws NamingException {
        LdapContext ldapContext = activeDirectoryService.getLdapContext(
                config.getUrl(), config.getDomain(), 
                config.getUsername(), config.getPassword());
        String searchBase = config.getSearchBase();
        String searchUsername = "faker";
        Map result = activeDirectoryService.search(ldapContext, searchBase, searchUsername);
        assertNull(result);
    }
}
