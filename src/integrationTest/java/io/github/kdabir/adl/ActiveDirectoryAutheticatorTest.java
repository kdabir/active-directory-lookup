package io.github.kdabir.adl;

import io.github.kdabir.adl.exceptions.BadCredentialsException;
import io.github.kdabir.adl.exceptions.NotFoundException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kdabir
 */
public class ActiveDirectoryAutheticatorTest {

    static ActiveDirectoryConfig config;
    static ActiveDirectoryAutheticator autheticator;

    @BeforeClass
    public static void setUpClass() throws Exception {
        // set up anything thats needed to test this class
        System.out.println("Testing the ActiveDirectoryService ...");
        config = new ActiveDirectoryConfig(); // declared here as it throws exception
        autheticator = new ActiveDirectoryAutheticator(config.getDomain(), config.getUrl(), config.getSearchBase(),config.getLookupAttrs());
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // clean up so that net result of running this test is zero
    }

    @Test
    public void testAuthenticate_good() throws BadCredentialsException, NotFoundException {
        assertTrue(autheticator.authenticate(config.getUsername(), config.getPassword()).size()>1);
    }
    
    @Test (expected = BadCredentialsException.class)
    public void testAuthenticate_badPassword() throws BadCredentialsException , NotFoundException{
        autheticator.authenticate(config.getUsername(), "unreal");
    }
    
    @Test (expected = BadCredentialsException.class)
    public void testAuthenticate_badUsername() throws BadCredentialsException , NotFoundException{
        autheticator.authenticate("some0ne", config.getPassword());
    }   
    
    @Test (expected = NotFoundException.class)
    public void testAuthenticate_badSearchBase() throws BadCredentialsException , NotFoundException{
        // creating a autheticator with bad search base
        ActiveDirectoryAutheticator autheticatorWithBadSearchBase = new ActiveDirectoryAutheticator(
                config.getDomain(), config.getUrl(), config.getSearchBase()+",dc=someplasce",config.getLookupAttrs());
    
        autheticatorWithBadSearchBase.authenticate(config.getUsername(), config.getPassword());
    }    
    
    @Test
    public void testIsValid_good() {
        assertTrue(autheticator.isValid(config.getUsername(), config.getPassword()));
    }
    
    @Test
    public void testIsValid_badPassword() { 
        assertFalse(autheticator.isValid(config.getUsername(), "fake"));
    }
    
    @Test
    public void testIsValid_badUsername() { // ofcourse this password is of no use :P
        assertFalse(autheticator.isValid("n0nex1stent", config.getPassword()));
    }
}
