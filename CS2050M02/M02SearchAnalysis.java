/**
 * 
 */

public class M02SearchAnalysis
{

	public static void main(String[] args)
	{
		// Array sizes to test
		int[] sizes =
		{ 10, 100, 1000, 10000 };

		for (int n : sizes)
		{
			// Build a sorted array [1, 2, 3, ..., n]
			int[] data = new int[n];
			for (int i = 0; i < n; i++)
			{
				data[i] = i + 1;
			}

			System.out.println("\n=== Array size " + n + " ===");

			// Pick keys for test cases
			int firstKey = data[0]; // first element
			int middleKey = data[n / 2]; // middle element
			int lastKey = data[n - 1]; // last element
			int absentKey = n + 10; // guaranteed not present

			// Run Linear Search
			System.out.println("-- Linear Search --");
			linearSearch(data, firstKey);
			linearSearch(data, middleKey);
			linearSearch(data, lastKey);
			linearSearch(data, absentKey);

			// Run Binary Search
			System.out.println("-- Binary Search --");
			binarySearch(data, firstKey);
			binarySearch(data, middleKey);
			binarySearch(data, lastKey);
			binarySearch(data, absentKey);
		}
	}

	// ---------- Linear Search with comparison counting ----------
	public static int linearSearch(int[] data, int key)
	{
		int comparisons = 0;
		for (int i = 0; i < data.length; i++)
		{
			comparisons++;
			if (data[i] == key)
			{
				System.out.println(
						"Linear: key " + key + " found at index " + i + " after " + comparisons + " comparisons.");
				return i;
			}
		}
		System.out.println("Linear: key " + key + " not found after " + comparisons + " comparisons.");
		return -1;
	}

	// ---------- Binary Search with comparison counting ----------
	public static int binarySearch(int[] data, int key)
	{
		int low = 0;
		int high = data.length - 1;
		int comparisons = 0;

		while (low <= high)
		{
			int mid = (low + high) / 2;
			comparisons++;
			if (data[mid] == key)
			{
				System.out.println(
						"Binary: key " + key + " found at index " + mid + " after " + comparisons + " comparisons.");
				return mid;
			} else if (data[mid] < key)
			{
				comparisons++;
				low = mid + 1;
			} else
			{
				comparisons++;
				high = mid - 1;
			}
		}
		System.out.println("Binary: key " + key + " not found after " + comparisons + " comparisons.");
		return -1;
	}

}