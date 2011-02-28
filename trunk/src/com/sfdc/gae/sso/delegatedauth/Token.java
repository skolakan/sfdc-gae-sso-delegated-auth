package com.sfdc.gae.sso.delegatedauth;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

public class Token
{

	// change secret value before installing into Google App Engine
	private static String secretKeyword = "your_secret_keyword";
	
	private static String seperator = "#";
	private static String dateFormat = "yyyyMMddHHmm";
	
	public static String generateToken(String username, String ipAddress) throws Exception
	{
		return Token.generateDigest( username, ipAddress, Token.formatDate(new Date()));
	}

	public static String formatDate(Date date)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat); 
		return simpleDateFormat.format(date);
	}

    public static String generateDigest(String username, String ipAddress, String dateNow) throws Exception
    {
		String token = username + seperator + ipAddress + seperator + dateNow + seperator + secretKeyword;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(token.getBytes());
 
        byte byteData[] = md.digest();
  
        //convert the byte to hex format
        StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<byteData.length;i++) {
    		String hex=Integer.toHexString(0xff & byteData[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
    	
    	return hexString.toString();
    }

	public static boolean checkToken(String username, String ipAddress, String token)
	{

		Logger log = Logger.getLogger(Token.class.getName());

		Date dateNow = new Date();
		
		try
		{
			// check current minute
			if (Token.generateDigest( username, ipAddress, Token.formatDate(dateNow)).equals(token) )
			{	
				log.info("Token match on current minute");
				return true;
			}

			// check if last minute
			GregorianCalendar gc = new java.util.GregorianCalendar();
			gc.setTime(dateNow);
			gc.add(java.util.GregorianCalendar.MINUTE, -1);

			if (Token.generateDigest( username, ipAddress, Token.formatDate(gc.getTime())).equals(token) )
			{
				log.info("Token match on last minute");
				return true;
			}

		}
		catch(Exception e)
		{
			log.info("Error checking token");
			return false;
		}
		
		return false;
	}

}
