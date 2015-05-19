package com.viewer.servlet.album;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.viewer.dto.AlbumTagsDTO;
import com.viewer.dto.TagsDTO;
import com.viewer.servlet.BeanManager;

/**
 * Servlet implementation class AlbumViewInfoServlet
 */
@WebServlet("/pictureViewerWeb/albumServlet/tags")
public class AlbumViewTagsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private AlbumBeanLocal albumBean;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AlbumViewTagsServlet() {
		albumBean = BeanManager.getAlbumBeanLocal();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			long albumId = Long.parseLong(request.getParameter("albumId"));

			AlbumTagsDTO albumTags = albumBean.fetchUserAlbumTags(1, albumId);
			String json = generateAlbumInfoJSON(albumTags);

			// Write JSON
			PrintWriter out = response.getWriter();
			out.println(json);
			out.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
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

	private String generateAlbumInfoJSON(AlbumTagsDTO albumTags) {
		JSONArray allTagsJSON = new JSONArray();
		HashMap<String, List<TagsDTO>> albumTagsList = albumTags.getTags();
		try {
			for (String category : albumTagsList.keySet()) {
				List<TagsDTO> tagList = albumTagsList.get(category);
				JSONObject categoryJSON = new JSONObject();
				categoryJSON.put("category", category);

				JSONArray tagListJSON = new JSONArray();
				for (TagsDTO tag : tagList) {
					JSONObject tagJSON = new JSONObject();
					tagJSON.put("tagId", tag.getId());
					tagJSON.put("tagName", tag.getName());
					tagListJSON.put(tagJSON);
				}
				categoryJSON.put("tags", tagListJSON);
				allTagsJSON.put(categoryJSON);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return allTagsJSON.toString();
	}
}
