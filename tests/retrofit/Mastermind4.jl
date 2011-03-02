public class Mastermind4
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
	
	if (aliceGuess.length == code.length) {  
	    // alice better guess something with the same length as bob! 
	    
	    returnArray = new int[aliceGuess.length]; 
	    int k = 0;

	    int[] usedToIndicateCorrectColor;
	    
	    for(int j = 0; j < code.length; ++j) {  
		// for each element, see if Alice guessed the right color and/or position 
		int guessResult = -1;
		if (aliceGuess[j] == code[j]) {
		    usedToIndicateCorrectColor[j] = 1;
		    guessResult = CORRECT_COLOR_AND_POSITION;
		}
		else {
		    for(int i = 0; i < code.length; ++i) {
			if (aliceGuess[i] == code[j] &&
			    usedToIndicateCorrectColor[i] != 1) {
			    usedToIndicateCorrectColor[i] = 1;
			    guessResult = CORRECT_COLOR;
			}
		    }
		}
		if (guessResult != -1) {
		    returnArray[k] = guessResult;
		    ++k;
		}
	    } 
	    ++numGuesses;
	}

	return returnArray;
    } 
}