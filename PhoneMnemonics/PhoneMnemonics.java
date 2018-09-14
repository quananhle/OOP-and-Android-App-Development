import java.io.*;
import java.util.*;

public class PhoneMnemonics extends PhoneMnemonicsException {

	public static void recursiveMnemonics(Vector<String> result, String mnemonic, String leftDigit) {
		if(leftDigit.length() == 0) {
			result.add(mnemonic);
			return;
		}
		String letters = digitLetters(leftDigit.charAt(0));
		leftDigit = leftDigit.substring(1);
		for(int i = 0; i < letters.length(); i++) {
			mnemonic = mnemonic + letters.charAt(i);
			recursiveMnemonics(result, mnemonic, leftDigit);
			mnemonic = mnemonic.substring(0,mnemonic.length()-1);
		}
	}
	public static Vector<String> listMnemonics(String number) {
		Vector<String> result = new Vector<String>();
		recursiveMnemonics(result, "", number); 
		return result;
	}

	public static String digitLetters(char c) {
		String str = "";
		switch (c) {
		case '2': str = "ABC";
		break;
		case '3': str = "DEF";
		break;
		case '4': str = "GHI";
		break;
		case '5': str = "JKL";
		break;         
		case '6': str = "MNO";
		break;         
		case '7': str = "PQRS";
		break;
		case '8': str = "TUV";
		break;         
		case '9': str = "WXYZ";
		break;         
		}
		return str;
	}
	public static void main(String[] args) throws IOException {
		Vector<String> list = listMnemonics("234");
		Vector<String> list1 = listMnemonics("456");
		Vector<String> list2 = listMnemonics("678");
		Vector<String> list3 = listMnemonics("789");
		System.out.println(list);
		System.out.println(list1);	
		System.out.println(list2);	
		System.out.println(list3);	
		
		while (true) {
			try {
				System.out.println("Please enter a three-digit number: ");
				Scanner sc = new Scanner (System.in);
				String num = sc.nextLine();
				
				if (num.length() <= 3) {
					Vector<String> list5 = listMnemonics(num);
					System.out.println(list5);
				}
				else {
					throw new IOException ("Wrong input! Please enter a 3 digit number");
				}
			}
			catch (NumberFormatException nfe) {
				System.out.println("Please enter a valid input");
				System.exit(0);
			}
			catch (IOException ie) {
				System.out.println("Wrong input! Please enter a 3 digit number");
				System.exit(0);
			}
		}
	}
}