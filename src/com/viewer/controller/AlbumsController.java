package com.viewer.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.viewer.bean.BeanManager;
import com.viewer.beans.AlbumBeanLocal;
import com.viewer.dto.AlbumDTO;
import com.viewer.dto.MediaDTO;
import com.viewer.model.SearchQuery;
import com.viewer.security.model.AlbumUser;
import com.viewer.util.ResponseUtil;
import com.viewer.util.StringUtil;
import com.viewer.util.StringUtils;

@Controller
public class AlbumsController {

	@Resource(mappedName = BeanManager.PHOTO_ALBUM_JNDI_NAME)
	protected AlbumBeanLocal albumBean;

	public AlbumsController() {
	}

	/**
	 * Gets the Album page
	 * 
	 * @param albumId
	 * @return JSON containing load information
	 */
	@RequestMapping("albums/view")
	public String fetchAlbumPage() {
		return "browser/albumView";
	}

	@RequestMapping("albums/user")
	public String fetchUserAlbumPage(Model model) {
		model.addAttribute("headerMessage", "My Albums");
		model.addAttribute("userControls", "true");
		model.addAttribute("fetchUrl", "'user/get.do'");
		model.addAttribute("searchUrl", "'user/search.do'");
		return "browser/userAlbumView";
	}

	@RequestMapping("albums/subscribed")
	public String fetchUserSubscribedPage(Model model) {
		model.addAttribute("headerMessage", "My Subscribed Albums");
		model.addAttribute("userControls", "false");
		model.addAttribute("fetchUrl", "'subscribed/get.do'");
		model.addAttribute("searchUrl", "'subscribed/search.do'");
		return "browser/userAlbumView";
	}

	@RequestMapping("albums/viewable")
	public String fetchUserViewablePage(Model model) {
		model.addAttribute("headerMessage", "All Albums");
		model.addAttribute("userControls", "false");
		model.addAttribute("fetchUrl", "'albums/get.do'");
		model.addAttribute("searchUrl", "'albums/search.do'");
		return "browser/userAlbumView";
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

	@ResponseBody
	@RequestMapping("/albums/albums/get")
	public String fetchUserViewableAlbums(@RequestParam("albumId") long parentId,
			@RequestParam(value = "limit", defaultValue = "50", required = false) int limit,
			@RequestParam(value = "offset", defaultValue = "0", required = false) int offset) {
		AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<AlbumDTO> albums = albumBean.fetchViewableAlbums(principal.getUserid(), parentId, 0, limit, offset);
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
	@RequestMapping("/albums/subscribed/get")
	public String fetchSubscribedAlbums(@RequestParam("albumId") long parentId,
			@RequestParam(value = "limit", defaultValue = "50", required = false) int limit,
			@RequestParam(value = "offset", defaultValue = "0", required = false) int offset) {
		AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<AlbumDTO> albums = albumBean.fetchUserSubscriptions(principal.getUserid(), parentId, 0, limit, offset);
		JSONArray albumJson = generateAlbumJSON(albums);

		return albumJson.toString();
	}

	@ResponseBody
	@RequestMapping("/albums/user/get")
	public String fetchUserAlbums(@RequestParam("albumId") long parentId,
			@RequestParam(value = "limit", defaultValue = "50", required = false) int limit,
			@RequestParam(value = "offset", defaultValue = "0", required = false) int offset) {
		AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<AlbumDTO> albums = albumBean.fetchUserAlbums(principal.getUserid(), parentId, 0, limit, offset);
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
	@RequestMapping(value = "albums/subscribed/search", method = RequestMethod.POST)
	public String fetchAlbumSearchSubscribed(@RequestBody SearchQuery searchQuery) {
		AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<AlbumDTO> albums = albumBean.fetchSearchUserSubscribedAlbums(principal.getUserid(),
				searchQuery.toSearchQueryDTO());
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
	@RequestMapping(value = "albums/user/search", method = RequestMethod.POST)
	public String fetchAlbumSearchUser(@RequestBody SearchQuery searchQuery) {
		AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<AlbumDTO> albums = albumBean.fetchSearchUserAlbums(principal.getUserid(), searchQuery.toSearchQueryDTO());
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
			@RequestParam(required = false, value = "description") String description,
			@RequestParam(required = false, value = "file") MultipartFile file,
			@RequestParam(required = false, value = "parentId") Long parentId) {

		AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (StringUtil.notNullEmpty(name)) {
			if (parentId == null) parentId = new Long(0);

			long albumid = 0;
			if (parentId == 0) albumid = albumBean.createAlbum(principal.getUserid(), name, subtitle, description, permission);
			else albumid = albumBean.createAlbum(principal.getUserid(), name, subtitle, description, parentId);
			MediaDTO coverPhoto = processPhotoFiles(albumid, principal.getUserid(), file);
			albumBean.setAlbumCoverPhoto(principal.getUserid(), albumid, coverPhoto.getId());
		}
		return "success";
	}

	/**
	 * Subscribes to an album
	 * 
	 * @param albumId
	 */
	@RequestMapping(value = "/albums/subscribe", method = RequestMethod.POST)
	public void subscribeToAlbum(@RequestParam("albumId") Long albumId) {
		AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		albumBean.subscribeToAlbum(principal.getUserid(), albumId);
	}

	/**
	 * Assigns a rating to album
	 * 
	 * @param albumId
	 * @param rating
	 */
	@RequestMapping(value = "/albums/rating", method = RequestMethod.POST)
	public void voteAlbumRating(@RequestParam("albumId") Long albumId, @RequestParam("rating") Integer rating) {
		if (rating > 10 || rating < 0) return;
		AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		albumBean.voteAlbumRating(principal.getUserid(), albumId, rating);
	}

	@ResponseBody
	@RequestMapping(value = "/albums/rating", method = RequestMethod.GET)
	public String fetchUserAlbumRating(@RequestParam("albumId") Long albumId) {
		AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		albumBean.fetchAlbumUserRating(principal.getUserid(), albumId);
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/albums/rating/avg", method = RequestMethod.GET)
	public String fetchAverageAlbumRating(@RequestParam("albumId") Long albumId) {
		AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		albumBean.fetchAlbumAverageRating(principal.getUserid(), albumId);
		return null;
	}

	private MediaDTO processPhotoFiles(long albumId, long userid, MultipartFile file) {
		try {
			String name = file.getOriginalFilename();
			String ext = ResponseUtil.convertContentTypeToExt(file.getContentType());
			if (StringUtils.notNullEmpty(ext)) {
				InputStream data = file.getInputStream();
				return albumBean.uploadMedium(userid, albumId, name, ext, data, 1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected JSONObject generateAlbumInfo(AlbumDTO album) {
		JSONObject albumJSON = new JSONObject();
		albumJSON.put("id", album.getId());
		albumJSON.put("description", album.getDescription());
		return albumJSON;
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
				albumJSON.put("description", album.getDescription());
				albumJSON.put("ratings", album.getRating());
				albumsJSON.put(albumJSON);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return albumsJSON;
	}
}
