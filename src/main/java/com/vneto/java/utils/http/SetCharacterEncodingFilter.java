package com.vneto.java.utils.http;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SetCharacterEncodingFilter implements Filter {

	private final String ENC_PARAM = "encoding", IGN_PARAM = "ignore"; 
	private FilterConfig config;
	private String encoding;
	private Boolean ignore;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		if( (config = filterConfig) != null ) { 
			if( ( encoding = config.getInitParameter(ENC_PARAM) ) == null )
				encoding = "UTF-8";	
			
			String sIgnore;
			
			ignore = ( ( sIgnore = config.getInitParameter(IGN_PARAM) ) == null ) ? new Boolean(false) 
					: Boolean.valueOf(sIgnore); 
		}
		else {
			encoding = "UTF-8";
			ignore = false;
		}
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		try {
			if(request instanceof HttpServletRequest) {
				HttpServletRequest httpReq = (HttpServletRequest) request;
				
				if( ignore || ( null == httpReq.getCharacterEncoding() ) )
					httpReq.setCharacterEncoding(encoding);
			}

			if(response instanceof HttpServletResponse) {
				HttpServletResponse httpResp = (HttpServletResponse) response;
				
				if( ignore || ( null == httpResp.getCharacterEncoding() ) )
					httpResp.setCharacterEncoding(encoding);
			}
		}
		catch(IOException ex) {
			if(config != null) {
				config.getServletContext().log("Error while filtering the request/response with SetCharacterEnconding filter. Exception details below", ex);
			}
			
			throw ex;
		}
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		
		config = null;
		encoding = null;
		
	}

}
