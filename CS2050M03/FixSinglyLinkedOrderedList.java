/**
 * Lab: Fix Singly Linked Ordered List ----------------------------------- The
 * insertNode() and deleteNode() methods contain logic bugs. 1. Predict what
 * each method should do (draw before/after pictures). 2. Use the debugger or
 * print statements to trace previous/current. 3. Fix the code so the list stays
 * in sorted order after insertions and nodes are correctly deleted when found.
 *
 * Add comments above your fixes explaining what was wrong and why.
 */

public class FixSinglyLinkedOrderedList
{

	// Test the Singly Linked List
	public static void main(String[] args)
	{

		SinglyLinkedListFix list = new SinglyLinkedListFix();
		list.printList();

		// Use your unit testing to ensure it handles all cases
		list.insertNode(4);

		list.printList();
		list.insertNode(5);
		list.printList();
		list.deleteNode(5);

		list.insertNode(6);
		list.printList();
		list.insertNode(17);
		list.printList();
		list.deleteNode(4);
		list.printList();

	}

}

class SinglyLinkedListFix
{
	NodeFix head;

	public void insertNode(int number)
	{
		NodeFix newNode = new NodeFix(number);
		NodeFix current = head;
		NodeFix previous = null;

		while (current != null && current.data < number)
		{
			previous = current;
			current = current.next;
		}

		if (previous == null)
		{
			newNode.next = head;
			head = newNode;
		} else
		{
			// The original code only did previous.next = newNode,
			// which inserted the new node but disconnected the rest of the list.
			// You must point the new node to current first, then link previous to newNode.
			newNode.next = current;
			previous.next = newNode;
		}
	}

	public void deleteNode(int number)
	{
		NodeFix current = head;
		NodeFix previous = null;
		
		// The original loop used current.next != null, which could crash on an empty list
		// and also failed to properly check the last node.
		// Using current != null safely checks every node in the list.
		while (current != null && current.data != number)
		{
			previous = current;
			current = current.next;
		}
		// If current is null, the value was not found.
		// The original code would continue and could throw a NullPointerException.
		if (current == null) {
			return;
		}
		if (previous == null)
		{
			head = current.next;
		} else
		{
			previous.next = current.next; // Bug #5: Should be previous.next = current.next
		}
	}

	public void printList()
	{
		NodeFix current = head;
		while (current != null)
		{
			System.out.print(current.data + " → ");
			current = current.next;
		}
		System.out.println("null");
	}

	private static class NodeFix
	{
		int data;
		NodeFix next;

		public NodeFix(int data)
		{
			this.data = data;
			this.next = null;
		}
	}
}
