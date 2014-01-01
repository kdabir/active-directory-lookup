package io.github.kdabir.adl.api;

import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

/**
 * A stateless, thread-safe service to perform ldap operations on Active
 * Directory. 
 * <p>
 * Logically it is singleton, but it is allowed to create {@code new} using 
 * default constructor for ease of integration with containers like spring 
 * without requiring any special configuration.
 * <p>
 * Ideally user will never need to create instance of this class. Depending on
 * use-case, wrapper classes like {@code ActiveDirectoryAuthenticator} or 
 * {@code ActiveDirectorySearcher} should be used.
 * 
 * 
 * @author kunal dabir
 */
public class ActiveDirectoryLdapService {

    private final ActiveDirectoryBinder activeDirectoryBinder = new ActiveDirectoryBinder(new ActiveDirectoryEnvironmentBuilder());

    public ActiveDirectoryLdapService() {
    }
    
    /**
     * Method tries to get LdapContext by connecting to the Active Directory.
     * 
     * 
     * @param url
     * @param domain
     * @param username
     * @param password
     * @return LdapContext
     * @throws NamingException - if bind is unsuccessful
     */
    public LdapContext getLdapContext(String url, String domain, String username, String password)
            throws NamingException {

        return activeDirectoryBinder.getLdapContext(url, domain, username, password);
    }

    /**
     * 
     * Searches for user in the Active directory.
     * 
     * @param ldapContext
     * @param searchBase
     * @param searchUsername
     * @return Map populated with user details, null if not found 
     * @throws NamingException
     */
    public Map<String, String> search(LdapContext ldapContext, String searchBase, String searchUsername) 
            throws NamingException {
        if (ldapContext == null) {
            throw new IllegalArgumentException("ldapContext can not be null");
        }

        Map<String, String> result = null;
        // sAMAccountName is specific to AD
        String searchFilter = "(&(objectClass=user)(sAMAccountName=" + searchUsername + "))";

        //Create the search controls
        SearchControls searchControls = new SearchControls();

        // setting only specific attributes to be returned
        //String returnedAtts[] = {"sn", "givenName", "mail", "telephoneNumber"};
        //searchCtls.setReturningAttributes(returnedAtts);

        //Specify the search scope
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        
        NamingEnumeration answer = ldapContext.search(searchBase, searchFilter, searchControls);
        if (answer.hasMoreElements()) { // no need to loop
            SearchResult sr = (SearchResult) answer.next();
            result = new HashMap<String, String>();
            Attributes attrs = sr.getAttributes();
            if (attrs != null) {
                NamingEnumeration ne = attrs.getAll();
                while (ne.hasMore()) {
                    Attribute attribute = (Attribute) ne.next();
                    result.put(attribute.getID(), attribute.get().toString());
                }
                ne.close();
            }
        }
        return result;
    }
}