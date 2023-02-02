/**
 * 
 */
package de.dralle.som;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
		c = new Compiler();
	}

	public HRASModel readHRASFile(String path) throws IOException {
		File f = new File(path);
		FileInputStream fis = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(fis);
		HRASParser hp = new HRASParser();
		HRASModel m = hp.parse(bis);
		bis.close();
		return m;
	}

	public void writeHRASFile(HRASModel m, String path) throws IOException {
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
		HRACParser hp = new HRACParser();
		HRACModel m = hp.parse(bis);
		bis.close();
		return m;
	}

	public void writeHRACFile(HRACModel m, String path) throws IOException {
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

		for (int i = 0; i < memspace.getSize(); i++) {
			bis.write(memspace.getBit(i) ? '1' : '0');
		}
		bis.close();
	}

	private Object loadFromString(String source, SOMFormats sourceFormat) throws IOException {
		return loadFromInputStream(new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8)), sourceFormat);
	}

	private Object loadFromFile(File f, SOMFormats sourceFormat) throws IOException {
		FileInputStream fis = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(fis);
		Object obj = loadFromInputStream(bis, sourceFormat);
		bis.close();
		return obj;
	}

	private Object loadFromPath(Path p, SOMFormats sourceFormat) throws IOException {
		return loadFromFile(p.toFile(), sourceFormat);
	}

	private Object loadFromFile(String path, SOMFormats sourceFormat) throws IOException {
		return loadFromFile(new File(path), sourceFormat);
	}

	private Object loadFromInputStream(InputStream source, SOMFormats sourceFormat) throws IOException {
		if (sourceFormat.equals(SOMFormats.HRAC)) {
			return new HRACParser().parse(source);
		}
		if (sourceFormat.equals(SOMFormats.HRAS)) {
			return new HRASParser().parse(source);
		}
		if (sourceFormat.equals(SOMFormats.AB)) {
			Scanner s = new Scanner(source, StandardCharsets.UTF_8.name()).useDelimiter("\\A");
			String result = s.hasNext() ? s.next() : "";
			s.close();
			return new Compiler().abStringToMemspace((String) result);
		}
		if (sourceFormat.equals(SOMFormats.BIN)) {
			List<Byte> bytes = new ArrayList<>();
			int b;
			while ((b = source.read()) != -1) {
				bytes.add((byte) b);
			}
			IMemspace m = new Compiler().byteListToMemspace(bytes);
			return m;
		}
		return null;
	}

	private OutputStream writeToOutputStream(Object obj, SOMFormats format, OutputStream out) throws IOException {
		if (format.equals(SOMFormats.BIN)) {
			IMemspace m = (IMemspace) obj;
			byte[] arr = new Compiler().memspaceToByteArray(m);
			for (int i = 0; i < arr.length; i++) {
				byte c = arr[i];
				out.write(c);
			}
			return out;
		} else {
			OutputStreamWriter osw = new OutputStreamWriter(out);
			osw = writeToOutputWriter(obj, format, osw);
			osw.close();
			return out;
		}
	}

	private OutputStreamWriter writeToOutputWriter(Object obj, SOMFormats format, OutputStreamWriter out)
			throws IOException {
		if (format.equals(SOMFormats.HRAC)) {
			HRACModel m = (HRACModel) obj;
			String str = m.asCode();
			out.write(str);
		}
		if (format.equals(SOMFormats.HRAS)) {
			HRASModel m = (HRASModel) obj;
			String str = m.asCode();
			out.write(str);
		}
		if (format.equals(SOMFormats.AB)) {
			IMemspace m = (IMemspace) obj;
			String str = new Compiler().memSpaceToABString(m);
			out.write(str);
		}
		return out;
	}

	private String writeToString(Object obj, SOMFormats format) throws IOException {
		OutputStream os = new ByteArrayOutputStream();
		os = writeToOutputStream(obj, format, os);
		os.close();
		return os.toString();

	}

	private File writeToFile(Object obj, SOMFormats format, File f) throws IOException {
		FileOutputStream fis = new FileOutputStream(f);
		BufferedOutputStream bis = new BufferedOutputStream(fis);
		OutputStream os = writeToOutputStream(obj, format, bis);
		os.close();
		return f;
	}
	private File writeToFile(Object obj, SOMFormats format, Path p) throws IOException {
		return writeToFile(obj, format, p.toFile());
	}
	private File writeToFile(Object obj, SOMFormats format,String filePath) throws IOException {
		return writeToFile(obj, format, Paths.get(filePath));
	}

	public SOMFormats getFormatFromFilename(String name) {
		for (SOMFormats format : SOMFormats.values()) {
			String[] possibleFileExtensions = format.getFileExtensionString();
			for (int i = 0; i < possibleFileExtensions.length; i++) {
				String string = possibleFileExtensions[i];
				if (name.endsWith(string)) {
					return format;
				}
			}

		}
		return null;
	}

}
