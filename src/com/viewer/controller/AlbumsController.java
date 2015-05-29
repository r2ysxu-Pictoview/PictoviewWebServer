package com.viewer.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viewer.bean.BeanManager;
import com.viewer.beans.AlbumBeanLocal;
import com.viewer.dto.AlbumDTO;
import com.viewer.dto.SearchQueryDTO;
import com.viewer.util.SearchParser;

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
	@RequestMapping("/albums")
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
	@RequestMapping("/albums/get")
	public String fetchAlbumPageInfo(@RequestParam("albumId") long albumId) {

		List<AlbumDTO> albums = albumBean.fetchAllUserAlbums(1, albumId);
		JSONArray albumJson = generateAlbumJSON(albums);

		// Write JSON
		return albumJson.toString();
	}

	/**
	 * Gets initial information for page load
	 * 
	 * @param albumId
	 * @return JSON containing load information
	 */
	@RequestMapping("/search")
	public String fetchAlbumSearchInfo(
			@RequestParam("nameQuery") String nameQuery,
			@RequestParam("tagQuery") String tagQuery, ModelMap map) {
		SearchParser sp = new SearchParser();
		if (!tagQuery.startsWith("tags:")) tagQuery = "tags:" + tagQuery;
		
		SearchQueryDTO searchQuery = sp.parseSearchName(nameQuery, tagQuery);
		List<AlbumDTO> albums = albumBean.fetchSearchedUserAlbums(1, searchQuery);

		map.put("albumList", albums);

		return "albumView";
	}

	@ResponseBody
	@RequestMapping("/albums/create")
	public void createAlbum() {
		albumBean.createAlbum(1, null);
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
