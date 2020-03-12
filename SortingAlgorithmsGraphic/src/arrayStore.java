package introAlgorithms;

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

public class arrayStore {
	
	int arraySize;
	long sortTime;

	arrayStore(int size, long time){
		this.arraySize = size;
		this.sortTime = time;
	}

	public void print(){
		System.out.println(this.arraySize + " and " + this.sortTime);
	}

}
