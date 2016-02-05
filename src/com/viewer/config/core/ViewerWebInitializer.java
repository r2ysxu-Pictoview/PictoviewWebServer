package com.viewer.config.core;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.viewer.config.SpringWebConfig;

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
}
