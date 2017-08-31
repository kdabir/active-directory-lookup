package io.github.kdabir.adl.api;

import io.github.kdabir.adl.api.filters.UsernameFilter;
import io.github.kdabir.adl.exceptions.BadCredentialsException;
import io.github.kdabir.adl.exceptions.NotFoundException;
import io.github.kdabir.adl.util.ActiveDirectoryEnvironmentProvider;
import io.github.kdabir.adl.util.SearchBaseGuesser;

import javax.naming.ldap.LdapContext;
import java.util.List;
import java.util.Map;

/**
 * <p>This class provides simple API to authenticate users against the Active Directory.</p>
 * <p>{@code boolean isValid(String username, String password) }</p>
 *
 * <p>{@code Map<String, String> authenticate(String username, String password) }</p>
 *
 * @author Kunal Dabir
 */
public class ActiveDirectoryAuthenticator {

    private String domain;              // e.g test.com
    private String url;                 // ldap://somehost.test.com or ldap://someotherhost.com
    private String searchBase;          // dc=test,dc=com
    private List<String> returnedAttrs = null;

    public ActiveDirectoryAuthenticator(String domain, String url) {
        this(domain, url, new SearchBaseGuesser().guessFrom(domain), null);
    }

    public ActiveDirectoryAuthenticator(String domain, String url, String searchBase) {
        this(domain, url, searchBase, null);
    }

    public ActiveDirectoryAuthenticator(String domain, String url, String searchBase, List<String> returnedAttrs) {
        this.domain = domain;
        this.url = url;
        this.searchBase = searchBase;
        this.returnedAttrs = returnedAttrs;
    }

    public static ActiveDirectoryBinder getDefaultActiveDirectoryBinder() {
        return new ActiveDirectoryBinder(new ActiveDirectoryEnvironmentProvider(), new LdapContextFactory());
    }

    /**
     * <p>
     * Tries to bind with Active Directory using the supplied username/password.
     * If bind is successful, it searches for the user details in the AD using
     * the same credentials and returns a {@code Map<String,String>} populated
     * with user details.
     * </p>
     *
     * <p>Throws {@code ActiveDirectoryException} (RuntimeException) if something goes wrong</p>
     *
     * @param username
     * @param password
     * @return a <code>Map</code> populated with user details
     * @throws io.github.kdabir.adl.exceptions.BadCredentialsException when username password do not match
     * @throws io.github.kdabir.adl.exceptions.NotFoundException       if user auth succeeds but user info can not be found in AD.
     */
    public Map<String, String> authenticate(String username, String password)
            throws BadCredentialsException, NotFoundException {

        LdapContext ldapContext = getDefaultActiveDirectoryBinder().getLdapContext(url, domain, username, password);
        final List<Map<String, String>> result = new SimpleActiveDirectorySearcher(ldapContext, searchBase)
                .withReturnedAttrs(returnedAttrs)
                .search(new UsernameFilter(username));

        if (result.size() < 1) {
            throw new NotFoundException("Username password matched, "
                    + "but user details could not be found in the given search base. "
                    + "Either search base is incorrect or user does not have privileges to search");
        }

        return result.get(0);
    }

    /**
     * Just checks the username/password by doing a bind. This method does not
     * throw any checked exception.
     *
     * <p> Throws ActiveDirectoryException (RuntimeException) if something goes wrong </p>
     *
     * @param username
     * @param password
     * @return true if username/password match otherwise false
     */
    public boolean isValid(String username, String password) {
        try {
            getDefaultActiveDirectoryBinder().getLdapContext(url, domain, username, password);
            return true;
        } catch (BadCredentialsException ex) {
            return false;
        }
    }
}
