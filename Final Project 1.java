import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Sorting {
	// Merges two sub-arrays of arr[]. First sub-array is arr[l..m]
	// Second sub-array is arr[m + 1..r] in ascending order
	public static void mergeAscending (int arr[], int l, int m, int r) {
		// Find sizes of two sub-arrays to be merged
		int n1 = m - l +1;
		int n2 = r - m;
		
		/* Create temp arrays */
		int L[] = new int[n1];
		int R[] = new int [n2];
		
		/* Copy data to temp arrays */
		for (int i = 0; i < n1; ++i) {
			L[i] = arr[l+i];
		}
		for (int j = 0; j < n2; ++j) {
			R[j] = arr[m + 1 + j];
		}
		
		/* Merge the temp arrays */
		// Initial indexes of first and second sub-arrays
		int i = 0, j = 0;
		// Initial index of merged sub-arrays
		int k = l;
	
		while (i < n1 && j < n2) {
			if (L[i] <= R[j]) {
				arr[k] = L[i];
				i++;
			}
			else {
				arr[k] = R[j];
				j++;
			}
			k++;
		}
		/* Copy remaining elements of L[] if any */
		while (i < n1) {
			arr[k] = L[i];
			i++;
			k++;
		}
		/* Copy remaining elements of R[] if any */
		while (j < n2) {
			arr[k] = R[j];
			j++;
			k++;
		}
	}
	
	// Merges two sub-arrays of arr[]. First sub-array is arr[l..m]
	// Second sub-array is arr[m + 1..r] in descending order
	public static void mergeDescending (int arr[], int l, int m, int r) {
		// Find sizes of two sub-arrays to be merged
		int n1 = m - l + 1;
		int n2 = r - m;
		/* Create temp arrays */
		int L[] = new int [n1];
		int R[] = new int [n2];
		/* Copy data to temp arrays */
		for (int i = 0; i < n1; ++i) {
			L[i] = arr [l + i];
		}
		for (int j = 0; j < n2; ++j) {
			R[j] = arr [m + 1 + j];
		}
		/* Merge the temp arrays */
		// Initial indexes of first and second sub-arrays
		int i = 0, j = 0;
		// Initial indexes of merged sub-arrays
		int k = l;
		
		while (i < n1 && j < n2) {
			if (L[i] >= R[j]) {
				arr[k] = L[i];
				i++;
			}
			else {
				arr[k] = R[j];
				j++;
			}
			k++;
		}
		/* Copy remaining elements of L[] if any */
		while (i < n1) {
			arr[k] = L[i];
			i++;
			k++;
		}
		/* Copy remaining elements of R[] if any */
		while (j < n2) {
			arr[k] = R[j];
			j++;
			k++;
		}
	}
	public static void sortAscending (int arr[], int l, int r) {
		if (l < r) {
			// Find the middle point
			int m = (l + r) / 2;
			// Sort first and second halves
			sortAscending (arr, l, m);
			sortAscending (arr, m + 1, r);
			// Merge the sorted halves
			mergeAscending (arr, l, m ,r);
		}
	}
	public static void sortDescending (int arr[], int l, int r) {
		if (l < r) {
			// Find the middle point
			int m = (l + r) / 2;
			// Sort first and second halves
			sortDescending (arr, l, m);
			sortDescending (arr, m + 1, r);
			// Merge the sorted halves
			mergeDescending (arr, l, m, r);
		}
	}
	/* A utility function to print array of size n */
	public static void printWriteAscending (int arr[], int n) throws FileNotFoundException {
		PrintWriter out = new PrintWriter (new File ("asc.txt"));
		
		for (int i = 0; i < n; ++i) {
			out.write(Integer.toString(arr[i]));
			out.write ("\n");
			System.out.print(arr[i] + " ");
		}
		System.out.println();
		System.out.println("Array succesfully writen to asc.txt");
		System.out.println();
		out.close();
	}
	public static void printWriteDescending (int arr[], int n) throws FileNotFoundException {
		PrintWriter out = new PrintWriter (new File ("desc.txt"));
		for (int i = 0; i < n; ++i) {
			out.write(Integer.toString(arr[i]));
			out.write("\n");
			System.out.print (arr[i] + " ");
		}
		System.out.println ();
		System.out.println ("Array successfully written to desc.text");
		System.out.println ();
		out.close();
	}
	// Driver method
	public static void main(String[] args) throws FileNotFoundException {
		// creating an array of 100 elements	
		Scanner ip = new Scanner (System.in);
		int n = 100;
		int arr[] = new int [n];
		// opening input file 
		String fileName = "input.txt";
		Scanner fileScanner = new Scanner (new File (fileName));
		// reading from file
		int size = 0;
		
		while (fileScanner.hasNextInt()) {
			arr[size] = fileScanner.nextInt();
			size++;
		}
		fileScanner.close();
		int op = 1;
		
		while (op != 4) {
			System.out.println("1. Ascending order, 2. Descending order, 3. Both, 4. Exit");
			op = ip.nextInt();
			
			switch (op) {
				case 1:
					sortAscending (arr, 0, size -1 );
					printWriteAscending (arr, size);
					break;
				case 2:
					sortDescending (arr, 0, size - 1);
					printWriteDescending (arr, size);
					break;
				case 3:
					sortAscending (arr, 0, size -1);
					printWriteAscending (arr, size);
					sortDescending (arr, 0, size - 1);
					printWriteDescending (arr, size);
					break;
				case 4:
					break;
				default:
					System.out.println("Invalid options");
			}
			
		}
		ip.close();
	}
}

