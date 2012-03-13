package org.adl;

import java.util.List;
import java.util.Map;
import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

/**
 *
 * <p>This class provides simple API to authenticate users against the Active Directory.</p>
 * <p>{@code boolean isValid(String username, String password) }</p>
 * 
 * <p>{@code Map<String, String> authenticate(String username, String password) }</p>
 * 
 * @author Kunal Dabir
 */
public class ActiveDirectoryAutheticator {

    private String domain;              // e.g test.com
    private String url;                 // ldap://somehost.test.com or ldap://someotherhost.com
    private String searchBase;          // dc=test,dc=com
    private List<String> returnedAttrs = null;
    protected ActiveDirectoryLdapService activeDirectoryService = null;

    public ActiveDirectoryAutheticator(String domain, String url) {
        this(domain, url, "", null);
        //TODO derive the default search base from domain
        String derivedSeachBase = ""; 
        this.searchBase = derivedSeachBase; 
    }
    
    public ActiveDirectoryAutheticator(String domain, String url, String searchBase) {
        this(domain, url, searchBase, null);
    }

    public ActiveDirectoryAutheticator(String domain, String url, String searchBase, List<String> returnedAttrs) {
        this.domain = domain;
        this.url = url;
        this.searchBase = searchBase;
        this.returnedAttrs = returnedAttrs;
        this.activeDirectoryService = new ActiveDirectoryLdapService(); // initialize the core service
    }

    /**
     * <p>
     * Tries to bind with Active Directory using the supplied username/password.
     * If bind is successful, it searches for the user details in the AD using 
     * the same credentials and returns a {@code Map<String,String>} populated 
     * with user details.
     * </p>
     * 
     * Throws {@code ActiveDirectoryException} (RuntimeException) if something goes wrong
     *
     * @param username
     * @param password
     * @return a <code>Map</code> populated with user details
     * @throws BadCredentialsException when username password do not match
     * @throws NotFoundException if user auth succeeds but user info can not be found in AD. 
     */
    public Map<String, String> authenticate(String username, String password)
            throws BadCredentialsException, NotFoundException {
        Map<String, String> result;
        try {
            LdapContext ldapContext = activeDirectoryService.getLdapContext(url, domain, username, password);
            result = activeDirectoryService.search(ldapContext, searchBase, username);

            if (result == null) {
                throw new NotFoundException("Username password matched, "
                        + "but user details could not be found in the given search base. "
                        + "Either search base is incorrect or user does not have privileges to search");
            }

            if (returnedAttrs != null && returnedAttrs.size() > 0) {
                result.keySet().retainAll(returnedAttrs);
            }

        } catch (AuthenticationException ex) {
            throw new BadCredentialsException();
        } catch (CommunicationException ex) {
            throw new ActiveDirectoryException(ex);
        } catch (NamingException ex) {
            throw new ActiveDirectoryException(ex);
        }
        return result;
    }

    /**
     * Just checks the username/password by doing a bind. This method does not 
     * throw any checked exception. 
     * 
     * throws ActiveDirectoryException (RuntimeException) if something goes wrong
     * 
     * @param username
     * @param password
     * @return true if username/password match otherwise false
     */
    public boolean isValid(String username, String password) {
        try {
            activeDirectoryService.getLdapContext(url, domain, username, password);
            return true;
        } catch (AuthenticationException ex) {
            return false;
        } catch (CommunicationException ex) {
            throw new ActiveDirectoryException(ex);
        } catch (NamingException ex) {
            throw new ActiveDirectoryException(ex);
        }
    }
}
