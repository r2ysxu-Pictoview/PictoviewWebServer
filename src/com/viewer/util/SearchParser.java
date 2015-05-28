package com.viewer.util;

import java.util.ArrayList;
import java.util.List;

import com.viewer.dto.SearchQueryDTO;

public class SearchParser {

	public SearchParser() {

	}

	public SearchQueryDTO parseSearchName(String nameQuery, String tagQuery) {
		SearchQueryDTO searchQuery = new SearchQueryDTO();

		// Parse Names
		if (!nameQuery.isEmpty()) {
			List<String> names = splitByQuote(nameQuery, ' ', '\"');
			searchQuery.setNames(names);
		}

		// Parse Tags
		String[] tagQueryList = tagQuery.split(":");

		String category = "tags";
		for (int i = 0; i < tagQueryList.length; i++) {
			if (i % 2 == 0) {
				category = tagQueryList[i];
			} else if (i % 2 != 0) {
				List<String> tags = splitByQuote(tagQueryList[i], ' ', '\"');
				String nextCategory = "";
				if (i < tagQueryList.length - 1)
					nextCategory = tags.remove(tags.size() - 1);
				searchQuery.insertTag(category, tags);
				category = nextCategory;
			}
		}

		return searchQuery;
	}

	private List<String> splitByQuote(String query, char delim, char quote) {
		List<String> values = new ArrayList<String>();

		String[] splitValues = query.split("" + delim);

		for (int i = 0; i < splitValues.length; i++) {
			String s = splitValues[i];
			if (s.equals(quote + quote)) {
				// Do nothing
			} else if (!s.endsWith(quote + "") && s.startsWith("" + quote)
					&& s.endsWith("" + quote)) {
				values.add(s.substring(1, s.length() - 1));
			} else if (s.startsWith("" + quote)) {
				String quotedString = "";
				quotedString += s.substring(1) + " ";
				i++;
				while (i < splitValues.length) {
					String s2 = splitValues[i];
					if (s2.equals("" + quote)) {
						break;
					} else if (s2.endsWith("" + quote)) {
						quotedString += s2.substring(0, s2.length() - 1);
						break;
					}
					quotedString += s2 + " ";
					i++;
				}
				values.add(quotedString.trim());
			} else {
				values.add(s);
			}
		}
		return values;
	}
}
