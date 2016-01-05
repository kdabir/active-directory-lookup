package io.github.kdabir.adl.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Kunal Dabir
 */
public class ActiveDirectoryConfig {

    // keys in the properties file
    public static final String SERVER_URL_KEY = "activedirectory.server.url";
    public static final String SERVER_DOMAIN_KEY = "activedirectory.server.domain";
    public static final String SEARCH_BASE_KEY = "activedirectory.search.base";
    public static final String BIND_USERNAME_KEY = "activedirectory.bind.username";
    public static final String BIND_PASSWORD_KEY = "activedirectory.bind.password";
    public static final String LOOKUP_ATTRS_KEY = "activedirectory.lookup.attrs";
    // the default name of properties file
    public static final String DEFAULT_CONFIG_FILE_NAME = "adl.properties";
    public static final String EMPTY_STRING = "";


    // properties holder
    final Properties config = new Properties();

    /**
     * Constructs the Config using the default config file which should be placed in
     * classpath (given preference) or current directory
     *
     * @throws IOException if
     */
    public ActiveDirectoryConfig() throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILE_NAME);
        if (stream != null) {
            config.load(stream);
            stream.close();
        } else {
            loadConfigFromFile("./" + DEFAULT_CONFIG_FILE_NAME);
        }
    }

    /**
     * constructs the Config using specified properties file name (including path)
     *
     * @param filename
     * @throws IOException
     */
    public ActiveDirectoryConfig(String filename) throws IOException {
        loadConfigFromFile(filename);
    }

    private void loadConfigFromFile(String filename) throws IOException {
        final FileInputStream inStream = new FileInputStream(filename);
        try {
            config.load(inStream);
        } finally {
            inStream.close();
        }
    }

    /**
     * constructs the Config using specified properties file name (including path)
     *
     * @param stream
     * @throws IOException
     */
    public ActiveDirectoryConfig(InputStream stream) throws IOException {
        config.load(stream);
    }


    public String getUsername() {
        return config.getProperty(BIND_USERNAME_KEY, EMPTY_STRING).trim();
    }

    public String getPassword() {
        return config.getProperty(BIND_PASSWORD_KEY, EMPTY_STRING).trim();
    }

    public String getUrl() {
        return config.getProperty(SERVER_URL_KEY, EMPTY_STRING).trim();
    }

    public String getDomain() {
        return config.getProperty(SERVER_DOMAIN_KEY, EMPTY_STRING).trim();
    }

    public String getSearchBase() {
        return config.getProperty(SEARCH_BASE_KEY, EMPTY_STRING).trim();
    }

    public List<String> getLookupAttrs() {
        return Arrays.asList(config.getProperty(LOOKUP_ATTRS_KEY, EMPTY_STRING).split(",( )*")); // splits by comma followed by zero or more spaces
    }

    @Override
    public String toString() {
        return config.toString();
    }

    /**
     * @param string
     * @return true if string has some non whitespace characters
     */
    public static boolean hasValue(String string) {
        return string != null && string.trim().length() > 0;
    }

}
