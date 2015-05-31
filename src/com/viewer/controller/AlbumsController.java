package com.viewer.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viewer.bean.BeanManager;
import com.viewer.beans.AlbumBeanLocal;
import com.viewer.dto.AlbumDTO;
import com.viewer.dto.SearchQueryDTO;
import com.viewer.util.SearchParser;
import com.viewer.util.StringUtil;

@Controller
public class AlbumsController {

	private AlbumBeanLocal albumBean;

	public AlbumsController() {
		albumBean = BeanManager.getAlbumBeanLocal();
	}

	/**
	 * Gets the Album page
	 * 
	 * @param albumId
	 * @return JSON containing load information
	 */
	@RequestMapping("/albums/albums")
	public String fetchAlbumPage(ModelMap map) {

		List<AlbumDTO> albums = albumBean.fetchAllUserAlbums(1, 0);
		map.put("albumList", albums);

		return "albumView";
	}

	/**
	 * Gets initial information for page load
	 * 
	 * @param albumId
	 * @return JSON containing load information
	 */
	@ResponseBody
	@RequestMapping("/albums/albums/get")
	public String fetchAlbumPageInfo(@RequestParam("albumId") long parentId) {

		// Write JSON
		return fetchAlbums(parentId);
	}

	/**
	 * Gets initial information for page load
	 * 
	 * @param albumId
	 * @return JSON containing load information
	 */
	@RequestMapping("/albums/search")
	public String fetchAlbumSearchInfo(
			@RequestParam("nameQuery") String nameQuery,
			@RequestParam("tagQuery") String tagQuery, ModelMap map) {
		SearchParser sp = new SearchParser();
		if (!tagQuery.startsWith("tags:"))
			tagQuery = "tags:" + tagQuery;

		SearchQueryDTO searchQuery = sp.parseSearchName(nameQuery, tagQuery);
		List<AlbumDTO> albums = albumBean.fetchSearchedUserAlbums(1,
				searchQuery);

		map.put("albumList", albums);

		return "albumView";
	}

	/**
	 * Creates an albums
	 * 
	 * @param name
	 * @param subtitle
	 * @param parentId
	 * @return album page
	 */
	@RequestMapping(value = "/albums/create", method = RequestMethod.POST)
	public String createAlbum(
			@RequestParam("albumName") String name,
			@RequestParam(required = false, value = "albumSub") String subtitle,
			@RequestParam(required = false, value = "parentId") Long parentId) {

		System.out.println("albums/create");
		if (StringUtil.notNullEmpty(name)) {
			if (parentId == null)
				parentId = new Long(0);
			albumBean.createAlbum(1, name, subtitle, parentId);
		}
		return "redirect:/albums/albums.do";
	}

	private String fetchAlbums(long parentId) {
		List<AlbumDTO> albums = albumBean.fetchAllUserAlbums(1, parentId);
		JSONArray albumJson = generateAlbumJSON(albums);

		return albumJson.toString();
	}

	/**
	 * Turns a collection of AlbumDTOs to JSON array
	 * 
	 * @param albums
	 * @return JSON array object
	 */
	private JSONArray generateAlbumJSON(List<AlbumDTO> albums) {
		JSONArray albumsJSON = new JSONArray();
		try {
			for (AlbumDTO album : albums) {
				JSONObject albumJSON = new JSONObject();
				albumJSON.put("id", album.getId());
				albumJSON.put("name", album.getName());
				albumJSON.put("subtitle", album.getSubtitle());
				albumJSON.put("coverId", album.getCoverId());
				albumsJSON.put(albumJSON);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return albumsJSON;
	}
}
