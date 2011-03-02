public class Mastermind 
{
    int{Bob:}[]{Bob:} code;                // bob's secret code
    int numGuesses = 0;                    // the number of guesses Alice has made
    
    // some enums to specify the response to Alice's guess
    final int NOT_CORRECT = 0;
    final int CORRECT_COLOR = 1; 
    final int CORRECT_COLOR_AND_POSITION = 2; 
    
    public int{Alice:}[]{Alice:} guess{}(int{Alice:}[]{Alice:} aliceGuess) {
	// return an array to Alice with information on Alice's guesses 
	
	int{Alice:}[]{Alice:} returnArray = null; 
	
	try {
	    if (aliceGuess.length == code.length) {  
		// alice better guess something with the same length as bob! 
		
		returnArray = new int[aliceGuess.length]; 
		
		for(int j = 0; j < code.length; ++j) {  
		    // for each element, see if Alice guessed the right color and/or position 
		    int guessResult = getGuessResult(aliceGuess, code[j], j); 
		    returnArray[j] = guessResult;
		} 
		++numGuesses;
	    }
	} catch (Exception e) { }

	return returnArray;
    } 
    
    public int getGuessResult(int[] guess, int correctColor, int position) throws RuntimeException {
	if (guess[position] == correctColor) {  
	    return CORRECT_COLOR_AND_POSITION;
	} 
	else { 
	    // see if the guess has 'correctColor' somewhere in it 
            for(int i = 0; i < guess.length; ++i) {  
		if (guess[i] == correctColor) {  
		    guess[i] = -1;
		    return CORRECT_COLOR;
		}
	    } 
	    return NOT_CORRECT;
	}
    }
}