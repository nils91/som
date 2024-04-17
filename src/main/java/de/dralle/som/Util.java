/**
 * 
 */
package de.dralle.som;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Nils Dralle
 *
 */
public class Util {
	public static boolean getBit(int value, int position) {
		return ((value >> position) & 1) > 0;
	}

	public static int getAsUnsignedInt(List<Boolean> bits) {
		return getAsUnsignedInt(bits.toArray(new Boolean[bits.size()]));
	}

	public static int getAsUnsignedInt(Boolean[] bits) {
		boolean[] valBits = new boolean[bits.length];
		for (int i = 0; i < valBits.length; i++) {
			valBits[i] = bits[i];
		}
		return getAsUnsignedInt(valBits);
	}

	public static int getAsUnsignedInt(boolean[] bits) {
		int n = 0;
		for (int i = 0; i < bits.length; i++) {
			if (bits[i]) {
				n += Math.pow(2, bits.length - i - 1);
			}
		}
		return n;
	}

	public static Map<String, Integer> getBuiltinAdresses() {
		Map<String, Integer> map = new HashMap<>();
		map.put("ACC", AbstractSomMemspace.ACC_ADDRESS);
		map.put("ADR_EVAL", AbstractSomMemspace.ADR_EVAL_ADDRESS);
		map.put("WH_EN", AbstractSomMemspace.WH_EN);
		map.put("N", AbstractSomMemspace.ADDRESS_SIZE_START);
		map.put("WH_COM", AbstractSomMemspace.WH_COM);
		map.put("WH_DIR", AbstractSomMemspace.WH_DIR);
		map.put("WH_SEL", AbstractSomMemspace.WH_SEL);
		map.put("ADR", AbstractSomMemspace.START_ADDRESS_START);
		return map;
	}

	public static Map<Integer, String> getBuiltinAdressesAddressKey() {
		Map<Integer, String> map = new HashMap<>();
		for (Entry<String, Integer> entry : getBuiltinAdresses().entrySet()) {
			String key = entry.getKey();
			Integer val = entry.getValue();
			map.put(val, key);
		}
		return map;
	}

	public static byte[] image2ByteArray(RenderedImage source) {
		BufferedImage bufferedImage = null;
		if (source instanceof BufferedImage) {
			bufferedImage = (BufferedImage) source;
		} else {
			// create a new BufferedImage from the RenderedImage
			bufferedImage = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
			bufferedImage.createGraphics().drawRenderedImage(source, new AffineTransform());
		}
		// Get the pixels from the image
		int[] npixels = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();

		// Create a byte array to hold the decoded data
		byte[] ndataWithLength = new byte[npixels.length * 3];
		int nindex = 0;
		for (int i = 0; i < npixels.length; i++) {
			int pixel = npixels[i];
			ndataWithLength[nindex++] = (byte) ((pixel >> 16) & 0xff);
			ndataWithLength[nindex++] = (byte) ((pixel >> 8) & 0xff);
			ndataWithLength[nindex++] = (byte) (pixel & 0xff);
		}

		// Extract the length of the original data
		int length = ByteBuffer.wrap(ndataWithLength, 0, 4).getInt();

		// Extract the data from the byte array (excluding the length)
		byte[] data = new byte[length];
		for (int i = 0; i < data.length; i++) {
			data[i] = ndataWithLength[i + 4];
		}
		return data;
	}

	public static int byteArrayToInt(byte[] bytes) {
		return ByteBuffer.wrap(bytes).getInt();
	}

	public static RenderedImage byteArray2Image(byte[] data) {
		// Prepend the length of the data to the byte array
		byte[] dataWithLength = new byte[data.length + 4];
		ByteBuffer.wrap(dataWithLength).putInt(data.length);
		System.arraycopy(data, 0, dataWithLength, 4, data.length);
		int pxCNT = dataWithLength.length / 3;
		while (pxCNT * 3 < dataWithLength.length) {
			pxCNT++;
		}
		double sq = Math.sqrt(pxCNT);
		int width = 0;
		int height = 0;
		if (sq != (int) sq) {
			width = (int) sq;
			height = width + 1;
		} else {
			width = (int) sq;
			height = (int) sq;
		}
		double minAr = 0.9;
		double maxAr = 2.5;
		double ar = (double) width / (double) height;
		while (width * height != pxCNT && !(ar > minAr && ar < maxAr)) {
			int wh = width * height;
			if (wh < pxCNT) {
				width++;
			}
			if (wh > pxCNT) {
				height--;
				if (height < 1) {
					height++;
					width--;
				}
			}
			ar = (double) width / (double) height;
			if (!(ar > minAr && ar < maxAr)) {
				pxCNT++;
			}
		}
		while (width * height < pxCNT) {
			width++;
		}
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// Set the pixels in the image
		int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		int index = 0;
		for (int i = 0; i < pixels.length; i++) {
			int r = (index < dataWithLength.length) ? (dataWithLength[index++] & 0xff) : 0;
			int g = (index < dataWithLength.length) ? (dataWithLength[index++] & 0xff) : 0;
			int b = (index < dataWithLength.length) ? (dataWithLength[index++] & 0xff) : 0;
			pixels[i] = (r << 16) | (g << 8) | b;
		}
		img.flush();
		return img;
	}

	/**
	 * Takes prefixes of the form '2b', '3b' etc. and returns the number. Lower
	 * (inclusice) bound is 2.
	 * 
	 * @param prefix
	 * @return
	 */
	public static int getBaseFromPrefix(String prefix) {
		return Integer.parseInt(prefix.substring(0, prefix.length() - 1));
	}

	/**
	 * Parses an integer with a prefix specifying the base. If the string starts
	 * with a '-', the integer is negative. The following prefixes are supported:
	 * '0b' for base 2, '0o' for base 8, '0d' for base 10, '0h' and '0x' for base
	 * 16. Other bases can also be specified with the 'b' prefix: '2b' would be base
	 * 2, 'b3' base 3 and so on. If no base is specified, 10 is assumed to be the
	 * base.
	 * 
	 * @param str
	 * @return
	 */
	public static int decodeInt(String str) {
		boolean neg = str.startsWith("-");
		if (str.startsWith("-")) {
			str = str.substring(1);
		}
		int base = 10;
		if (str.startsWith("0b")) {
			base = 2;
			str = str.substring(2, str.length());
		} else if (str.startsWith("0o")) {
			base = 8;
			str = str.substring(2, str.length());
		} else if (str.startsWith("0d")) {
			base = 10;
			str = str.substring(2, str.length());
		} else if (str.startsWith("0h")) {
			base = 16;
			str = str.substring(2, str.length());
		} else if (str.startsWith("0x")) {
			base = 16;
			str = str.substring(2, str.length());
		} else if (str.contains("b")) {
			int idx = str.indexOf("b");
			base = getBaseFromPrefix(str.substring(0, idx + 1));
			str = str.substring(idx + 1, str.length());
		}
		int i = Integer.parseInt(str, base);
		if (neg) {
			return i * -1;
		} else {
			return i;
		}
	}
}
