package com.viewer.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.imageio.stream.ImageInputStream;

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viewer.bean.BeanManager;
import com.viewer.beans.AlbumBeanLocal;
import com.viewer.security.model.AlbumUser;

@Controller
public class ImageController {

	@Resource(mappedName = BeanManager.ALBUM_JNDI_NAME)
	private AlbumBeanLocal albumBean;

	public ImageController() {
	}

	@ResponseBody
	@RequestMapping(value = "albums/images/cover", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public void fetchAlbumCoverPhoto(@RequestParam("albumid") long albumId, OutputStream responseOutput) {
		if (albumId == 0) return;

		AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			// Get Image
			ImageInputStream is = albumBean.fetchAlbumCoverThumbnail(principal.getUserid(), albumId, 0);
			writeImageStreamToResponse(is, responseOutput);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets Image thumbnail
	 * 
	 * @param photoid
	 */
	@ResponseBody
	@RequestMapping(value = "albums/images/thumbnail", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public void fetchAlbumPhotoThumbnail(@RequestParam("photoid") long photoid, OutputStream responseOutput) {
		if (photoid == 0) return;

		AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			// Get Image
			ImageInputStream is = albumBean.fetchPhotoThumbnailData(principal.getUserid(), photoid, 0);
			writeImageStreamToResponse(is, responseOutput);
			// return data;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets Image
	 * 
	 * @param photoid
	 * @param responseOutput
	 */
	@ResponseBody
	@RequestMapping(value = "albums/images", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public void fetchAlbumPhoto(@RequestParam("photoid") long photoid, OutputStream responseOutput) {

		AlbumUser principal = (AlbumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ImageInputStream is = albumBean.fetchPhotoData(principal.getUserid(), photoid);
		writeImageStreamToResponse(is, responseOutput);
	}

	private void writeImageStreamToResponse(ImageInputStream is, OutputStream out) {
		try {

			// Copy the contents of the file to the output stream
			byte[] buf = new byte[1024];
			int count = 0;
			while ((count = is.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}

			is.close();
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
