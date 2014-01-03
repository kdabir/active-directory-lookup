package io.github.kdabir.adl.api;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResultMapper {

    List<Map<String, String>> mapResult(NamingEnumeration<SearchResult> namingEnumeration) throws NamingException {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();

        while (namingEnumeration.hasMoreElements()) { // no need to loop
            SearchResult searchResult = namingEnumeration.next();

            Map<String, String> row = new HashMap<String, String>();
            Attributes attrs = searchResult.getAttributes();

            if (attrs != null) {
                NamingEnumeration ne = attrs.getAll();
                while (ne.hasMore()) {
                    Attribute attribute = (Attribute) ne.next();
                    row.put(attribute.getID(), attribute.get().toString());
                }
                ne.close();
            }
            result.add(row);
        }

        return result;
    }
}