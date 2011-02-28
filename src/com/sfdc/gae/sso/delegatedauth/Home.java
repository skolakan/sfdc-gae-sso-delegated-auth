package com.sfdc.gae.sso.delegatedauth;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Logger;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.UserService;

@SuppressWarnings("serial")

public class Home extends HttpServlet
{
	// logout URL
	public final String logoutURL = "http://salesforce.com/";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		Logger log = Logger.getLogger(Home.class.getName());
		UserService userService = UserServiceFactory.getUserService();
		
		// work out which login url to use (Customer/Partner Portal or standard app)
	    String salesforceLoginURL = req.getParameter("salesforceloginurl");
	    
	    if ((salesforceLoginURL == null) || salesforceLoginURL.equals(""))
	    {
	    	salesforceLoginURL = "https://login.salesforce.com";
	    }
	    else
	    {
	    	salesforceLoginURL = Utility.cleanXSS(salesforceLoginURL);
	    }

		log.info("Using salesforce login url <" + salesforceLoginURL + ">");
		log.info("Google Account <" + userService.getCurrentUser().getEmail() + ">");

	    resp.setContentType("text/html");
		resp.getWriter().println("<html>");
		resp.getWriter().println("<head><title>Salesforce/Google App Engine SSO</title></head>");
		resp.getWriter().println("<body>");
		resp.getWriter().println("<h1>Single Sign On with Delegated Authentication example</h1>");
		resp.getWriter().println("<p>");
		resp.getWriter().println("Salesforce");
		resp.getWriter().println("</p>");
		resp.getWriter().println("<ul>");
		resp.getWriter().println("<li><a href=\"/login?salesforceloginurl=" + URLEncoder.encode(salesforceLoginURL,"UTF-8") + "\">Login to salesforce</a> with login url <strong>" + salesforceLoginURL + "</strong> and username <strong>" + userService.getCurrentUser().getEmail() + "</strong></li>");
		resp.getWriter().println("</ul>");
		resp.getWriter().println("<p>");
		resp.getWriter().println("Google");
		resp.getWriter().println("</p>");
		resp.getWriter().println("<ul>");
		resp.getWriter().println("<li><a href=\"http://mail.google.com\">Google Mail</a></li>");
		resp.getWriter().println("<li><a href=\"http://picasaweb.google.com/\">Google Picasa</a></li>");
		resp.getWriter().println("<li><a href=\"" + userService.createLogoutURL(logoutURL)+ "\">Logout from Google App Engine</a></li>");
		resp.getWriter().println("</ul>");
		
		resp.getWriter().println("<p>");
		resp.getWriter().println("This </strong>Salesforce Single Sign On with Delegated Authentication example</strong> using Google App Engine is for demostrations purposes only, it is not currently designed for production systems");
		resp.getWriter().println("</p>");

		resp.getWriter().println("</body>");
		resp.getWriter().println("</html>");

	}
}
