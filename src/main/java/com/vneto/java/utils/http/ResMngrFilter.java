package com.vneto.java.utils.http;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class ResMngrFilter implements Filter {

	String fileDestPath;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		if( (filterConfig == null) || (( fileDestPath = filterConfig.getInitParameter("fileDestPath") ) == null) ) 
			fileDestPath = "";
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String mimeType;
		
		if( (mimeType = request.getContentType()) != null ) {
			if(mimeType.toLowerCase().contains("multipart/form-data")) {
				HttpServletRequest httpReq = (HttpServletRequest) request;
				
				if(ServletFileUpload.isMultipartContent(httpReq)) {
					try {
						DiskFileItemFactory dskFileItemFac = new DiskFileItemFactory();
						dskFileItemFac.setRepository( new File( httpReq.getServletContext().getAttribute("javax.servlet.context.tempdir").toString() ) );
						ServletFileUpload servUpLd = new ServletFileUpload(dskFileItemFac);

						for (FileItem fileItem : servUpLd.parseRequest(httpReq)) {
							if(!fileItem.isFormField())
								fileItem.write(new File(fileDestPath + fileItem.getName()));
						}
						
					} catch (FileUploadException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
