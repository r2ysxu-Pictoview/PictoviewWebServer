package com.viewer.servlet.album;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viewer.beans.AlbumBeanLocal;
import com.viewer.servlet.BeanManager;

/**
 * Servlet implementation class AlbumViewTagCreateServlet
 */
@WebServlet("/pictureViewerWeb/albumServlet/tags/create")
public class AlbumTagCreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private AlbumBeanLocal albumBean;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlbumTagCreateServlet() {
    	albumBean = BeanManager.getAlbumBeanLocal();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			long albumId = Long.parseLong(request.getParameter("albumId"));
			long cateId = Long.parseLong(request.getParameter("cateId"));
			String tag = request.getParameter("tagName");
			albumBean.tagUserAlbum(1, albumId, tag, cateId);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
