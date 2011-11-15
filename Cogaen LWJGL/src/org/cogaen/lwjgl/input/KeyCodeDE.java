package org.cogaen.lwjgl.input;

public class KeyCodeDE extends KeyCode {
	
	@Override
	public char getChar(int keyCode, boolean upperCase) {
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
			return upperCase ? '!' : '1';

		case KEY_2:
			return upperCase ? '"' : '2';
			
		case KEY_3:
			return upperCase ? '¤' : '3';
			
		case KEY_4:
			return upperCase ? '$' : '4';
			
		case KEY_5:
			return upperCase ? '%' : '5';
			
		case KEY_6:
			return upperCase ? '&' : '6';
			
		case KEY_7:
			return upperCase ? '/' : '7';

		case KEY_8:
			return upperCase ? '(' : '8';

		case KEY_9:
			return upperCase ? ')' : '9';

		case KEY_0:
			return upperCase ? '=' : '0';
			
		case KEY_BACKSLASH:
			return '/';
			
		case KEY_PERIOD:
			return '.';
		}
		
		return 0;
	}

}
