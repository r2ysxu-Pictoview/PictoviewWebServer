package com.viewer.config.core;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.viewer.config.SpringWebConfig;
import com.viewer.file.ConfigProperties;

public class ViewerWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { SpringWebConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "*.do" };
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return null;
	}

	@Override
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
		registration.setMultipartConfig(getMultipartConfigElement());
	}

	private MultipartConfigElement getMultipartConfigElement() {
		MultipartConfigElement multipartConfigElement = new MultipartConfigElement(LOCATION, MAX_FILE_SIZE, MAX_REQUEST_SIZE,
				FILE_SIZE_THRESHOLD);
		return multipartConfigElement;
	}

	private static final String LOCATION = ConfigProperties.getProperty("tempStorageDirectory");
	private static final long MAX_FILE_SIZE = 5242880;
	private static final long MAX_REQUEST_SIZE = 20971520;
	private static final int FILE_SIZE_THRESHOLD = 0;
}
