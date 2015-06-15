package com.viewer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.viewer.dto.CategoryDTO;
import com.viewer.dto.SearchQueryDTO;

public class SearchQuery implements Serializable {
	private static final long serialVersionUID = 1L;
	
	List<String> name;
	List<CategoryDTO> cate;
	
	public SearchQuery(List<String> name) {
		this.name = name;
		cate = new ArrayList<CategoryDTO>();
	}
	
	public void addCategory(String category, List<String> tags) {
		cate.add(new CategoryDTO(category, tags));
	}
	
	public SearchQueryDTO toSearchQueryDTO() {
		SearchQueryDTO sq = new SearchQueryDTO();
		sq.setNames(name);
		for (CategoryDTO c : cate)
			sq.insertTag(c.getCategory(), c.getTags());
		return sq;
	}

	public List<String> getName() {
		return name;
	}

	public void setName(List<String> name) {
		this.name = name;
	}

	public List<CategoryDTO> getCate() {
		return cate;
	}

	public void setCate(List<CategoryDTO> cate) {
		this.cate = cate;
	}
	
	public String toString() {
		return "Name: " + name + ", Category: " + cate;
	}
}
