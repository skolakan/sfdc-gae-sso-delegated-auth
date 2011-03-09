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

// transition page 
public class Login extends HttpServlet
{

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{

		Logger log = Logger.getLogger(Login.class.getName());
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

		String token = "";
		try
		{
			token = Token.generateToken(userService.getCurrentUser().getEmail(), req.getRemoteAddr());
			log.info("Generated token <" + token + ">");

		}
		catch(Exception e)
		{
			throw (new IOException(e));
		}

	    resp.setContentType("text/html");
		resp.getWriter().println("<html>");
		resp.getWriter().println("<head><title>Logging into salesforce</title></head>");
		resp.getWriter().println("<body>");
		resp.getWriter().println("<p>Logging into salesforce<p>");
		resp.getWriter().println("<form name=\"form1\" action=\"" + salesforceLoginURL + "\" method=\"post\">");
		resp.getWriter().println("     <input type='hidden' name=\"un\" value=\"" + userService.getCurrentUser().getEmail() + "\">");
		resp.getWriter().println("     <input type='hidden' name=\"pw\" value=\"" + token + "\">");
		resp.getWriter().println("     <input type='hidden' name=\"loginURL\" value=\"https://mark-sivill-sfdc.appspot.com/?salesforceloginurl=" + URLEncoder.encode(salesforceLoginURL,"UTF-8") + "\">");
		resp.getWriter().println("</form>");
		resp.getWriter().println("<script language=\"JavaScript\">");
		resp.getWriter().println("document.form1.submit();");
		resp.getWriter().println("</script>");
		resp.getWriter().println("</body>");
		resp.getWriter().println("</html>");

	}

}
