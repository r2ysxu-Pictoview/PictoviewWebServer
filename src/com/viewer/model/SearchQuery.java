package com.viewer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.viewer.dto.CategoryDTO;
import com.viewer.dto.SearchQueryDTO;

public class SearchQuery implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<String> names;
	private List<CategoryDTO> cate;

	public SearchQuery() {
		names = new ArrayList<String>();
		cate = new ArrayList<CategoryDTO>();
	}
	
	public void addCategory(String category, List<String> tags) {
		cate.add(new CategoryDTO(category, tags));
	}

	public SearchQueryDTO toSearchQueryDTO() {
		SearchQueryDTO sq = new SearchQueryDTO();
		sq.setNames(names);
		for (CategoryDTO c : cate)
			sq.insertTag(c.getCategory(), c.getTags());
		return sq;
	}

	public List<CategoryDTO> getCate() {
		return cate;
	}

	public void setCate(List<CategoryDTO> cate) {
		this.cate = cate;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public String toString() {
		return "Name: " + names + ", Category: " + cate;
	}
}
