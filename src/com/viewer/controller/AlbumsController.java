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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.viewer.bean.BeanManager;
import com.viewer.beans.AlbumBeanLocal;
import com.viewer.dto.AlbumDTO;
import com.viewer.dto.PhotoDTO;
import com.viewer.model.SearchQuery;
import com.viewer.security.model.AlbumUser;
import com.viewer.util.ResponseUtil;
import com.viewer.util.StringUtil;
import com.viewer.util.StringUtils;

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

	@ResponseBody
	@RequestMapping("/albums/albums/info")
	public String fetchAlbumInfo(@RequestParam("albumId") long albumid) {
		AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		AlbumDTO albumInfo = albumBean.fetchUserAlbumInfo(principal.getUserid(), albumid);
		return generateAlbumInfo(albumInfo).toString();
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
		AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<AlbumDTO> albums = albumBean.fetchViewableAlbums(principal.getUserid(), parentId);
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
		AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<AlbumDTO> albums = albumBean.fetchSearchedUserAlbums(principal.getUserid(), searchQuery.toSearchQueryDTO());
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
	public String createAlbum(@RequestParam("albumName") String name, @RequestParam(required = false, value = "albumSub") String subtitle,
			@RequestParam(required = false, value = "permission") String permission,
			@RequestParam(required = false, value = "description") String description,
			@RequestParam(required = false, value = "file") MultipartFile file, @RequestParam(required = false, value = "parentId") Long parentId) {

		AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (StringUtil.notNullEmpty(name)) {
			if (parentId == null) parentId = new Long(0);

			long albumid = 0;
			if (parentId == 0)
				albumid = albumBean.createAlbum(principal.getUserid(), name, subtitle, description, permission);
			else
				albumid = albumBean.createAlbum(principal.getUserid(), name, subtitle, description, parentId);
			PhotoDTO coverPhoto = processPhotoFiles(albumid, principal.getUserid(), file);
			albumBean.setAlbumCoverPhoto(principal.getUserid(), albumid, coverPhoto.getId());
		}
		return "redirect:/albums/albums.do";
	}

	private PhotoDTO processPhotoFiles(long albumId, long userid, MultipartFile file) {
		try {
			String name = file.getOriginalFilename();
			String ext = ResponseUtil.convertContentTypeToExt(file.getContentType());
			if (StringUtils.notNullEmpty(ext)) {
				InputStream data = file.getInputStream();
				return albumBean.uploadPhoto(userid, albumId, name, ext, data, 1);
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
				albumsJSON.put(albumJSON);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return albumsJSON;
	}
}
