package com.viewer.bean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.viewer.beans.AlbumBeanLocal;

public class BeanManager {
	
	public static AlbumBeanLocal getAlbumBeanLocal() {
		AlbumBeanLocal albumBean;
		try {
			Context c = new InitialContext();
			albumBean = (AlbumBeanLocal) c
					.lookup("java:global/PictureViewerEAR/PictureViewerEJB/AlbumBean!com.viewer.beans.AlbumBeanLocal");
		} catch (NamingException e1) {
			e1.printStackTrace();
			return null;
		}
		return albumBean;
	}

}
