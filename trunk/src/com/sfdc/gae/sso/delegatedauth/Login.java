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
