package io.github.kdabir.adl.cli;

import io.github.kdabir.adl.api.ActiveDirectoryAutheticator;
import io.github.kdabir.adl.util.ActiveDirectoryConfig;
import io.github.kdabir.adl.api.ActiveDirectorySearcher;

import java.io.Console;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The command line program to search the user in the AD
 * 
 * @author Kunal Dabir
 */
public class ActiveDirectoryCLI {

    public static void main(String[] args) {
        try {
            // create the config, load it from the default location
            ActiveDirectoryConfig config = new ActiveDirectoryConfig();

            if (!ActiveDirectoryConfig.hasValue(config.getDomain())
                    || !ActiveDirectoryConfig.hasValue(config.getUrl())
                    || !ActiveDirectoryConfig.hasValue(config.getSearchBase())) {
                System.err.println("Please set url, domain and search base in the config! see help");
            }

            String username = "", password = "";
            boolean authenticate = true;

            if (args.length == 0 || "-h".equalsIgnoreCase(args[0])) {
                System.err.println("usage: java -jar adl.jar <username> [-p <password>]");
                return;
            }
            if (args.length >= 1) {
                username = args[0];
                if (args.length >= 2 && "-p".equalsIgnoreCase(args[1])) {
                    authenticate = true;
                    if (args.length >= 3) {
                        password = args[2];
                    } else {
                        Console console = System.console();
                        if (console != null) {
                            password = new String(console.readPassword("Enter password for [%s]: ", username));
                        } else {
                            System.out.println("Could not get console to read password, try entering -p <password>");
                            return;
                        }

                    }
                }
            }

            Map<String, String> result = new HashMap<String, String>();

            if (authenticate) {
                ActiveDirectoryAutheticator authenticator = new ActiveDirectoryAutheticator(
                        config.getDomain(),
                        config.getUrl(),
                        config.getSearchBase(),
                        config.getLookupAttrs());
                try {
                    result = authenticator.authenticate(username, password);
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            } else {
                ActiveDirectorySearcher searcher = new ActiveDirectorySearcher(
                        config.getUrl(),
                        config.getDomain(),
                        config.getSearchBase(),
                        config.getUsername(),
                        config.getPassword(),
                        config.getLookupAttrs());
                try {
                    result = searcher.search(args[0]);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            for (Map.Entry<String, String> entry: result.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

        } catch (IOException ex) {
            System.err.println("error");
            ex.printStackTrace();
        }

    }
}
