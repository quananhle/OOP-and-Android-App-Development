package introAlgorithms;

import java.util.ArrayList;
import java.util.Random;

/**
=================================================
 * CS430	HW3
 *
 * Team name: Corona Virus 
 *
 * @author Jun Fan - seat#: 21 - CWID: A204
 * @author Quan Le - seat#: 38 - CWID: A20410211
 =================================================
 */

public class mergeSortGraph implements Runnable {


	static Random rnd = new Random();
	static final int largestValue = 250;
	sortingGraphics graph;
	private int array[];
	private int tempArray[];
	boolean running = false, visualize = true;
	ArrayList<arrayStore> list;

	mergeSortGraph (int i, sortingGraphics graph, boolean visualize, ArrayList<arrayStore> list) {
		this.array = new int[i];
		this.graph = graph;
		this.list = list;
		this.visualize = visualize;
	}

	public void sort() {
		running = true;
		fillArray(array);
		tempArray = new int[array.length];

		long startTime = System.nanoTime();
		mergeSort(0, array.length -1);
		long endTime = System.nanoTime();

		long timeTaken = (endTime - startTime) / 1000;

		if (visualize == false) {
			this.list.add(new arrayStore(array.length, timeTaken));
		}
		running = false;	
	}

	private void mergeSort(int leftIndex, int rightIndex) {

		if (leftIndex < rightIndex) {
			int middle = leftIndex + (rightIndex - leftIndex) / 2;
			if (running == true) {
				mergeSort(leftIndex, middle);
			}
			if (running == true) {
				mergeSort(middle + 1, rightIndex);
			}
			if (running == true) {
				merge(leftIndex, middle, rightIndex);
			}
		}
	}

	private void merge(int leftIndex, int middle, int rightIndex) {

		for (int i = leftIndex; i <= rightIndex; i++) {
			tempArray[i] = array[i];
		}
		int i = leftIndex;
		int j = middle + 1;
		int k = leftIndex;
		while (i <= middle && j <= rightIndex) {
			if (tempArray[i] <= tempArray[j]) {
				array[k] = tempArray[i];

				if (visualize == true) {
					graph.draw(array);
					try { Thread.sleep(5);} catch (Exception ex) {}
				}
				i++;
			} else {
				array[k] = tempArray[j];
				
				if (visualize == true) {
					graph.draw(array);
					try { Thread.sleep(5);} catch (Exception ex) {}
				}
				j++;
			}
			k++;
		}
		while (i <= middle) {
			array[k] = tempArray[i];
			
			if (visualize == true) {
				graph.draw(array);
				try { Thread.sleep(5);} catch (Exception ex) {}
			}
			k++;
			i++;
		}
	}
	public void stop() {
		running = false;
	}
	private void fillArray (int[] a) {
		for (int i = 0; i < a.length; i++) {
			a[i] = rnd.nextInt(largestValue);
		}
	}
	@Override
	public void run() {
		sort();
	}

}