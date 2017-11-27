

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Stefan on 16-May-17.
 * 
 * Utility methods for sorting arrays.
 */

public class Utils {
	
	/* Fill array */
	public static void fillArray(int[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		printArray(array, "Array Filled: " + array.length);
	}
	
	/* Verify sorted array */
	public static void checkArray(int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (i != array[i]) {
				printArray(array, "FAIL: Array not sorted...");
				return;
			}
 		}
		printArray(array, "SUCCESS: Array sorted");	
	}
	
	/* Chop array into pieces */
	public static int[][] chopArray(int[] array, int chops) {
		int sharedArraysLength = 0;
		int[][] arrays = new int[chops][];
		int chunkSize = array.length / chops;
		for (int i = 0; i < chops; i++) {
			if (i < chops - 1) {				
				arrays[i] = Arrays.copyOfRange(array, i * chunkSize, (i + 1) * chunkSize);
				sharedArraysLength += arrays[i].length;
				//System.out.println("Chop " + i + " length: " + arrays[i].length);
			} else {
				arrays[i] = Arrays.copyOfRange(array, i * chunkSize, array.length);
				sharedArraysLength += arrays[i].length;
				//System.out.println("Chop " + i + " length: " + arrays[i].length);
			}
		}
		if (array.length == sharedArraysLength) {			
			System.out.println("Array Chopped");
		} else {
			System.out.println("Array chops not same length as original array.");
		}
		return arrays;
	}
	
	/* Convert array to string delimited by spaces */
	public static String arrayToString(int[] array) {
		StringBuilder sb = new StringBuilder();
		Arrays.stream(array).forEach((i) -> {
			sb.append(i + " ");
		});
		return sb.toString();
	}
	
	/* Convert string delimited by spaces to array */
	public static int[] stringArrayToIntArray(String[] integerStrings) {
		int[] array = new int[integerStrings.length];
		for (int i = 0; i < array.length; i++) {
			array[i] = Integer.parseInt(integerStrings[i]);
		}
		return array;
	}

    /* Print array */
    public static void printArray(int[] array, String message) {
        System.out.print(message + " ");
        printArray(array);
    }
    public static void printArray(int[] array) {
    	if (array.length > 500) {
    		System.out.println("\n(Array too big to print)\n");
    		return;
    	} else {
    		System.out.println();
    		Arrays.stream(array).forEach((i) -> {
    			System.out.print(i + " ");
    		});
    		System.out.println();
    	}
    }

    /* Shuffle array */
    public static int[] shuffleArray(int[] array) {

        if (array.length < 2)
            return array;

        Random rnd = new Random();

        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int a = array[index];
            array[index] = array[i];
            array[i] = a;
        }

        printArray(array, "Array Shuffled");

        return array;
    }

    /* Paste arrays */
	public static int[] pasteArrays(int[][] arrays) {
		int[] result;
		int finalSize = 0;
		for (int i = 0; i < arrays.length; i++) {
			finalSize += arrays[i].length;
		}
		result = new int[finalSize];
		int prev = 0;
		for (int i = 0; i < arrays.length; i++) {			
			System.arraycopy(arrays[i], 0, result, prev, arrays[i].length);
			prev += arrays[i].length;
		}
		return result;
	}
}