public class Mastermind 
{
    int{Bob:}[]{Bob:} code;                // bob's secret code
    int{} numGuesses = 0;  // the number of guesses Alice has made
    
    // some enums to specify the response to Alice's guess
    final int NOT_CORRECT = 0;
    final int CORRECT_COLOR = 1; 
    final int CORRECT_COLOR_AND_POSITION = 2; 
    
    public int{Alice:}[]{Alice:} guess{}(int{Alice:}[]{Alice:} aliceGuess) {
	// return an array to Alice with information on Alice's guesses 
	
	int{Alice:}[]{Alice:} returnArray = null; 

	int guessLength = (aliceGuess != null) ? aliceGuess.length : -1;
	int codeLength = -1;
	try {
	    codeLength = declassify(code.length,{Alice:});
	}
	catch (NullPointerException e) { }
	    
	
	if ((guessLength == codeLength) && (guessLength > 0)) {  
	    // alice better guess something with the same length as the code! 
	    
	    returnArray = new int[guessLength]; 
	    
	    for(int j = 0; j < guessLength; ++j) {  
		// for each element, see if Alice guessed the right color and/or position 
		int guessResult = NOT_CORRECT;
		try {
		    guessResult = declassify(getGuessResult(aliceGuess, code[j], j),{Alice:}); 
		} catch(ArrayIndexOutOfBoundsException impossible) { }
		catch (NullPointerException nullCode) { }
		
		try {
		    returnArray[j] = guessResult;
		} catch (ArrayIndexOutOfBoundsException impossible) { }
	    }
	    ++numGuesses;
	}

	return returnArray;
    } 
    
    public int getGuessResult(int[] guess, int correctColor, int position) throws ArrayIndexOutOfBoundsException {

	if (guess != null) {
	    int guessLength = guess.length;
	    
	    if (guess[position] == correctColor) {  
		return CORRECT_COLOR_AND_POSITION;
	    } 
	    else { 
		// see if the guess has 'correctColor' somewhere in it 
		for(int i = 0; i < guessLength; ++i) {  
		    if (guess[i] == correctColor) {  
			return CORRECT_COLOR;
		    }
		} 
	    }
	}
	return NOT_CORRECT;
    }
}