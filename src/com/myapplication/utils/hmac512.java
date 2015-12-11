package com.myapplication.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class hmac512 {
	
	private static volatile String hmachHash;
//	private static volatile String hmacSecret_;
	private static volatile String strEncoded;
	private static volatile String secret_;
	private static volatile String message_;

	public static String getHmac512(String user, String pswrd, String deviceId, String time) {
	  
    	secret_ = hmacSecret(user, pswrd); 

    try {

	    message_ = "/login/HelloWorld:user="+user+"&pswrd="+pswrd+"&deviceId="+deviceId+":"+time;

     Mac sha256_HMAC = Mac.getInstance("HmacSHA512");
     SecretKeySpec secret_key = new SecretKeySpec(secret_.getBytes(), "HmacSHA512");
     sha256_HMAC.init(secret_key);

//     hmachHash = Base64.encodeBase64String(sha256_HMAC.doFinal(message_.getBytes()));
     hmachHash = new String(Base64.encodeBase64(sha256_HMAC.doFinal(message_.getBytes())));

    	}
    
    	catch (Exception e){
    		System.out.println("1 Error");
    	}
    
    return hmachHash;
    
   }
	
	  private static String hmacSecret(String message, String secret) {
		  
		    try {

		     Mac sha256_HMAC = Mac.getInstance("HmacSHA512");
		     SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA512");
		     sha256_HMAC.init(secret_key);

//		     hmacSecret_ = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
		     strEncoded = new String(Base64.encodeBase64(sha256_HMAC.doFinal(message.getBytes())));

		     
		    }
		    catch (Exception e){
		     System.out.println("2 Error");
		    }
		    
		    return strEncoded;
		    
		   }
	  
}
