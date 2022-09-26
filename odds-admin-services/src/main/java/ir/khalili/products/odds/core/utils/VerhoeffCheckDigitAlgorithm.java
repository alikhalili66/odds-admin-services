package ir.khalili.products.odds.core.utils;

public class VerhoeffCheckDigitAlgorithm {
	
	static int[][] multiplication = new int[][]
    {
        {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, 
        {1, 2, 3, 4, 0, 6, 7, 8, 9, 5}, 
        {2, 3, 4, 0, 1, 7, 8, 9, 5, 6}, 
        {3, 4, 0, 1, 2, 8, 9, 5, 6, 7}, 
        {4, 0, 1, 2, 3, 9, 5, 6, 7, 8}, 
        {5, 9, 8, 7, 6, 0, 4, 3, 2, 1}, 
        {6, 5, 9, 8, 7, 1, 0, 4, 3, 2}, 
        {7, 6, 5, 9, 8, 2, 1, 0, 4, 3}, 
        {8, 7, 6, 5, 9, 3, 2, 1, 0, 4}, 
        {9, 8, 7, 6, 5, 4, 3, 2, 1, 0}
    };
	
    static int[][] permutation = new int[][]
    {
        {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, 
        {1, 5, 7, 6, 2, 8, 3, 0, 9, 4}, 
        {5, 8, 0, 3, 7, 9, 6, 1, 4, 2}, 
        {8, 9, 1, 6, 0, 4, 3, 5, 2, 7}, 
        {9, 4, 5, 3, 1, 2, 6, 8, 7, 0}, 
        {4, 2, 8, 6, 5, 7, 3, 9, 0, 1}, 
        {2, 7, 9, 3, 8, 0, 6, 4, 1, 5}, 
        {7, 0, 4, 6, 9, 1, 3, 2, 5, 8}
    };
	
    static int[] inv = {0, 4, 3, 2, 1, 5, 6, 7, 8, 9};
	
    
    
	public static String generateVerhoeff(String num){
			
		int result = 0;
		int[] myArray = stringToReversedIntArray(num);
		
		for(int i = 0; i < myArray.length; i++)
		{
			result = multiplication[result][permutation[((i + 1) % 8)] [myArray[i]]];
		}
		
		return Integer.toString(inv[result]);
	}
	
	
	
	public static boolean validateVerhoeff(String num){
		
        	int result = 0;
        	int[] myArray = stringToReversedIntArray(num);
        
        	for (int i = 0; i < myArray.length; i++)
        	{
        		result = multiplication[result][permutation[(i % 8)][myArray[i]]];
        	}
		
		return (result == 0);
	}
	
	

	private static int[] stringToReversedIntArray(String num){
		
		int[] myArray = new int[num.length()];
		
		for(int i = 0; i < num.length(); i++)
		{
			myArray[i] = Integer.parseInt(num.substring(i, i + 1));					
		}
		
		myArray = reverse(myArray);
		
		return myArray;
	
	}
	
	private static int[] reverse(int[] myArray)
	{
		int[] reversed = new int[myArray.length];
		
		for(int i = 0; i < myArray.length ; i++)
		{
			reversed[i] = myArray[myArray.length - (i + 1)];			
		}
				
		return reversed;
	}
	

    public static void main( String args[] ) {

		int code = Helper.generateRandomNumber();
//		Random rand = new Random(1000);
//		rand.nextInt(bound)
		
		
		String text = String.valueOf(code)+VerhoeffCheckDigitAlgorithm.generateVerhoeff(String.valueOf(code));
		System.out.println(text);
		System.out.println(validateVerhoeff(text));
    }


	

}
