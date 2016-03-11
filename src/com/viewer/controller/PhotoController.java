package com.viewer.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.viewer.bean.BeanManager;
import com.viewer.beans.AlbumBeanLocal;
import com.viewer.dto.PhotoDTO;

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

		UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<PhotoDTO> photos = albumBean.fetchUserAlbumPhotos(principal.getUsername(), albumId);
		map.put("photoList", photos);
		map.put("photoCount", "" + photos.size());
		map.put("albumId", albumId);

		return "mobile/photoView";
	}

	@RequestMapping(value = "albums/upload", method = RequestMethod.POST)
	public String handleFileUpload(ModelMap map,
			@RequestParam("albumId") Long albumId,
			@RequestParam("file") List<MultipartFile> files) {
		
		for (MultipartFile file : files) {
			processPhotoFiles(albumId, file);
		}
		return "redirect:/albums/photos.do?albumId=" + albumId;
	}

	private void processPhotoFiles(long albumId, MultipartFile file) {
		try {
			String name = file.getOriginalFilename();
			InputStream data = file.getInputStream();
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			albumBean.uploadPhoto(principal.getUsername(), albumId, name, data, 1);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}