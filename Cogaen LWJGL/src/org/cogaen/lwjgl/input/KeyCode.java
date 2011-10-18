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

	public static final int KEY_1 = 2;
	public static final int KEY_2 = 3;
	public static final int KEY_3 = 4;
	public static final int KEY_4 = 5;
	public static final int KEY_5 = 6;
	public static final int KEY_6 = 7;
	public static final int KEY_7 = 8;
	public static final int KEY_8 = 9;
	public static final int KEY_9 = 10;
	public static final int KEY_0 = 11;
	public static final int KEY_PERIOD = 52;
	
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
	public static final int KEY_F3 = 61;
	public static final int KEY_F4 = 62;
	public static final int KEY_F5 = 63;
	public static final int KEY_F6 = 64;
	public static final int KEY_F7 = 65;
	public static final int KEY_F8 = 66;
	public static final int KEY_F9 = 67;
	public static final int KEY_F10 = 68;	
	public static final int KEY_F11 = 87;	
	public static final int KEY_F12 = 88;	
	
	
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
		case KEY_F3:
		case KEY_F4:
		case KEY_F5:
		case KEY_F6:
		case KEY_F7:
		case KEY_F8:
		case KEY_F9:
		case KEY_F10:
		case KEY_F11:
		case KEY_F12:
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

		case KEY_1:
			return '1';

		case KEY_2:
			return '2';
			
		case KEY_3:
			return '3';
			
		case KEY_4:
			return '4';
			
		case KEY_5:
			return '5';
			
		case KEY_6:
			return '6';
			
		case KEY_7:
			return '7';

		case KEY_8:
			return '8';

		case KEY_9:
			return '9';

		case KEY_0:
			return '0';
			
		case KEY_BACKSLASH:
			return '/';
			
		case KEY_PERIOD:
			return '.';
		}
		
		return 0;
	}
}
