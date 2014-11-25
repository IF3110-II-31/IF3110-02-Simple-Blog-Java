package com.github.if3110_31.simple_blog;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SHA512 {
	private static String convertByteToHex(byte[] data) {
		StringBuffer buffer = new StringBuffer();
		for(int byteIdx = 0; byteIdx < data.length; byteIdx++) {
			buffer.append(Integer.toString((data[byteIdx] & 0xff) + 0x100, 16).substring(1));
		}
		
		return buffer.toString();
	}
	
	public static String hashText(String textToHash) {
		try {
			final MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
			
			sha512.update(textToHash.getBytes());
			return convertByteToHex(textToHash.getBytes());
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
}
