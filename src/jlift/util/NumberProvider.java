package jlift.util;

public class NumberProvider {

	static int currentNumber = 0;

	public static int getNextNumber() {
		++currentNumber;
		return currentNumber;
	}

}
