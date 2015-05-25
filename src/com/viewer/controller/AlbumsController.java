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
import com.viewer.dto.PhotoDTO;

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

		System.out.println("albums/get " + albumId);

		List<AlbumDTO> albums = albumBean.fetchAllUserAlbums(1, albumId);
		JSONArray albumJson = generateAlbumJSON(albums);

		List<PhotoDTO> photos = albumBean.fetchUserAlbumPhotos(1, albumId);
		JSONArray photoJson = generatePhotoJSON(photos);

		List<String> categories = albumBean.fetchAllUserCategories(1);
		JSONArray categoriesJSON = generateCategoryJSON(categories);

		JSONObject responseJson = generateResponseJson(albumJson, photoJson,
				categoriesJSON);

		// Write JSON
		return albumJson.toString();
	}
	
	/**
	 * Gets initial information for page load
	 * 
	 * @param albumId
	 * @return JSON containing load information
	 */
	@ResponseBody
	@RequestMapping("/albums/search")
	public String fetchAlbumSearchInfo(@RequestParam("albumId") long albumId, String searchName, String searchTag) {

		System.out.println("albums/get " + albumId);
		
		String[] tags = searchTag.split(" ");

		List<AlbumDTO> albums = albumBean.fetchSearchedUserAlbums(1, searchName, tags);
		JSONArray albumJson = generateAlbumJSON(albums);

		// Write JSON
		return albumJson.toString();
	}

	@ResponseBody
	@RequestMapping("/albums/create")
	public void createAlbum() {
		albumBean.createAlbum(1, null);
	}

	/**
	 * Combines the JSON objects & arrays required for the page
	 * 
	 * @param albumJson
	 * @param photoJson
	 * @param categoryJSON
	 * @return JSON object or null if it fails
	 */
	private JSONObject generateResponseJson(JSONArray albumJson,
			JSONArray photoJson, JSONArray categoryJSON) {
		try {
			JSONObject responseJson = new JSONObject();
			responseJson.put("subAlbums", albumJson);
			responseJson.put("photos", photoJson);
			responseJson.put("categories", categoryJSON);
			return responseJson;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Turns a collection of CategoryDTOs into JSON array
	 * 
	 * @param categories
	 * @return JSON array object
	 */
	private JSONArray generateCategoryJSON(List<String> categories) {
		JSONArray categoriesJSON = new JSONArray();
		try {
			for (String category : categories) {
				JSONObject categoryJSON = new JSONObject();
				categoryJSON.put("category", category);
				categoriesJSON.put(categoryJSON);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return categoriesJSON;
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

	/**
	 * Turns a collection of PhotoDTOs into JSON array
	 * 
	 * @param photos
	 * @return JSON array object
	 */
	private JSONArray generatePhotoJSON(List<PhotoDTO> photos) {
		JSONArray photosJSON = new JSONArray();
		try {
			for (PhotoDTO photo : photos) {
				JSONObject photoJSON = new JSONObject();
				photoJSON.put("id", photo.getId());
				photoJSON.put("name", photo.getName());
				photosJSON.put(photoJSON);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return photosJSON;
	}
}
