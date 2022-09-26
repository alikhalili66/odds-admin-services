package ir.khalili.products.odds.core.utils;

public class NationalNumberChecker {

    public static boolean isValid(String str)
    {
        try {
        	
        	if(null == str) {
        		return false;
        	}
        	
        	if(!isValidNotZeroNumber(str, 8, 12)) {
        		return false;
        	}
        	
//        	if(str.length() == 12) {
//        		return true;
//        	}
        	
            return genNinCheckDigit(strLeft(str, str.length() - 1)).equalsIgnoreCase(strRight(str, 1));	
		} catch (Exception e) {
			return false;
		}
    }

    private static boolean isValidNotZeroNumber(String str, int minLen, int maxLen)
    {
        if(isValidNumber(str, maxLen))
        {
            if(str.length() < minLen)
                return false;
            else
                return !str.matches("0*");
        } else
        {
            return false;
        }
    }

    private static String genNinCheckDigit(String str)
        throws Exception
    {
        if(!isValidNotZeroNumber(str, 7, 9))
            throw new Exception("errInvalidNin");
        str = strRight("00".concat(str), 9);
        int sum = 0;
        for(int i = 0; i < 9; i++)
            sum += Integer.parseInt(strMid(str, i, 1)) * (10 - i);

        int checkDigit = sum % 11;
        if(checkDigit > 1)
            checkDigit = 11 - checkDigit;
        return Integer.toString(checkDigit);
    }

    private static boolean isValidNumber(String str, int maxLen)
    {
        if(str == null || str.length() == 0 || str.length() > maxLen)
            return false;
        else
            return str.matches("[0-9]*");
    }

    private static String strLeft(String str, int len)
    {
        if(str == null)
            return null;
        if(len <= 0)
            return "";
        if(str.length() <= len)
            return str;
        else
            return str.substring(0, len);
    }

    private static String strRight(String str, int len)
    {
        if(str == null)
            return null;
        if(len <= 0)
            return "";
        if(str.length() <= len)
            return str;
        else
            return str.substring(str.length() - len, str.length());
    }

    private static String strMid(String str, int pos)
    {
        if(str == null)
            return null;
        if(pos < 0 || pos >= str.length())
            return "";
        else
            return str.substring(pos, str.length());
    }

    private static String strMid(String str, int pos, int len)
    {
        if(str == null)
            return null;
        if(pos < 0 || pos >= str.length() || len <= 0)
            return "";
        if(str.length() <= pos + len)
            return strMid(str, pos);
        else
            return str.substring(pos, pos + len);
    }
    
    public static void main(String[] args) {
		System.out.println(isValid("0075202655"));
	}
}