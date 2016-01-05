package io.github.kdabir.adl.api;

import io.github.kdabir.adl.util.ActiveDirectoryConfig;
import io.github.kdabir.adl.util.ActiveDirectoryEnvironmentProvider;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

import java.util.Hashtable;

import static org.junit.Assert.assertNotNull;

public class LdapContextFactoryTest {

    static ActiveDirectoryConfig config;
    LdapContextFactory ldapContextFactory = new LdapContextFactory();
    ActiveDirectoryEnvironmentProvider environmentProvider = new ActiveDirectoryEnvironmentProvider();

    @BeforeClass
    public static void setUpClass() throws Exception {
        config = new ActiveDirectoryConfig(); // declared here as it throws exception
    }

    @Test
    public void testGetLdapContext_happyPath() throws NamingException {
        LdapContext result = ldapContextFactory.getLdapContext(environmentProvider.getActiveDirectoryEnvironment(
                config.getUrl(), config.getDomain(),
                config.getUsername(), config.getPassword()));
        assertNotNull(result);
    }

    @Test(expected = javax.naming.CommunicationException.class)
    public void testGetLdapContext_badUrl() throws NamingException {
        final Hashtable<String,String> env = environmentProvider.getActiveDirectoryEnvironment(
                "ldap://fakeldapserver.com", config.getDomain(), // hope this never becomes a valid ldap server url :)
                config.getUsername(), config.getPassword());
        // env.put("com.sun.jndi.ldap.connect.timeout", "3000"); // so that test doesn't keep on waiting for long
        ldapContextFactory.getLdapContext(env);
    }

    @Test(expected = javax.naming.AuthenticationException.class)
    public void testGetLdapContext_badDomain() throws NamingException {
        ldapContextFactory.getLdapContext(environmentProvider.getActiveDirectoryEnvironment(
                config.getUrl(), "dc=fake",
                config.getUsername(), config.getPassword()));
    }

    @Test(expected = javax.naming.AuthenticationException.class)
    public void testGetLdapContext_badUsername() throws NamingException {
        ldapContextFactory.getLdapContext(environmentProvider.getActiveDirectoryEnvironment(
                config.getUrl(), config.getDomain(),
                "fake", config.getPassword()));
    }

    @Test(expected = javax.naming.AuthenticationException.class)
    public void testGetLdapContext_badPassword() throws NamingException {
        ldapContextFactory.getLdapContext(environmentProvider.getActiveDirectoryEnvironment(
                config.getUrl(), config.getDomain(),
                config.getUsername(), "fake"));
    }

}
