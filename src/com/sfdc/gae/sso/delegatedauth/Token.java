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
