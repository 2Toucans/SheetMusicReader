package com.twotoucans;

public class Note {
	int position;
	int modification;
	int lenNumerator;
	int lenDenominator;
	public Note(int pos, int mod, int lenNum, int lenDen) {
		position = pos;
		modification = mod;
		lenNumerator = lenNum;
		lenDenominator = lenDen;
	}
	public int getPosition() {
		return position;
	}
	public int getModification() {
		return modification;
	}
	public int getNumerator() {
		return lenNumerator;
	}
	public int getDenominator() {
		return lenDenominator;
	}
	public static String getNoteName(int note) {
		switch((note % 7 + 7) % 7) {
		case 0:
			return "B";
		case 1:
			return "C";
		case 2:
			return "D";
		case 3:
			return "E";
		case 4:
			return "F";
		case 5:
			return "G";
		case 6:
			return "A";
		}
		return "Dunno";
	}
}
