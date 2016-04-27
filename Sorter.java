/* 

	Sorter class. 
	You can pass it a list, it will sort it using either bubblesort or insertion sort.

*/ 

public class Sorter { 

	private int[] unsortedList = {0};
	private int[] sortedList = {0}; 

	public Sorter(int[] list) { 
		unsortedList = list; 
	}

	public void bubbleSort(int maxIterations) { // defining max iterations will make you only partially sort the list
		int[] tempList = unsortedList;
		int cap = tempList.length;

		while(cap >= 1 && tempList.length-cap < maxIterations) {
			for (int i = 0; i < cap-1; i++) {
				if (tempList[i] > tempList[i+1]) {
					int temp = tempList[i]; 
					tempList[i] = tempList[i+1]; 
					tempList[i+1] = temp; 
				}
			}
			cap --; 

			// sum of list elements - not necessary 
			/*int sum = 0; 

			for (int j = 0; j < unsortedList.length; j ++) {
				sum += unsortedList[j];
			}*/
		}

		sortedList = tempList; // apparently modifying tempList also modifies unsortedList - are they 2 pointer referencing the same memory location?
	}

	public void insertionSort(int maxIterations) { 
		// will sort from greatest to least 
		int[] tempList = unsortedList;
		
		int j;                     // the number of items sorted so far
	    int key;                // the item to be inserted
	    int i;  

	    int cap = (maxIterations < tempList.length) ? maxIterations : tempList.length; // select the smaller value; src: http://www.cafeaulait.org/course/week2/43.html

	    for (j = 1; j < cap; j++) {   // Start with 1 (not 0)
	    
	        key = tempList[j];
	        
	        for(i = j-1; (i >= 0) && (tempList[i] < key); i--) {  // Smaller values are moving up
	     		tempList[i+1] = tempList[i];
	        }
	        
	        tempList[i+1] = key;    // Put the key in its proper location
	    }

		sortedList = tempList;
		// code for this sorting method based off of example at http://mathbits.com/MathBits/Java/arrays/InsertionSort.htm
	}


	public int[] getSortedList() { 
		return sortedList; 
	}

	public int[] getUnsortedList() { 
		return unsortedList; 
	}

	public void setUnsortedList(int[] list) { 
		unsortedList = list; 
	}

}