package com.yeah.bztools.net;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
public class MD5Util {
   public static String md5Encode(String inStr){
       MessageDigest md5 = null;
       StringBuffer hexValue = new StringBuffer();
       try {
           md5 = MessageDigest.getInstance("MD5");
       
	       byte[] byteArray = inStr.getBytes("UTF-8");
	       byte[] md5Bytes = md5.digest(byteArray);
	       hexValue = new StringBuffer();
	       for (int i = 0; i < md5Bytes.length; i++) {
	           int val = ((int) md5Bytes[i]) & 0xff;
	           if (val < 16) {
	               hexValue.append("0");
	           }
	           hexValue.append(Integer.toHexString(val));
	       }
       } 
       catch (UnsupportedEncodingException ue){
    	   System.out.println(ue.getMessage()+"");
    	   ue.printStackTrace();
       }
       catch (Exception e) {
           System.out.println(e.toString());
           e.printStackTrace();
           return "";
       }
       return hexValue.toString();
   }
   public static void main(String args[]) throws Exception {
       String str = new String("123456");
       System.out.println("原始：" + str);
       System.out.println("MD5后：" + md5Encode(str));
   }
}