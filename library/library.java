package library.client.classes;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.*;
import library.service.classes.BookGenre;
import library.service.classes.BookRecord;

class library{
	ArrayList <BookRecord> books;
	public void swap (ArrayList<BookRecord> books, int i, int j) {
		BookRecord temp = books.get(i);
		books.set(i, books.get(j));
		books.set(j, temp);
	}
	
	public ArrayList<BookRecord> sortString(ArrayList<BookRecord> myArray){//sort the array based on strings		
		int insertPt;
		int maxIndex;
		for(int i = myArray.size() - 1; i >= 1; --i){
			insertPt = i;
			maxIndex = 0;
			
			for(int j = 1; j <= i; ++j){
				//find the index with the max value in this sub array
				if(myArray.get(maxIndex).getTag().compareTo(myArray.get(j).getTag())<0){
					maxIndex = j;					
				}
			}
			//swap values
			swap(myArray, insertPt, maxIndex);
		}
		return myArray;
	}
	
	public ArrayList<BookRecord> sortPages(ArrayList<BookRecord> myArray){//sort the array based on pages
		int insertPt;
		int maxIndex;
		for(int i = myArray.size()-1; i>=1; --i){
			insertPt = i;
			maxIndex = 0;
			
			for(int j = 1; j <= i; ++j){
				//find the index with the max value in this sub array
				if(myArray.get(maxIndex).getPages() < myArray.get(j).getPages()){
					maxIndex = j;					
				}
			}
			//swap values
			swap(myArray, insertPt, maxIndex);
		}
		return myArray;
	}
	
	public void searchByGenre(BookGenre genre){
		//loop through the book records and find the number of matches
		ArrayList <BookRecord> matchingBook = new ArrayList<>();
		for (BookRecord book : books ){
			if(book.getGenre() == genre) {
				matchingBook.add(book);
				System.out.println(book);
			}
		}
		if(matchingBook.size() == 0){
			System.out.println("No books of this genre found");
			return;
		}
		//sort the results according to the page length
		this.sortPages(matchingBook);
		//print out the results
		ArrayList <BookRecord> searchResults = new ArrayList<>(matchingBook);
		for(int i = 0; i < searchResults.size(); ++i){
			System.out.println(searchResults.get(i).toString());
		}
	}
	
	public void searchTag(String tag){
		//implement the binary search
		if(books.size() == 0){
			System.out.println("The library database is empty");
			return;
		}
		
		int first = 0;
		int end = books.size() - 1;
		int mid = (first + end) / 2;
		while(first <= end){
			if(books.get(mid).getTag().compareTo(tag) == 0){
				System.out.println("Found a match");
				System.out.println(books.get(mid).toString());
				return;
			} else if(books.get(mid).getTag().compareTo(tag) < 0){ //look at the right half
				first = mid + 1;
				mid = (first + end) / 2;
			} else if(books.get(mid).getTag().compareTo(tag) > 0){ //look at the left half
				end = mid - 1;
				mid = (first + end) / 2;
			}			
		}
		System.out.println("No match found");
	}
	
	public boolean searchForDuplicate(BookRecord aRecord){
		//loop through the library and find duplicates
		//return true if duplicate found 
		//else return false
		if(books.size() == 0) { 
			return false;
		}
		
		for(int i = 0; i < books.size(); ++i){
			if(books.get(i).equals(aRecord)) {
				return true;
			}
		}
		return false;
	}
	
	public void print(){//list the library
		for(int i = 0; i < books.size(); ++i){
			System.out.println(books.get(i).toString());
		}	
	}
	
	library(){
		this.books=new ArrayList <BookRecord>();
	}
	
	public static void main(String []args){//instantiate the library
	//arg[0]: text file //arg[1]: resize factor
		int resizeFactor = Integer.parseInt(args[1]);
		library myLib = new library();
		//read the the files from text files
		String []authors;
		BookRecord aRecord;
		Scanner scan;
		String str;
		try {
			File myFile=new File(args[0]);
            scan=new Scanner(myFile);//each line has the format title:genre:author-name-1,author-name-2..authorname-m
			while(scan.hasNextLine()){
				str=scan.nextLine();			
				String []tok=str.split(":");
				authors=tok[2].split(",");
				aRecord = new BookRecord(tok[0],tok[1],authors,tok[3],Integer.parseInt(tok[4]));
				//check for duplicate records
				if (!myLib.searchForDuplicate(aRecord)){
					//create a BookRecord object and load it on the array
					myLib.books.add(aRecord);
					//System.out.println("No of records: " + myLib.noRecords);
				}
				else{
					System.out.println("Found a duplicate");
					String out="";
					out = out + "===================================\n";
					out = out + "Tag:" + aRecord.getTag() + "\n";
					out = out + "Title:" + aRecord.getTitle() + "\n";
					out = out + "Genre: " + aRecord.getGenre() + "\n";
					out = out + "Authors: " + aRecord.getAuthorList() + "\n";
					out = out + "No. of Pages: " + aRecord.getPages() + "\n";
					out = out + "===================================\n";
					System.out.println(out);
				}
			}
			scan.close();
        }catch(IOException ioe){ 
			System.out.println("The file can not be read");
		}
		//sort the array
		myLib.sortString(myLib.books);
		
		//User interactive part
		String option1, option2;
		scan = new Scanner(System.in);
		while(true){
			System.out.println("Select an option:");
			System.out.println("Type \"S\" to list books of a genre");
			System.out.println("Type \"P\" to print out all the book records");		
			System.out.println("Type \"T\" to search for a record with a specific tag");
			System.out.println("Type \"Q\" to Quit");
			option1=scan.nextLine();
			
			switch (option1) {
				case "S":	System.out.println("Type a genre. The genres are:");
							for (BookGenre d : BookGenre.values()) {
									System.out.println(d);
							}
							option2=scan.nextLine(); //assume the use will type in a valid genre
							myLib.searchByGenre(BookGenre.valueOf(option2));
							break;
							 
				case "P":   myLib.print();	 
							break;
				
				case "Q":   System.out.println("Quitting program");
							System.exit(0);
							
				case "T":	System.out.println("Input the tag of the book you want to search for:");
							option2=scan.nextLine(); //assume the use will type in a valid tag
							myLib.searchTag(option2);
							break;
							
				default:	System.out.println("Wrong option! Try again");
							break;
			
			}
			
		}
			 
	}
	
}