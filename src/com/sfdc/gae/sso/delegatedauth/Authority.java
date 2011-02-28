package com.sfdc.gae.sso.delegatedauth;

//Delegated Authentication Authority

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")

public class Authority extends HttpServlet
{

	// process soap authentication request
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{

		Logger log = Logger.getLogger(Authority.class.getName());

		log.info("Request toString <" + req.toString() + ">");
		log.info("Request content length <" + req.getContentLength() + ">");
		
		// get soap message
		String data=null;
		InputStream is = req.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		int letti;
		while ((letti = is.read(buf)) > 0)
		{
			baos.write(buf, 0, letti);	
		}
		data = new String(baos.toByteArray()); 

		log.info("Request payload <" + data + ">");
		
		String username = "";
		String token = "";
		String sourceIp = "";
		Pattern pattern;
		Matcher matcher;
		
		// find google username
		pattern = Pattern.compile("<username>([^<]*)</username>");
		matcher = pattern.matcher(data);
		
		// should only return one
		while (matcher.find())
		{
			username = data.substring((matcher.start()+10), (matcher.end()-11));
			log.info("Using google username <" + username + ">");
		}

		pattern = Pattern.compile("<password>([^<]*)</password>");
		matcher = pattern.matcher(data);
		
		// should only return one
		while (matcher.find()) {
			token = data.substring((matcher.start()+10), (matcher.end()-11));
			log.info("Using token <" + token + ">");
		}

		pattern = Pattern.compile("<sourceIp>([^<]*)</sourceIp>");
		matcher = pattern.matcher(data);
		
		// should only return one
		while (matcher.find()) {
			sourceIp = data.substring((matcher.start()+10), (matcher.end()-11));
			log.info("Using sourceIp <" + sourceIp + ">");
		}

	    resp.setContentType("text/xml");
	    
	    String response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> ";
	    response = response + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"; 
	    response = response + "<soapenv:Body>";
	    response = response + " <AuthenticateResponse ";
	    response = response + "xmlns=\"urn:authentication.soap.sforce.com\"> ";
	    
	    // is token valid
		if (Token.checkToken(username, sourceIp, token) == true)
		{
			response = response + "  <Authenticated>true</Authenticated> ";			
		}
		else
		{
			response = response + "  <Authenticated>false</Authenticated> ";			
		}
		response = response + " </AuthenticateResponse> ";
		response = response + "</soapenv:Body> ";
		response = response + "</soapenv:Envelope>";

		log.info("Response <" + response + ">");

		resp.getWriter().println(response);
		
	}

	// return error page as token should only be passed in a HTML POST
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{

	    resp.setContentType("text/html");
		resp.getWriter().println("<html>");
		resp.getWriter().println("<head><title>Error calling page</title></head>");
		resp.getWriter().println("<body>");
		resp.getWriter().println("<h1>Error calling page<h1>");
		resp.getWriter().println("</body>");
		resp.getWriter().println("</html>");
		
	}

}
