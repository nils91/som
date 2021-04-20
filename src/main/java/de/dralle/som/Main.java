/**
 * 
 */
package de.dralle.som;

/**
 * @author Nils Dralle
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int i = 0; i < args.length; i++) {
			String string = args[i];
			System.out.println("Argument["+i+"]: "+string);
		}

	}

}
