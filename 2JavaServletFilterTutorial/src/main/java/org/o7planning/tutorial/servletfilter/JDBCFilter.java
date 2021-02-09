package org.o7planning.tutorial.servletfilter;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.o7planning.tutorial.servletfilter.conn.ConnectionUtils;
import org.o7planning.tutorial.servletfilter.conn.MyUtils;

@WebFilter(urlPatterns = "/*")
public class JDBCFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	HttpServletRequest req = (HttpServletRequest) request;
	String servletPath = req.getServletPath();
	if (servletPath.contains("/specialPath1") || servletPath.contains("/specialPath2")) {
	    Connection conn = null;
	    try {
		// Create a Connection.
		conn = ConnectionUtils.getConnection();
		// Set auto commit = false
		conn.setAutoCommit(false);

		// Store connection in attribute of request.
		MyUtils.storeConnection(request, conn);

		// Go to next element (filter or target) in chain
		chain.doFilter(request, response);

		// Call commit() to commit transaction.
		conn.commit();
	    } catch (Exception e) {
		ConnectionUtils.rollbackQuietly(conn);
		throw new ServletException();
	    } finally {
		ConnectionUtils.closeQuietly(conn);
	    }
	}
	// For common request.
	else {
	    // Go to next element (filter or target) in chain.
	    chain.doFilter(request, response);
	}
    }

}
