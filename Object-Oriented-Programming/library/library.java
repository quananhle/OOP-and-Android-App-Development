package library.client.classes;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import library.service.classes.BookGenre;
import library.service.classes.BookRecord;
import java.util.Vector;
class library{
	/*Task 1: Declare the vector of objects*/
	Vector<BookRecord> books;
	library(){
		/* Task 2: Initiliaze vector*/
		books=new Vector<BookRecord>();
		/* Task 3: Print out its capacity and its size*/
		System.out.println("Size of vector: " + books.size());
		System.out.println("Capacity of vector: " + books.capacity());
	}
	
	public void searchByGenre(BookGenre genre){//search the vector for records of specific genre
		/* Task 5: implement searchByGenre() to search for records of a particylar genre in the vector*/
		for(int i=0;i<books.size();++i){
			if(books.get(i).getGenre().equals(genre))
			{
				System.out.println(books.get(i).toString());
			}
		}
	}
	
	public void print(){//list the records
		/* Task 6: Implement the print() to list out the records*/
		for(int i=0;i<books.size();++i){
			System.out.println(books.get(i).toString());
		}
	}
	public int findNumGenre(BookGenre genre){
		/**/
		int count=0;
		for(int i=0;i<books.size();++i){
			if(this.books.get(i).getGenre()==genre){
				count++;
			}				
		}
		return count;
	}
		
	public static void main(String []args){//instantiate the library
	//arg[0]: text file
		
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
				aRecord = new BookRecord(tok[0],tok[1],authors);
				
				/*Task 4: Add the objects to the vector*/
				myLib.books.add(aRecord);
			}
			scan.close();
        }catch(IOException ioe){ 
			System.out.println("The file can not be read");
		}
		
		//User interactive part
		String option1, option2;
		scan = new Scanner(System.in);
		option1="";
		while(!option1.equals("Q")){
			System.out.println("Select an option:");
			System.out.println("Type \"S\" to list books of a genre");
			System.out.println("Type \"P\" to print out all the book recors");		
			System.out.println("Type \"Q\" to Quit");
			option1=scan.nextLine();
			
			switch (option1) {
				case "S":	System.out.println("Type a genre. The genres are:");
							for (BookGenre d : BookGenre.values()) {
									System.out.println(d);
							}
							option2=scan.nextLine(); //assume the user will type in a valid genre
							myLib.searchByGenre(BookGenre.valueOf(option2));//implement this method
							break;									
				
				case "P":   myLib.print();//print the array; implement this method	 
							break;
				
				case "Q":   System.out.println("Quitting interactive part");
							break;
							
				default:	System.out.println("Wrong option! Try again");
							break;
			
			}
		}
			/*Task 7- Create 2 dimensional array using the records from the vector
			The array has rows for GENRE_HISTORY, GENRE_SCIENCE, GENRE_ENGINEERING, GENRE_LITERATURE*/
			BookRecord [][] libAr; //declaration of the 2D array
			libAr = new BookRecord[4][];
			int cnt=0;
			int cnt1=0;
			for (BookGenre d : BookGenre.values()) {//for each genre count the number of records belonging to it and then intantiate its row
			/*add code here*/
					libAr[cnt]=new BookRecord[myLib.findNumGenre(d)];
					cnt1=0;
					for(int i=0;i<myLib.books.size();++i){
						if(myLib.books.get(i).getGenre()==d){
							libAr[cnt][cnt1++]=myLib.books.get(i);
						}
					}
					cnt++;					
			}
			System.out.println("Printing out the array. Type enter to proceed");
			option1=scan.nextLine();
			/*Task 8: Print out the array*/
			for(int i=0;i<libAr.length;++i){
				System.out.println("Printing Row: " + i);
				for(int j=0;j<libAr[i].length;++j){
					System.out.println(libAr[i][j].toString());
				}
			}
			System.out.println("Removing the duplicates. Type enter to proceed");
			option1=scan.nextLine();
			/* Task 9: Identify and remove the duplicate records IN THE VECTOR (NOT THE ARRAY) and print out the removed records */
			for(int i=0;i<myLib.books.size()-1;++i){
				for(int j=i+1;j<myLib.books.size();++j){
					if(myLib.books.get(i).equals(myLib.books.get(j))){
						aRecord=myLib.books.remove(j);
						System.out.println("Removing the record:");
						System.out.println(aRecord.toString());
					}
				}
			}
			
			System.out.println("Note the record nos. of the duplicated records");
			
			System.out.println("Printing out the array. Type enter to proceed");
			option1=scan.nextLine();
			/* Task 10: print out the array again; are the duplicated printed out again? */
			for(int i=0;i<libAr.length;++i){
				for(int j=0;j<libAr[i].length;++j){
					System.out.println(libAr[i][j].toString());
				}
			}
			
		}
}