package com.viewer.controller.mobile;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viewer.bean.BeanManager;
import com.viewer.beans.AlbumBeanLocal;
import com.viewer.dto.AlbumDTO;
import com.viewer.model.SearchQuery;
import com.viewer.security.model.AlbumUser;
import com.viewer.util.StringUtil;

@Controller
public class AlbumsMobileController {
	@Resource(mappedName = BeanManager.ALBUM_JNDI_NAME)
	protected AlbumBeanLocal albumBean;

	public AlbumsMobileController() {
	}

	/**
	 * Gets the Album page for mobile view
	 * 
	 * @param albumId
	 * @return JSON containing load information
	 */
	@RequestMapping("mobile/albums/albums")
	public String fetchMobileAlbumPage(ModelMap map) {

		List<AlbumDTO> albums = albumBean.fetchAllPublicAlbums(1000, 0);
		map.put("albumList", albums);

		return "mobile/albumView";
	}

	@ResponseBody
	@RequestMapping(value = "mobile/albums/search", method = RequestMethod.POST)
	public String fetchAlbumSearchInfoURL(@RequestBody final MultiValueMap<String, String> query) {

		AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<String> name = query.get("name[]");
		if (name == null) name = new ArrayList<String>();

		SearchQuery searchQuery = new SearchQuery();
		searchQuery.setNames(name);

		int categoryCount = Integer.parseInt(query.getFirst("cateNum"));

		for (int i = 0; i < categoryCount; i++) {
			String category = query.getFirst("cate[" + i + "][category]");
			List<String> tags = query.get("cate[" + i + "][tags][]");
			if (StringUtil.notNullEmpty(category) && tags != null && !tags.isEmpty()) searchQuery.addCategory(category, tags);
		}
		List<AlbumDTO> albums = albumBean.fetchSearchUserSubscribedAlbums(principal.getUserid(), searchQuery.toSearchQueryDTO());
		JSONArray albumJson = generateAlbumJSON(albums);
		return albumJson.toString();
	}

	/**
	 * Turns a collection of AlbumDTOs to JSON array
	 * 
	 * @param albums
	 * @return JSON array object
	 */
	protected JSONArray generateAlbumJSON(List<AlbumDTO> albums) {
		JSONArray albumsJSON = new JSONArray();
		try {
			for (AlbumDTO album : albums) {
				JSONObject albumJSON = new JSONObject();
				albumJSON.put("id", album.getId());
				albumJSON.put("name", album.getName());
				albumJSON.put("subtitle", StringUtil.emptyIfNull(album.getSubtitle()));
				albumJSON.put("coverId", 0);
				albumJSON.put("subalbums", new ArrayList<String>());
				albumsJSON.put(albumJSON);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return albumsJSON;
	}
}
