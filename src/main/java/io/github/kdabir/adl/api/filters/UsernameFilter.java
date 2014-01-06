package io.github.kdabir.adl.api.filters;

/**
 *  Searches by user id (usually LAN id) in the active directory
 */
public class UsernameFilter implements SearchFilter{

    final private String username;

    public UsernameFilter(String username) {
        this.username = username;
    }

    @Override
    public String getFilter() { // sAMAccountName is specific to AD
        return "(&(objectClass=user)(sAMAccountName=" + username + "))";
    }
}
