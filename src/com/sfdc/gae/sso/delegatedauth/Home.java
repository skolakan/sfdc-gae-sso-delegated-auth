/*
Copyright (c) 2011, Mark Sivill, salesforce.com
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
    * Neither the name of the salesforce.com nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

/*
History
Version		Date			Author							Comments
1.0			01-10-2010		Mark Sivill	(salesforce.com)	Initial version
*/


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
