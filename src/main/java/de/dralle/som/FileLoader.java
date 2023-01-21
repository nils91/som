/**
 * 
 */
package de.dralle.som;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.dralle.som.languages.hrac.HRACParser;
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.HRASParser;
import de.dralle.som.languages.hras.model.HRASModel;

/**
 * @author Nils
 *
 */
public class FileLoader {
	private Compiler c;
	public FileLoader() {
		c=new Compiler();
	}
	public HRASModel readHRASFile(String path) throws IOException {
		File f = new File(path);
		FileInputStream fis = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(fis);
		HRASParser hp=new HRASParser();
		HRASModel m = hp.parse(bis);
		bis.close();
		return m;
	}
	public void writeHRASFile(HRASModel m ,String path) throws IOException {
		File f = new File(path);
		FileWriter fis = new FileWriter(f);
		BufferedWriter bis = new BufferedWriter(fis);
		bis.write(m.asCode());
		bis.close();
	}
	public HRACModel readHRACFile(String path) throws IOException {
		File f = new File(path);
		FileInputStream fis = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(fis);
		HRACParser hp=new HRACParser();
		HRACModel m = hp.parse(bis);
		bis.close();
		return m;
	}
	public void writeHRACFile(HRACModel m ,String path) throws IOException {
		File f = new File(path);
		FileWriter fis = new FileWriter(f);
		BufferedWriter bis = new BufferedWriter(fis);
		bis.write(m.asCode());
		bis.close();
	}
	public IMemspace loadBinaryFile(String path) throws IOException {
		File f = new File(path);
		FileInputStream fis = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(fis);
		List<Byte> bytes = new ArrayList<>();
		int b;
		while ((b = bis.read()) != -1) {
			bytes.add((byte) b);
		}
		bis.close();		
		IMemspace m = c.byteListToMemspace(bytes);
		return m;
	}
	public void writeBinaryFile(IMemspace memspace, String path) throws IOException {
		File f = new File(path);
		FileOutputStream fis = new FileOutputStream(f);
		BufferedOutputStream bis = new BufferedOutputStream(fis);
		bis.write(c.memspaceToByteArray(memspace));
		bis.close();
	}

	public IMemspace loadAsciiBinaryFile(String path) throws IOException {
		File f = new File(path);
		FileReader fis = new FileReader(f);
		BufferedReader bis = new BufferedReader(fis);
		List<Boolean> bits = new ArrayList<>();
		String nxtLine;
		while ((nxtLine = bis.readLine()) != null) {
			char[] chars = nxtLine.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				switch (chars[i]) {
				case '0':
					bits.add(false);
					break;
				case '1':
					bits.add(true);
					break;
				default:
					break;
				}
			}
		}
		bis.close();
		IMemspace m = c.booleanListToMemspace(bits);
		return m;
	}

	

	public void writeAsciiBinaryFile(IMemspace memspace, String path) throws IOException {
		File f = new File(path);
		FileWriter fis = new FileWriter(f);
		BufferedWriter bis = new BufferedWriter(fis);

		for (int i = 0; i <memspace.getSize(); i++) {
			bis.write(memspace.getBit(i)?'1':'0');
		}
		bis.close();
	}

	/**
	 * Will use the file extension as format. Throws UnsupportedOperationException
	 * if unknown or not implemented yet.
	 * 
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
		case AB:
			return loadAsciiBinaryFile(path);
		default:
			throw new UnsupportedOperationException("Format " + format + " not implemented yet");
		}
		// return null;
	}
}
