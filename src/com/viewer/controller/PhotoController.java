package com.viewer.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.viewer.bean.BeanManager;
import com.viewer.beans.AlbumBeanLocal;
import com.viewer.dto.PhotoDTO;

@Controller
public class PhotoController {
	private AlbumBeanLocal albumBean;

	public PhotoController() {
		albumBean = BeanManager.getAlbumBeanLocal();
	}

	/**
	 * Gets the Photo page
	 * 
	 * @param albumId
	 * @return JSON containing load information
	 */
	@RequestMapping("/photos")
	public String fetchPhotoPage(ModelMap map, @RequestParam("albumId") long albumId) {

		List<PhotoDTO> photos = albumBean.fetchUserAlbumPhotos(1, albumId);
		map.put("photoList", photos);

		return "photoView";
	}
}