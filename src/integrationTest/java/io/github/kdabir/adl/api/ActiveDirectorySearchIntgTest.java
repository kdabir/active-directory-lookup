package io.github.kdabir.adl.api;

import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

import io.github.kdabir.adl.api.filters.UsernameFilter;
import io.github.kdabir.adl.exceptions.NotFoundException;
import io.github.kdabir.adl.util.ActiveDirectoryConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Kunal Dabir
 */
public class ActiveDirectorySearchIntgTest {

    static ActiveDirectoryConfig config;

    @BeforeClass
    public static void setUpClass() throws Exception {
        config = new ActiveDirectoryConfig(); // declared here as it throws exception
    }

    @Test
    public void testSearch_happyPath() throws NamingException, NotFoundException {

        LdapContext ldapContext = ActiveDirectoryAutheticator.getDefaultActiveDirectoryBinder().getLdapContext(config.getUrl(), config.getDomain(), config.getUsername(), config.getPassword());
        String searchBase = config.getSearchBase();
        String searchUsername = config.getUsername();

        List result = new ActiveDirectorySearcher(ldapContext, searchBase).search(new UsernameFilter(searchUsername));
        assertTrue(result != null && result.size() > 0);
    }

    @Test
    public void testSearch_badSearchBase() throws NamingException, NotFoundException {
        LdapContext ldapContext = ActiveDirectoryAutheticator.getDefaultActiveDirectoryBinder().getLdapContext(config.getUrl(), config.getDomain(), config.getUsername(), config.getPassword());
        String searchBase = "dc=fake";
        String searchUsername = config.getUsername();
        final List<Map<String, String>> result = new ActiveDirectorySearcher(ldapContext, searchBase).search(new UsernameFilter(searchUsername));
        assertEquals(0, result.size());
    }

    @Test
    public void testSearch_badUsername() throws NamingException, NotFoundException {
        LdapContext ldapContext = ActiveDirectoryAutheticator.getDefaultActiveDirectoryBinder().getLdapContext(config.getUrl(), config.getDomain(), config.getUsername(), config.getPassword());
        String searchBase = config.getSearchBase();
        String searchUsername = "faker";
        final List<Map<String, String>> result = new ActiveDirectorySearcher(ldapContext, searchBase).search(new UsernameFilter(searchUsername));
        assertEquals(0, result.size());
    }
}
