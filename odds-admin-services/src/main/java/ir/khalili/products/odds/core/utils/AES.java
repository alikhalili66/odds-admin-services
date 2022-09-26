package ir.khalili.products.odds.core.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {

	private static SecretKeySpec secretKey;
	private static byte[] key;

	private static void setKey(String myKey) {
		MessageDigest sha = null;
		try {
			key = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static String encrypt(String strToEncrypt, String secret) {
		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}
		return null;
	}

	public static String decrypt(String strToDecrypt, String secret) {
		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
		}
		return null;
	}

	public static void main(String[] args) {
//		final String secretKey = "NAS";

////		String originalString = "1234567890123456";
//		String originalString = "123456";
//		String encryptedString = AES.encrypt(originalString, secretKey);
//		String decryptedString = AES.decrypt(encryptedString, secretKey);
//
////        Tg2Nn7wUZOQ6Xc+1lenkZTQ9ZDf9a2/RBRiqJBCIX6o=
////        Tg2Nn7wUZOQ6Xc+1lenkZTQ9ZDf9a2/RBRiqJBCIX6o=     
////        iLfDVCQrCa7gu3QF3aeQgEN5kOqBg3V/8SXbcwxQgag=
//		System.out.println(originalString);
//		System.out.println(encryptedString);
//		System.out.println(decryptedString);
		
//		System.out.println(secretKey.ha);

	}
}
