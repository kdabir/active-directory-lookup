package io.github.kdabir.adl.api;

import io.github.kdabir.adl.exceptions.ActiveDirectoryException;
import io.github.kdabir.adl.exceptions.NotFoundException;

import java.util.List;
import java.util.Map;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

/**
 *
 * @author Kunal Dabir
 */
public class ActiveDirectorySearcher {

    private final SearchResultMapper searchResultMapper = new SearchResultMapper();
    /**
     * To support of multi-search mode, stores the context so that bind is not 
     * required every time
     */
    protected LdapContext ldapContext = null;

    /**
     * searches will be performed based on this search if search base is not provided in the call
     */
    protected String defaultSearchBase = null;  //eg dc=test,dc=com

    /**
     * only these attributes will be loaded
     */
    protected List<String> returnedAttrs = null;

    /**
     * Constructs the Searcher.  
     * 
     * Throws ActiveDirectoryException (RuntimeException) if something goes wrong
     * 
     * 
     * @param defaultSearchBase
     */
    public ActiveDirectorySearcher(LdapContext ldapContext, String defaultSearchBase) {
        this(ldapContext, defaultSearchBase, null);
    }    
    
    /**
     * Constructs the Searcher.  
     * 
     * Throws ActiveDirectoryException (RuntimeException) if something goes wrong
     * 
     * @param defaultSearchBase
     * @param returnedAttrs -- only these attributes will retained in the returned map (in search)
     * @param ldapContext
     */
    public ActiveDirectorySearcher(LdapContext ldapContext, String defaultSearchBase, List<String> returnedAttrs) {
        this.ldapContext = ldapContext;
        this.defaultSearchBase = defaultSearchBase;
        this.returnedAttrs = returnedAttrs;
    }


    /**
     * searches by user id (usually LAN id) in the active directory in default searchbase.
     * 
     * Throws ActiveDirectoryException (RuntimeException) if something goes wrong
     * 
     * @param username
     * @return
     * @throws io.github.kdabir.adl.exceptions.NotFoundException
     */
    public Map<String, String> search(String username) throws NotFoundException {
        return this.search(defaultSearchBase, username);
    }

    /**
     * Searches by user id (usually LAN id) in the active directory in given searchbase.
     * 
     * Throws ActiveDirectoryException (RuntimeException) if something goes wrong
     * 
     * @param searchBase
     * @param username
     * @return
     * @throws NotFoundException 
     */
    public Map<String, String> search(String searchBase, String username) throws NotFoundException {
        Map<String, String> result = null;
        // todo - can write a check and bind in case ldapContext gets closed
        try {
            if (ldapContext == null) {
                throw new IllegalStateException("ldapContext can not be null");
            }
            // sAMAccountName is specific to AD
            String searchFilter = "(&(objectClass=user)(sAMAccountName=" + username + "))";

            //Create the search controls
            SearchControls searchControls = new SearchControls();

            // setting only specific attributes to be returned
            //String returnedAtts[] = {"sn", "givenName", "mail", "telephoneNumber"};
            //searchCtls.setReturningAttributes(returnedAtts);

            //Specify the search scope
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<SearchResult> answer = ldapContext.search(searchBase, searchFilter, searchControls);

            final List<Map<String, String>> results = searchResultMapper.mapResult(answer);

            if (results.size() == 0 ){
                throw new NotFoundException( username + " not found");
            }
            result = results.get(0);
            answer.close();

            if (returnedAttrs != null && returnedAttrs.size() > 0) {
                result.keySet().retainAll(returnedAttrs);
            }
        } catch (NamingException ex) {
            throw new ActiveDirectoryException("Exception while searching Active Directory", ex);
        }
        return result;
    }


    /**
     * to clean up
     */
    public void close() {
        if (ldapContext != null) {
            try {
                ldapContext.close();
            } catch (NamingException ex) {
                throw new ActiveDirectoryException("Exception while closing the context", ex);
            }
        }
    }
}
