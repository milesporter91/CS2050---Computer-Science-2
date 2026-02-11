public class SelectionSortTDD {
	public static void main(String[] args) {
		System.out.println("Testing Selection Sort");

		int[] sizes = { 5, 10, 100, 1000 };

		for (int n : sizes) {
			System.out.printf("%nSort Size n=%d\n", n);

			int[] baseReversed = new int[n];
			for (int i = 0; i < n; i++) {
				baseReversed[i] = n - 1;
			}
		}

		int[][] testCases = { { 4, 2, 7, 1, 5 }, // Regular case
				{}, // Empty array
				{ 5 }, // Single element
				{ 1, 2, 3, 4, 5 }, // Already sorted
				{ 9, 7, 5, 3, 1 }, // Reverse sorted
				{ 4, 2, 7, 2, 5 } // Array with duplicates
		};

		for (int i = 0; i < testCases.length; i++) {
			System.out.println("Test Case " + (i + 1) + ": Before Sorting:");
			printArray(testCases[i]);
			selectionSort(testCases[i]);
			System.out.println("After Sorting:");
			printArray(testCases[i]);
			System.out.println();
		}
	}

	public static void selectionSort(int[] array) {
		int n = array.length;
		int currentIndex = 0;
		while (currentIndex < n) {
			for (int compareIndex = currentIndex + 1; compareIndex < n; compareIndex++) {
				int currentValue = array[currentIndex];
				int smallestValueIndex = currentIndex;
				if (array[compareIndex] < currentValue) {
					smallestValueIndex = compareIndex;
				}
				array[currentIndex] = array[smallestValueIndex];
				array[compareIndex] = currentValue;
				currentIndex++;
			}
		}
	}

	public static void printArray(int[] array) {
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i] + " ");
		}
		System.out.println();
	}

}