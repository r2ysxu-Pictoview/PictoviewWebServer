package com.viewer.servlet.photo;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viewer.beans.AlbumBeanLocal;
import com.viewer.servlet.BeanManager;

/**
 * Servlet implementation class ImageThumbnailRequestServlet
 */
@WebServlet("/web/images/thumbnail")
public class ImageThumbnailRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private AlbumBeanLocal albumBean;
	private static final String UNFOUND_DEFAULT_IMAGE = "";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ImageThumbnailRequestServlet() {
		albumBean = BeanManager.getAlbumBeanLocal();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext();

		try {
			long photoId = Long.parseLong(request.getParameter("photoId"));
			long albumId = request.getParameter("albumId") == null ? 0 : Long
					.parseLong(request.getParameter("albumId"));
			
			// Get Image
			byte[] data = albumBean.fetchPhotoThumbnailData(1, photoId, 0);

			readImage(data, response);

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	private void readImage(byte[] data, HttpServletResponse response) {
		try {
			
			response.setContentLength(data.length);

			InputStream is = new BufferedInputStream(new ByteArrayInputStream(data));
			OutputStream out = response.getOutputStream();
			
			String mime = URLConnection.guessContentTypeFromStream(is);
			response.setContentType(mime);

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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
