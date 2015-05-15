package com.viewer.servlet.album;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.viewer.beans.AlbumBeanLocal;
import com.viewer.dao.AlbumDAO;
import com.viewer.dao.AlbumDAOImpl;
import com.viewer.dto.PhotoDTO;
import com.viewer.servlet.BeanManager;

/**
 * Servlet implementation class PictureCollectionViewServlet
 */
@WebServlet("/pictureViewerWeb/albumServlet")
public class AlbumViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private AlbumBeanLocal albumBean;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AlbumViewServlet() {
		albumBean = BeanManager.getAlbumBeanLocal();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");

		try {
			long albumId = Long.parseLong(request.getParameter("albumId"));

			List<PhotoDTO> albums = albumBean.fetchUserAlbumPhotos(1, albumId);
			String json = generatePhotoJSON(albums);
			System.out.println(json);

			// Write JSON
			PrintWriter out = response.getWriter();
			out.println(json);
			out.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	private String generatePhotoJSON(List<PhotoDTO> photos) {
		JSONArray photosJSON = new JSONArray();
		try {
			for (PhotoDTO photo : photos) {
				JSONObject photoJSON = new JSONObject();
				photoJSON.put("id", photo.getId());
				photoJSON.put("name", photo.getName());
				photosJSON.put(photoJSON);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return photosJSON.toString();
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
