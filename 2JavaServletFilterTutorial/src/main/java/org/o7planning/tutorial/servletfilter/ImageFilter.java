package org.o7planning.tutorial.servletfilter;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = { "*.png", "*.jpg", "*.gif" }, initParams = { @WebInitParam(name = "notFoundImage", value = "/images/image-not-found.png") })
public class ImageFilter implements Filter {

    private String notFoundImage;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
	this.notFoundImage = filterConfig.getInitParameter("notFoundImage");
	System.out.println("Init ImageFilter");
    }

    @Override
    public void destroy() {
	System.out.println("destroy ImageFilter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	HttpServletRequest req = (HttpServletRequest) request;

	String servletPath = req.getServletPath();
	String realPath = req.getServletContext().getRealPath("");
	String imageRealPath = realPath + servletPath;

	System.out.println("servletPath: " + servletPath);
	System.out.println("realPath: " + realPath);
	System.out.println("imageRealPath: " + imageRealPath);

	File file = new File(imageRealPath);

	if (file.exists()) {
	    chain.doFilter(request, response);
	} else if (!servletPath.equals(this.notFoundImage)) {
	    HttpServletResponse resp = (HttpServletResponse) response;
	    resp.sendRedirect(req.getContextPath() + this.notFoundImage);
	}

    }

}
