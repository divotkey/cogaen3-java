package org.cogaen.lwjgl.input;

public class KeyCode {
	public static final int KEY_ESC = 1;
	public static final int KEY_LEFT = 203;
	public static final int KEY_RIGHT = 205;
	public static final int KEY_UP = 200;
	public static final int KEY_DOWN = 208;
	
	public static final int KEY_MINUS = 12;
	public static final int KEY_ADD = 78;
	public static final int KEY_SUBTRACT = 74;
	public static final int KEY_A = 30;
	public static final int KEY_B = 48;
	public static final int KEY_C = 46;
	public static final int KEY_D = 32;
	public static final int KEY_E = 18;
	public static final int KEY_F = 33;
	public static final int KEY_G = 34;
	public static final int KEY_H = 35;
	public static final int KEY_I = 23;
	public static final int KEY_J = 36;
	public static final int KEY_K = 37;
	public static final int KEY_L = 38;
	public static final int KEY_M = 50;
	public static final int KEY_N = 49;

	public static final int KEY_O = 24;
	public static final int KEY_P = 25;
	public static final int KEY_Q = 16;
	public static final int KEY_R = 19;
	public static final int KEY_S = 31;
	public static final int KEY_T = 20;
	public static final int KEY_U = 22;
	public static final int KEY_V = 47;
	public static final int KEY_W = 17;
	public static final int KEY_X = 45;
	public static final int KEY_Y = 21;
	public static final int KEY_Z = 44;
	
	public static final int KEY_BACKSLASH = 43;
	
	
	public static final int KEY_SPACE = 57;
	public static final int KEY_LCONTROL = 29;
	public static final int KEY_RCONTROL = 157;
	public static final int KEY_BACK = 14;
	public static final int KEY_RETURN = 28;	
	public static final int KEY_LSHIFT = 42;	
	public static final int KEY_RSHIFT = 54;	
	public static final int KEY_F1 = 59;	
	public static final int KEY_F2 = 60;	
	
	
	public static boolean isPrintable(int keyCode) {
		switch (keyCode) {
		case KEY_LCONTROL:
		case KEY_RCONTROL:
		case KEY_BACK:
		case KEY_RETURN:
		case KEY_LSHIFT:
		case KEY_RSHIFT:
		case KEY_ESC:
		case KEY_LEFT:
		case KEY_RIGHT:
		case KEY_UP:
		case KEY_DOWN:
		case KEY_F1:
		case KEY_F2:
			return false;
		}
		
		return true;
	}
	
	public static char getChar(int keyCode, boolean upperCase) {
		switch (keyCode) {
		case KEY_SPACE:
			return ' ';
		
		case KEY_A:
			return upperCase ? 'A' : 'a';
			
		case KEY_B:
			return upperCase ? 'B' : 'b';
			
		case KEY_C:
			return upperCase ? 'C' : 'c';
			
		case KEY_D:
			return upperCase ? 'D' : 'd';
			
		case KEY_E:
			return upperCase ? 'E' : 'e';
			
		case KEY_F:
			return upperCase ? 'F' : 'f';
			
		case KEY_G:
			return upperCase ? 'G' : 'g';
			
		case KEY_H:
			return upperCase ? 'H' : 'h';
			
		case KEY_I:
			return upperCase ? 'I' : 'i';
			
		case KEY_J:
			return upperCase ? 'J' : 'j';
			
		case KEY_K:
			return upperCase ? 'K' : 'k';
			
		case KEY_L:
			return upperCase ? 'L' : 'l';
			
		case KEY_M:
			return upperCase ? 'M' : 'm';
			
		case KEY_N:
			return upperCase ? 'N' : 'n';
			
		case KEY_O:
			return upperCase ? 'O' : 'o';
			
		case KEY_P:
			return upperCase ? 'P' : 'p';
			
		case KEY_Q:
			return upperCase ? 'Q' : 'q';
			
		case KEY_R:
			return upperCase ? 'R' : 'r';
			
		case KEY_S:
			return upperCase ? 'S' : 's';
			
		case KEY_T:
			return upperCase ? 'T' : 't';
			
		case KEY_U:
			return upperCase ? 'U' : 'u';
			
		case KEY_V:
			return upperCase ? 'V' : 'v';
			
		case KEY_W:
			return upperCase ? 'W' : 'w';
			
		case KEY_X:
			return upperCase ? 'X' : 'x';
			
		case KEY_Y:
			return upperCase ? 'Y' : 'y';
			
		case KEY_Z:
			return upperCase ? 'Z' : 'z';
			
		case KEY_BACKSLASH:
			return '/';
		}
		
		return 0;
	}
}
