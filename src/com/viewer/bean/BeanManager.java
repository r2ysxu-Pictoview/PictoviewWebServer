package com.viewer.bean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.viewer.beans.AlbumBeanLocal;

public class BeanManager {
	
	static AlbumBeanLocal albumBean;
	
	static {
		try {
			Context c = new InitialContext();
			albumBean = (AlbumBeanLocal) c
					.lookup("java:global/PictureViewerEAR/PictureViewerEJB/AlbumBean!com.viewer.beans.AlbumBeanLocal");
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
	}
	
	public static AlbumBeanLocal getAlbumBeanLocal() {
		return albumBean;
	}

}