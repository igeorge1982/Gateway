package com.myapplication.utils;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import com.myapplication.utils.hmac512;


public class test {
  public static void main(String[] args) {
//	     String message =  /login/HelloWorld:user=GG&pswrd=52fa80662e64c128f8389c9ea6c73d4c02368004bf4463491900d11aaadca39d47de1b01361f207c512cfa79f0f92c3395c67ff7928e3f5ce3e3c852b392f976&deviceId=7501011153736470252680537365900144024:1449872108237

//	     String message =  /login/HelloWorld:user=GG&pswrd=52fa80662e64c128f8389c9ea6c73d4c02368004bf4463491900d11aaadca39d47de1b01361f207c512cfa79f0f92c3395c67ff7928e3f5ce3e3c852b392f976&deviceId=7501011153736470252680537365900144024:1449872108237

//	     String message = "/login/HelloWorld:user=GG&pswrd=52fa80662e64c128f8389c9ea6c73d4c02368004bf4463491900d11aaadca39d47de1b01361f207c512cfa79f0f92c3395c67ff7928e3f5ce3e3c852b392f976&deviceId=7501011153736470252680537365900144024:1449872108237";

		String hmacHash = hmac512.getHmac512("GG", "52fa80662e64c128f8389c9ea6c73d4c02368004bf4463491900d11aaadca39d47de1b01361f207c512cfa79f0f92c3395c67ff7928e3f5ce3e3c852b392f976", "CCDCE176-B003-483E-9610-24F3D81E5143", "1451341524441");
		System.out.println(hmacHash);

	  
   }
}
