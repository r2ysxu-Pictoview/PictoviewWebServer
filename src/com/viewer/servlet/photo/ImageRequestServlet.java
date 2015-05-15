package com.viewer.servlet.photo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viewer.beans.AlbumBeanLocal;
import com.viewer.dto.PhotoDTO;
import com.viewer.servlet.BeanManager;

/**
 * Servlet implementation class AlbumImageServlet
 */
@WebServlet("/web/images")
public class ImageRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private AlbumBeanLocal albumBean;
	private static final String UNFOUND_DEFAULT_IMAGE = "";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ImageRequestServlet() {
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
			PhotoDTO photo = albumBean.fetchPhoto(1, photoId);

			File photoSource;
			if (photo != null)
				photoSource = photo.getSource();
			else
				photoSource = new File(".");

			// Set Response
			String mime = context.getMimeType(photoSource.getCanonicalPath());
			if (mime == null) {
				response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
				return;
			}
			response.setContentType(mime);
			readImage(photoSource, response);

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	private void readImage(File source, HttpServletResponse response) {
		try {
			response.setContentLength((int) source.length());

			FileInputStream in = new FileInputStream(source);
			OutputStream out = response.getOutputStream();

			// Copy the contents of the file to the output stream
			byte[] buf = new byte[1024];
			int count = 0;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}

			in.close();
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
