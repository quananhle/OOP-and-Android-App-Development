package isPalindrome;

import java.util.Scanner;

public class Palindrome {
	public static boolean isPalindrome (String word) {
		//if there is only one character
		if (word.length() == 0 || word.length() == 1) {
			//then the string is a palindrome
			return true;
		}
		//scanning if there are spaces from the first character to the last
		if (word.charAt(0) == ' ' || word.charAt(word.length() - 1) == ' ') {
			//still check if the string is a palindrome and ignore the spaces
			return isPalindrome(word.substring(1, word.length() - 1));
		}
		//return the first character converted to uppercase
		char f = Character.toUpperCase(word.charAt(0));
		//return the last character converted to uppercase 
		char l = Character.toUpperCase(word.charAt(word.length() - 1));		
		//if the first letter is the same as the last letter, keep scanning until complete or condition fails 
		if (f == l) {
			//then check if the string is a palindrome 
			return isPalindrome(word.substring(1, word.length() - 1));
		}
		//string is not a palindrome hence return false. 
		return false;
	}
	public static void main (String[] args) {
		//user input
		while (true) {
			Scanner sc = new Scanner (System.in);
			System.out.print ("Enter a word: ");
			String str = sc.nextLine();
			//if function returns true then the string is palindrome
			if (isPalindrome(str) == true) {
				System.out.println (str + " is a palindrome");
			}
			//else not a palindrome
			else {
				System.out.println (str + " is not a palindrome");
			}
		}
	}
}