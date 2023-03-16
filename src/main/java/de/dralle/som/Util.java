/**
 * 
 */
package de.dralle.som;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.util.ArrayList;
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
		boolean[] valBits=new boolean[bits.length];
		for (int i = 0; i < valBits.length; i++) {
			valBits[i]=bits[i];
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
	public static byte[] image2ByteArray(RenderedImage source) {
		BufferedImage bufferedImage = null;
		if (source instanceof BufferedImage) {
			bufferedImage = (BufferedImage) source;
		} else {
			// create a new BufferedImage from the RenderedImage
			bufferedImage = new BufferedImage(source.getWidth(), source.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			bufferedImage.createGraphics().drawRenderedImage(source, new AffineTransform());
		}
		List<Integer> pixValues = new ArrayList<>();
		for (int i = 0; i < bufferedImage.getWidth(); i++) {
			for (int j2 = 0; j2 < bufferedImage.getHeight(); j2++) {
				pixValues.add(bufferedImage.getRGB(i, j2));
			}
		}
		// create the byte array from image
		byte[] arr = new byte[pixValues.size() * 3];
		for (int i = 0; i < pixValues.size(); i++) {
			int rgb = pixValues.get(i);

			// extract the red, green, and blue components from the RGB value
			byte red = (byte) ((rgb >> 16) & 0xFF);
			byte green = (byte) ((rgb >> 8) & 0xFF);
			byte blue = (byte) (rgb & 0xFF);

			arr[i * 3] = red;
			arr[i * 3 + 1] = green;
			arr[i * 3 + 2] = blue;

		}
		//get length
		byte[] lenBytes=new byte[4];
		for (int i = 0; i < lenBytes.length; i++) {
			lenBytes[i]=arr[i];
			
		}
		int arrLen=byteArrayToInt(lenBytes);
		byte[] data=new byte[arrLen];
		for (int i = 0; i < data.length; i++) {
			data[i]=arr[lenBytes.length+i];
		}
		return data;
	}
	public static int byteArrayToInt(byte[] bytes) {
	    return (bytes[0] << 24) | ((bytes[1] & 0xff) << 16) | ((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff);
	}
	public static RenderedImage byteArray2Image(byte[] data) {
		int dataLen = data.length;
		byte[] dataLenBytes = intToByteArray(dataLen);
		byte[] dataAugment = new byte[dataLenBytes.length+data.length];
		for (int i = 0; i < dataLenBytes.length; i++) {
			byte b = dataLenBytes[i];
			dataAugment[i]=b;
			
			
		}
		for (int j = 0; j < data.length; j++) {
				byte b = data[j];
				dataAugment[dataLenBytes.length+j]=b;
			}
		int pxCNT = dataAugment.length / 3 + dataAugment.length % 3;
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
		double minAr=0.9;
		double maxAr=2.5;
		double ar=(double)width/(double)height;
		while (width * height != pxCNT&&!(ar>minAr&&ar<maxAr)) {
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
			ar=(double)width/(double)height;
			if(!(ar>minAr&&ar<maxAr)) {
				pxCNT++;
			}
		}
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int bARrIx = (i * width + j) * 3;
				byte r = 0;
				byte g = 0;
				byte b = 0;
				if (bARrIx < data.length) {
					r = data[bARrIx];
				}
				if (bARrIx + 1 < data.length) {
					g = data[bARrIx + 1];
				}
				if (bARrIx + 2 < data.length) {
					b = data[bARrIx + 2];
				}
				int rgb = (r << 16) | (g << 8) | b;
				img.setRGB(i, j, rgb);
			}
		}
		img.flush();
		return img;
	}
	public static byte[] intToByteArray(int value) {
	    return new byte[] { (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value };
	}
}
