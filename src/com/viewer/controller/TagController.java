package com.viewer.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viewer.bean.BeanManager;
import com.viewer.beans.AlbumBeanLocal;
import com.viewer.dto.AlbumTagsDTO;
import com.viewer.dto.TagsDTO;
import com.viewer.security.model.AlbumUser;
import com.viewer.util.StringUtil;

@Controller
public class TagController {

	@Resource(mappedName = BeanManager.PHOTO_ALBUM_JNDI_NAME)
	private AlbumBeanLocal albumBean;

	public TagController() {
	}

	@ResponseBody
	@RequestMapping("/albums/tag/get")
	public String fetchAlbumTagInfo(@RequestParam("albumId") long albumId) {

		try {
			AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			AlbumTagsDTO albumTags = albumBean.fetchUserAlbumTags(principal.getUserid(), albumId);
			String json = generateAlbumInfoJSON(albumTags);
			return json;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/albums/category/get")
	public String fetchCategories() {

		try {

			AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<String> categories = albumBean.fetchAllUserCategories(principal.getUserid());
			String json = new JSONArray(categories).toString();
			return json;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	@ResponseBody
	@RequestMapping("/albums/tag/create")
	public void createAlbumTag(@RequestParam("albumId") long albumId,
			@RequestParam("categoryName") String category,
			@RequestParam("tags") List<String> tag) {
		try {
			AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtil.notNullEmpty(category)) {
				albumBean.createCategory(principal.getUserid(), category);
				albumBean.tagUserAlbum(principal.getUserid(), albumId, tag, category);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	@ResponseBody
	@RequestMapping("/albums/tag/plus")
	public void upvoteAlbumTag(@RequestParam("tagId") long tagId) {
		albumBean.tagRelevanceAlbum(tagId);
	}

	private String generateAlbumInfoJSON(AlbumTagsDTO albumTags) {
		JSONArray allTagsJSON = new JSONArray();
		HashMap<String, List<TagsDTO>> albumTagsList = albumTags.getTags();
		try {
			for (String category : albumTagsList.keySet()) {
				List<TagsDTO> tagList = albumTagsList.get(category);
				JSONObject categoryJSON = new JSONObject();
				categoryJSON.put("category", category);

				JSONArray tagListJSON = new JSONArray();
				for (TagsDTO tag : tagList) {
					JSONObject tagJSON = new JSONObject();
					tagJSON.put("tagId", tag.getId());
					tagJSON.put("tagName", tag.getName());
					tagJSON.put("tagR", tag.getRelevance());
					tagListJSON.put(tagJSON);
				}
				categoryJSON.put("tags", tagListJSON);
				allTagsJSON.put(categoryJSON);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return allTagsJSON.toString();
	}
}
