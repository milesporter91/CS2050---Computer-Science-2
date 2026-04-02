/*
 * Stack Example
 * Add comments to explain the code
 */

import java.util.ArrayList;

public class StackTest
{
	public static void main(String[] args)
	{

		Stack<Integer> stack = new Stack<>();

		System.out.println("Pushing elements: 10, 20, 30");
		stack.push(10);
		stack.push(20);
		stack.push(30);

		System.out.println("Stack after pushing: " + stack);

		int popped = stack.pop();
		System.out.println("Popped element: " + popped);
		System.out.println("Stack after popping: " + stack);

		int topElement = stack.peek();
		System.out.println("Top element: " + topElement);

		System.out.println("Is stack empty? " + stack.isEmpty());
		if (!stack.isEmpty()) {
			popped = stack.pop();
		} else {
			System.out.println("Stack is empty.");
		}
		System.out.println("Popped element: " + popped);
		System.out.println("Stack after popping: " + stack);

		if (!stack.isEmpty()) {
			popped = stack.pop();
		} else {
			System.out.println("Stack is empty.");
		}
		System.out.println("Popped element: " + popped);
		System.out.println("Stack after popping: " + stack);

		if (!stack.isEmpty()) {
			popped = stack.pop();
		} else {
			System.out.println("Stack is empty.");
		}
		System.out.println("Popped element: " + popped);
		System.out.println("Stack after popping: " + stack);
		
		Stack<String> stackTwo = new Stack<>();
		System.out.println("Pushing 3 strings to Stack");
		stackTwo.push("String One");
		stackTwo.push("String Two");
		stackTwo.push("String Three");
		System.out.println(stackTwo);
		
		System.out.println("Popping from Stack Two");
		stackTwo.pop();
		System.out.println(stackTwo);
		stackTwo.pop();
		System.out.println(stackTwo);
		
		System.out.println("Peeking Stack Two");
		System.out.println(stackTwo.peek());
	}
}

class Stack<E>
{

	private ArrayList<E> items;

	public Stack()
	{
		items = new ArrayList<>();
	}

	public boolean isEmpty()
	{
		return items.isEmpty();
	}

	public void push(E item)
	{
		items.add(item);
	}

	public E pop()
	{
		int topIndex = items.size() - 1;
		return items.remove(topIndex);
	}

	public E peek()
	{
		int topIndex = items.size() - 1;
		return items.get(topIndex);
	}

	public int size()
	{
		return items.size();
	}

	@Override
	public String toString()
	{
		if (items.isEmpty())
		{
			return "<<empty stack>>";
		}
		return "bottom -> " + items + " <- top";
	}
}