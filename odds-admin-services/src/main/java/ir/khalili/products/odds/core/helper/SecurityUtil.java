package ir.khalili.products.odds.core.helper;

import com.googlecode.gwt.crypto.bouncycastle.DataLengthException;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;

import ir.khalili.products.odds.core.excp.util.EXCPSEC_UnableToDecrypt;

public class SecurityUtil {

	public final		static		byte[]		agentKey		= SecurityUtil.generateKey("aGt", "nas!2#");
	public final		static		byte[]		customerKey		= SecurityUtil.generateKey("Cus", "2!3N@s");
	
	public static byte[] generateKey(String mainKey, String complementaryKey) {
		byte[] GWT_DES_KEY = new byte[] { (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1,
				(byte) 1, (byte) 33, (byte) 76, (byte) 1, (byte) 120, (byte) 7, (byte) 3, (byte) 1, (byte) 93, (byte) 1,
				(byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, };

		// ----------------------------------------------------------
		// ----------------------------------------------- Validation
		// ----------------------------------------------------------
		if (mainKey == null || mainKey.isEmpty())
			return null;
		else {
			if (complementaryKey == null || complementaryKey.isEmpty())
				return null;
			else {
				try {
					char[] captchCharacters = mainKey.toCharArray();
					char[] CCPLUTOSTRING = complementaryKey.toCharArray();
					for (int i = 0; i < captchCharacters.length; i++) {
						GWT_DES_KEY[i] = (byte) captchCharacters[i];
					}
					for (int i = 0; i < CCPLUTOSTRING.length; i++) {
						GWT_DES_KEY[23 - i] = (byte) CCPLUTOSTRING[i];
					}

					return GWT_DES_KEY;
				} catch (Exception e) {
					return null;
				}
			}

		}
	}

	public static String decrypt(String value, byte[] myKey) throws EXCPSEC_UnableToDecrypt {
		String result = null;
		TripleDesCipher cipher = new TripleDesCipher();
		cipher.setKey(myKey);
		try {
			result = cipher.decrypt(value);
		} catch (Exception e) {
			throw new EXCPSEC_UnableToDecrypt();
		}
		return result;

	}

	public static String encrypt(String value, byte[] myKey) {
		String result = null;
		TripleDesCipher cipher = new TripleDesCipher();
		cipher.setKey(myKey);
		try {
			result = cipher.encrypt(value);
		} catch (DataLengthException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (InvalidCipherTextException e) {
			e.printStackTrace();
		}
		return result;

	}

	public static void main(String[] args) {

	}

}
