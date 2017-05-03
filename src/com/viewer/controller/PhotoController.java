package com.viewer.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.viewer.bean.BeanManager;
import com.viewer.beans.AlbumBeanLocal;
import com.viewer.dto.PhotoDTO;
import com.viewer.security.model.AlbumUser;
import com.viewer.util.ResponseUtil;
import com.viewer.util.StringUtils;

@Controller
public class PhotoController {
	
	@Resource(mappedName = BeanManager.ALBUM_JNDI_NAME)
	private AlbumBeanLocal albumBean;

	public PhotoController() {
		
	}

	/**
	 * Gets the Photo page
	 * 
	 * @param albumId
	 * @return JSON containing load information
	 */
	@RequestMapping("/albums/photos")
	public String fetchPhotoPage(ModelMap map,
			@RequestParam("albumId") long albumId) {

		AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<PhotoDTO> photos = albumBean.fetchUserAlbumPhotos(principal.getUserid(), albumId);
		map.put("photoList", photos);
		map.put("photoCount", "" + photos.size());
		map.put("albumId", albumId);

		return "browser/photoView";
	}

	@RequestMapping(value = "albums/upload", method = RequestMethod.POST)
	public String handleFileUpload(ModelMap map,
			@RequestParam("albumId") Long albumId,
			@RequestParam("file") List<MultipartFile> files) {
		
		AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		for (MultipartFile file : files) {
			processPhotoFiles(albumId, principal.getUserid(), file);
		}
		return "redirect:/albums/photos.do?albumId=" + albumId;
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
}