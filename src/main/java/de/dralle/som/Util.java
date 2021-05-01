/**
 * 
 */
package de.dralle.som;

import java.util.List;

/**
 * @author Nils Dralle
 *
 */
public class Util {
	public static int getAsUnsignedInt(List<Boolean> bits) {
		return getAsUnsignedInt(bits.toArray(new Boolean[bits.size()]));
	}

	public static int getAsUnsignedInt(Boolean[] bits) {
		int n = 0;
		for (int i = 0; i < bits.length; i++) {
			if (bits[i]) {
				n += Math.pow(2, bits.length - i - 1);
			}
		}
		return n;
	}

}
