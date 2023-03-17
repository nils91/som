package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.RenderedImage;
import java.nio.ByteBuffer;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import de.dralle.som.Util;

class IntByteArrayConversionTests {

	private static Random r;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		r = new Random();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@RepeatedTest(10000)
	void intToByteArraTest() {
		int origValue = r.nextInt();
		byte[] yrr = Util.intToByteArray(origValue);
		int convValue = Util.byteArrayToInt(yrr);
		assertEquals(origValue, convValue);
	}

	@RepeatedTest(10)
	void byteArrToImgConvTest() {
		int len = (int) (10+r.nextDouble()*1000000);
		System.out.println(len);
		byte[] arr = new byte[len];
		r.nextBytes(arr);
		RenderedImage img = Util.byteArray2Image(arr);
		byte[] narr = Util.image2ByteArray(img);
		assertArrayEquals(arr, narr);
	}

	@Test
	void byteArrToImgConvOnceTest() {
		byte[] arr = new byte[10];
		r.nextBytes(arr);
		RenderedImage img = Util.byteArray2Image(arr);
		System.out.println("----");
		byte[] narr = Util.image2ByteArray(img);
		assertArrayEquals(arr, narr);
	}

	@RepeatedTest(1000)
	void RGBintToByteArraTest() {
		int origValue =(int) (r.nextDouble()*50000);
		byte[] yrr = Util.toRGBBytes(origValue);
		int convValue = Util.fromRGBBytes(yrr);
		assertEquals(origValue, convValue);
	}

	@RepeatedTest(1000)
	void testImageAndBackLocal() {
		byte[] arr = new byte[450];
		r.nextBytes(arr);
		int width = 100;
		int height = 100;
		// Prepend the length of the data to the byte array
		byte[] dataWithLength = new byte[arr.length + 4];
		ByteBuffer.wrap(dataWithLength).putInt(arr.length);
		System.arraycopy(arr, 0, dataWithLength, 4, arr.length);

		// Create a BufferedImage object with RGB color model
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// Set the pixels in the image
		int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		int index = 0;
		for (int i = 0; i < pixels.length; i++) {
		    int r = (index < dataWithLength.length) ? (dataWithLength[index++] & 0xff) : 0;
		    int g = (index < dataWithLength.length) ? (dataWithLength[index++] & 0xff) : 0;
		    int b = (index < dataWithLength.length) ? (dataWithLength[index++] & 0xff) : 0;
		    pixels[i] = (r << 16) | (g << 8) | b;
		}

		// Get the pixels from the image
		int[] npixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

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
		System.arraycopy(ndataWithLength, 4, data, 0, length);
		assertArrayEquals(arr, data);
	}

}
