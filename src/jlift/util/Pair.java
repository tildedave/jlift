package jlift.util;

public class Pair<U,V> {

	protected U first;
	protected V second;

	public Pair(U first, V second) {
		this.first = first;
		this.second = second;
	}

	public U getFirst() {
		return first;
	}

	public V getSecond() {
		return second;
	}
}
