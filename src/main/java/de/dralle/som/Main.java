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
		String argN=args[0];
		String filename=args[1];
		int memAddressSize=Integer.parseInt(argN);
		long memSpaceSize=(long) Math.pow(2, memAddressSize);
		System.out.println("Mem Address Size: "+memAddressSize);
		System.out.println("Mem Space Size (Bits): "+memSpaceSize);
		System.out.println("File: "+filename);
	}
}
