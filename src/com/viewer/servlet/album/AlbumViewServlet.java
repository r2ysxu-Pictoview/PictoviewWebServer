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
import com.viewer.dto.AlbumDTO;
import com.viewer.dto.PhotoDTO;
import com.viewer.servlet.BeanManager;

/**
 * Servlet implementation class AlbumServlet
 */
@WebServlet("/pictureViewerWeb/galleryServlet")
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
		
		long albumId = Long.parseLong(request.getParameter("albumId"));

		List<AlbumDTO> albums = albumBean.fetchAllUserAlbums(1, albumId);
		JSONArray albumJson = generateAlbumJSON(albums);
		
		List<PhotoDTO> photos = albumBean.fetchUserAlbumPhotos(1, albumId);
		JSONArray photoJson = generatePhotoJSON(photos);
		
		JSONObject responseJson = generateResponseJson(albumJson, photoJson);

		// Write JSON
		PrintWriter out = response.getWriter();
		out.println(responseJson);
		out.close();
	}
	
	private JSONObject generateResponseJson(JSONArray albumJson, JSONArray photoJson) {
		try {
			JSONObject responseJson = new JSONObject();
			responseJson.put("subAlbums", albumJson);
			responseJson.put("photos", photoJson);
			return responseJson;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Turns a collection of AlbumDTOs to JSON string
	 * 
	 * @param albums
	 * @return JSON string
	 */
	private JSONArray generateAlbumJSON(List<AlbumDTO> albums) {
		JSONArray albumsJSON = new JSONArray();
		try {
			for (AlbumDTO album : albums) {
				JSONObject albumJSON = new JSONObject();
				albumJSON.put("id", album.getId());
				albumJSON.put("name", album.getName());
				albumJSON.put("coverId", album.getCoverId());
				albumsJSON.put(albumJSON);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return albumsJSON;
	}
	
	private JSONArray generatePhotoJSON(List<PhotoDTO> photos) {
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
		return photosJSON;
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
