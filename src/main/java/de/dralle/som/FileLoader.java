/**
 * 
 */
package de.dralle.som;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Nils
 *
 */
public class FileLoader {
	public ISomMemspace loadBinaryFile(String path) throws IOException {
		File f = new File(path);
		FileInputStream fis = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(fis);
		List<Byte> bytes = new ArrayList<>();
		int b;
		while ((b = bis.read()) != -1) {
			bytes.add((byte) b);
		}

		bis.close();
		byte[] byteArray = new byte[bytes.size()];
		for (int i = 0; i < byteArray.length; i++) {
			byteArray[i] = bytes.get(i);
		}
		ISomMemspace m = new ByteArrayMemspace(byteArray);
		int n = m.getN();
		m.resize((int) Math.pow(2, n), true);
		return m;
	}

	public void writeBinaryFile(IMemspace memspace, String path) throws IOException {
		File f = new File(path);
		FileOutputStream fis = new FileOutputStream(f);
		BufferedOutputStream bis = new BufferedOutputStream(fis);

		ByteArrayMemspace helper = new ByteArrayMemspace();
		if (memspace instanceof ByteArrayMemspace) {
			helper = (ByteArrayMemspace) memspace;
		} else {
			helper.resize(memspace.getSize(), false);
			for (int i = 0; i < memspace.getSize(); i++) {
				helper.setBit(i, memspace.getBit(i));
			}
		}
		bis.write(helper.getUnderlyingByteArray());
		bis.close();
	}
/**
 * Will use the file extension as format. Throws UnsupportedOperationException if unknown or not implemented yet.
 * @param path
 * @return
 * @throws IOException
 */
	public IMemspace loadFile(String path) throws IOException {
		File f = new File(path);
		String name = f.getName();
		for (SOMFormats format : SOMFormats.values()) {
			if (name.endsWith(format.getFileExtensionString())) {
				return loadFile(format, path);
			}
		}
		throw new UnsupportedOperationException("Unknown format");
	}

	public IMemspace loadFile(SOMFormats format, String path) throws IOException {
		switch (format) {
		case BIN:
			return loadBinaryFile(path);

		default:
			throw new UnsupportedOperationException("Format "+format+" not implemented yet");
		}
		// return null;
	}
}
