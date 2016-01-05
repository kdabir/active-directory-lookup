package io.github.kdabir.adl.util;

public class SearchBaseGuesser {
    public String guessFrom(String domain) {
        StringBuilder searchBase = new StringBuilder();
        String delim = "";

        for (String part : domain.toUpperCase().split("\\.")) {
            searchBase.append(delim).append("DC=" + part);
            delim = ",";
        }

        return searchBase.toString();
    }
}
