package io.github.kdabir.adl.api;

import io.github.kdabir.adl.exceptions.ActiveDirectoryException;
import io.github.kdabir.adl.exceptions.NotFoundException;

import java.util.List;
import java.util.Map;
import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

/**
 *
 * @author Kunal Dabir
 */
public class ActiveDirectorySearcher {

    /**
     * To support of multi-search mode, stores the context so that bind is not 
     * required every time
     * 
     */
    protected LdapContext ldapContext = null;
    /**
     * Service
     */
    protected ActiveDirectoryLdapService activeDirectoryService = null;
    /**
     * 
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
     * @param url
     * @param domain
     * @param defaultSearchBase
     * @param username
     * @param password 
     */
    public ActiveDirectorySearcher(String url, String domain, String defaultSearchBase, String username, String password) {
        this(domain, url, defaultSearchBase, username, password, null);
    }    
    
    /**
     * Constructs the Searcher.  
     * 
     * Throws ActiveDirectoryException (RuntimeException) if something goes wrong
     * 
     * @param url 
     * @param domain 
     * @param defaultSearchBase
     * @param username 
     * @param password  
     * @param returnedAttrs -- only these attributes will retained in the returned map (in search)
     */
    public ActiveDirectorySearcher(String url, String domain, String defaultSearchBase, String username, String password, List<String> returnedAttrs) {
        this.defaultSearchBase = defaultSearchBase;
        this.returnedAttrs = returnedAttrs;
        this.activeDirectoryService = new ActiveDirectoryLdapService();
        try {
            this.ldapContext = activeDirectoryService.getLdapContext(url, domain, username, password);
        } catch (CommunicationException ex) {
            throw new ActiveDirectoryException("Cannot reach to Active Directory, Please check url", ex);
        } catch (AuthenticationException ex) {
            throw new ActiveDirectoryException("Cannot connect to Active Directory, Please check domain/username/password", ex);
        } catch (NamingException ex) {
            throw new ActiveDirectoryException("Exception while connecting to Active Directory", ex);
        }
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
            result = activeDirectoryService.search(ldapContext, searchBase, username);
            
            if (result == null ){
                throw new NotFoundException( username + " not found");
            }
            
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
