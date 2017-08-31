package io.github.kdabir.adl.api;

import io.github.kdabir.adl.exceptions.BadCredentialsException;
import io.github.kdabir.adl.exceptions.NotFoundException;
import io.github.kdabir.adl.util.ActiveDirectoryConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kdabir
 */
public class ActiveDirectoryAuthenticatorTest {

    static ActiveDirectoryConfig config;
    static ActiveDirectoryAuthenticator authenticator;

    @BeforeClass
    public static void setUpClass() throws Exception {
        // set up anything thats needed to test this class
        config = new ActiveDirectoryConfig(); // declared here as it throws exception
        authenticator = new ActiveDirectoryAuthenticator(config.getDomain(), config.getUrl(), config.getSearchBase(),config.getLookupAttrs());
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // clean up so that net result of running this test is zero
    }

    @Test
    public void testAuthenticate_good() throws BadCredentialsException, NotFoundException {
        assertTrue(authenticator.authenticate(config.getUsername(), config.getPassword()).size()>1);
    }

    @Test (expected = BadCredentialsException.class)
    public void testAuthenticate_badPassword() throws BadCredentialsException , NotFoundException{
        authenticator.authenticate(config.getUsername(), "unreal");
    }

    @Test (expected = BadCredentialsException.class)
    public void testAuthenticate_badUsername() throws BadCredentialsException , NotFoundException{
        authenticator.authenticate("some0ne", config.getPassword());
    }

    @Test (expected = NotFoundException.class)
    public void testAuthenticate_badSearchBase() throws BadCredentialsException , NotFoundException{
        ActiveDirectoryAuthenticator authenticatorWithBadSearchBase = new ActiveDirectoryAuthenticator(
                config.getDomain(), config.getUrl(), config.getSearchBase()+",dc=someplace",config.getLookupAttrs());

        authenticatorWithBadSearchBase.authenticate(config.getUsername(), config.getPassword());
    }

    @Test
    public void testIsValid_good() {
        assertTrue(authenticator.isValid(config.getUsername(), config.getPassword()));
    }

    @Test
    public void testIsValid_badPassword() {
        assertFalse(authenticator.isValid(config.getUsername(), "fake"));
    }

    @Test
    public void testIsValid_badUsername() { // ofcourse this password is of no use :P
        assertFalse(authenticator.isValid("n0nex1stent", config.getPassword()));
    }
}
