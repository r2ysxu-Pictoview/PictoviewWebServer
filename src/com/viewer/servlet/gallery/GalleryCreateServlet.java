package com.viewer.servlet.gallery;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.viewer.beans.AlbumBeanLocal;
import com.viewer.servlet.BeanManager;

/**
 * Servlet implementation class GalleryCreateServlet
 */
@WebServlet("/pictureViewerWeb/galleryCreateServlet")
public class GalleryCreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private AlbumBeanLocal albumBean;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GalleryCreateServlet() {
		albumBean = BeanManager.getAlbumBeanLocal();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userid = (Integer) session.getAttribute("uid");
		
		albumBean.createAlbum(userid, null);
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
