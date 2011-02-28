package com.sfdc.gae.sso.delegatedauth;

public class Utility {

	//
	// remove any potential cross-site scripting from incoming parameters 
	//
    public static String cleanXSS(String value) {  
         value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");  
         value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");  
         value = value.replaceAll("'", "& #39;");            
         value = value.replaceAll("eval\\((.*)\\)", "");  
         value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");  
         value = value.replaceAll("script", "");  
         return value;  
    }  

}
