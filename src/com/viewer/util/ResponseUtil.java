package com.viewer.util;

public class ResponseUtil {

	public static String convertContentTypeToExt(String contentType) {
		if ("image/png".equals(contentType)) return "png";
		else if ("image/jpeg".equals(contentType)) return "jpg";
		else if ("image/gif".equals(contentType)) return "gif";
		else if ("image/bmp".equals(contentType)) return "bmp";
		return null;
	}
}
