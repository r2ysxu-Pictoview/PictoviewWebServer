package com.viewer.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viewer.bean.BeanManager;
import com.viewer.beans.AlbumBeanLocal;
import com.viewer.dto.AlbumDTO;
import com.viewer.model.SearchQuery;
import com.viewer.util.StringUtil;

@Controller
public class AlbumsController {

	@Resource(mappedName = BeanManager.ALBUM_JNDI_NAME)
	protected AlbumBeanLocal albumBean;

	public AlbumsController() {
	}

	/**
	 * Gets the Album page
	 * 
	 * @param albumId
	 * @return JSON containing load information
	 */
	@RequestMapping("albums/albums")
	public String fetchAlbumPage() {
		return "browser/albumView";
	}

	/**
	 * Gets public albums
	 * 
	 * @param albumId
	 * @return JSON containing load information
	 */
	@ResponseBody
	@RequestMapping("/albums/albums/public")
	public String fetchPublicAlbums(@RequestParam("albumId") long parentId) {
		List<AlbumDTO> albums = albumBean.fetchAllPublicAlbums(50, 0);
		JSONArray albumJson = generateAlbumJSON(albums);

		return albumJson.toString();
	}

	/**
	 * Gets subscribed Albums
	 * 
	 * @param albumId
	 * @return JSON containing load information
	 */
	@ResponseBody
	@RequestMapping("/albums/albums/get")
	public String fetchSubscribedAlbums(@RequestParam("albumId") long parentId) {
		UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<AlbumDTO> albums = albumBean.fetchViewableAlbums(principal.getUsername(), parentId);
		JSONArray albumJson = generateAlbumJSON(albums);

		return albumJson.toString();
	}

	/**
	 * Gets searched albums
	 * 
	 * @param albumId
	 * @return JSON containing albums
	 */
	@ResponseBody
	@RequestMapping(value = "albums/search", method = RequestMethod.POST)
	public String fetchAlbumSearchInfo(@RequestBody SearchQuery searchQuery) {
		UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<AlbumDTO> albums = albumBean.fetchSearchedUserAlbums(principal.getUsername(), searchQuery.toSearchQueryDTO());
		JSONArray albumJson = generateAlbumJSON(albums);
		return albumJson.toString();
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
	public String createAlbum(@RequestParam("albumName") String name,
			@RequestParam(required = false, value = "albumSub") String subtitle,
			@RequestParam(required = false, value = "permission") String permission,
			@RequestParam(required = false, value = "parentId") Long parentId) {

		UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (StringUtil.notNullEmpty(name)) {
			if (parentId == null) parentId = new Long(0);
			if (parentId == 0) albumBean.createAlbum(principal.getUsername(), name, subtitle, permission);
			else albumBean.createAlbum(principal.getUsername(), name, subtitle, parentId);
		}
		return "redirect:/albums/albums.do";
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
