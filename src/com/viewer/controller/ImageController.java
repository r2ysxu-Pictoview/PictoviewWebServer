package com.viewer.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.stream.ImageInputStream;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viewer.bean.BeanManager;
import com.viewer.beans.AlbumBeanLocal;

@Controller
public class ImageController {

	private AlbumBeanLocal albumBean;

	public ImageController() {
		albumBean = BeanManager.getAlbumBeanLocal();
	}

	/**
	 * Gets Image thumbnail
	 * 
	 * @param photoid
	 */
	@ResponseBody
	@RequestMapping(value = "albums/images/thumbnail", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public void fetchAlbumPhotoThumbnail(@RequestParam("photoid") long photoid,
			OutputStream responseOutput) {
		System.out.println("images/get " + photoid);
		try {
			// Get Image
			ImageInputStream is = albumBean.fetchPhotoThumbnailData(1, photoid, 0);
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
	public void fetchAlbumPhoto(@RequestParam("photoid") long photoid,
			OutputStream responseOutput) {

		ImageInputStream is = albumBean.fetchPhotoData(1, photoid);
		writeImageStreamToResponse(is, responseOutput);
	}

	private void writeImageStreamToResponse(ImageInputStream is,
			OutputStream out) {
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
