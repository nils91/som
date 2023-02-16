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
import de.dralle.som.languages.hrbs.HRBSParser;
import de.dralle.som.languages.hrbs.model.HRBSModel;

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
	
	public void writeHRBSFile(HRBSModel m, String path) throws IOException {
		File f = new File(path);
		FileWriter fis = new FileWriter(f);
		BufferedWriter bis = new BufferedWriter(fis);
		bis.write(m.asCode());
		bis.close();
	}

	public HRBSModel readHRBSFile(String path) throws IOException {
		File f = new File(path);
		FileInputStream fis = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(fis);
		HRBSParser hp = new HRBSParser();
		HRBSModel m = hp.parse(bis);
		bis.close();
		return m;
	}
	public HRBSModel readHRBSFileFromInternal(String path) throws IOException {
		ClassLoader cl=getClass().getClassLoader();
		InputStream is = cl.getResourceAsStream(path);
		BufferedInputStream bis = new BufferedInputStream(is);
		HRBSParser hp = new HRBSParser();
		HRBSModel m = hp.parse(bis);
		bis.close();
		return m;
	}
	public HRBSModel readHRBSFileInternalFirst(String path) throws IOException{
		try {
			return readHRBSFileFromInternal(path);
		} catch (IOException e) {			
		}
		return readHRBSFile(path);
	}
	public HRBSModel loadHRBSByName(String name) throws IOException{
		return readHRBSFileInternalFirst(Paths.get("includes", "hrbs", name+".hrbs").toString());
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
		writeToFile(memspace, SOMFormats.BIN, path);
	}

	public Object loadFromString(String source, SOMFormats sourceFormat) throws IOException {
		return loadFromInputStream(new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8)), sourceFormat);
	}

	public Object loadFromFile(File f, SOMFormats sourceFormat) throws IOException {
		FileInputStream fis = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(fis);
		Object obj = loadFromInputStream(bis, sourceFormat);
		bis.close();
		return obj;
	}

	public Object loadFromPath(Path p, SOMFormats sourceFormat) throws IOException {
		return loadFromFile(p.toFile(), sourceFormat);
	}

	public Object loadFromFile(String path, SOMFormats sourceFormat) throws IOException {
		return loadFromFile(new File(path), sourceFormat);
	}
	public Object loadFromFile(File f) throws IOException {
		return loadFromFile(f,getFormatFromFilename(f.getName()));
	}

	public Object loadFromPath(Path p ) throws IOException {
		return loadFromFile(p.toFile());
	}

	public Object loadFromFile(String path) throws IOException {
		return loadFromFile(new File(path));
	}

	public Object loadFromInputStream(InputStream source, SOMFormats sourceFormat) throws IOException {
		if (sourceFormat.equals(SOMFormats.HRAC)) {
			return new HRACParser().parse(source);
		}
		if (sourceFormat.equals(SOMFormats.HRAS)) {
			return new HRASParser().parse(source);
		}
		if (sourceFormat.equals(SOMFormats.HRBS)) {
			return new HRBSParser().parse(source);
		}
		if (sourceFormat.equals(SOMFormats.AB)) {
			Scanner s = new Scanner(source, StandardCharsets.UTF_8.name()).useDelimiter("\\A");
			String result = s.hasNext() ? s.next() : "";
			s.close();
			return result;
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

	public OutputStream writeToOutputStream(Object obj, SOMFormats format, OutputStream out) throws IOException {
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

	public OutputStreamWriter writeToOutputWriter(Object obj, SOMFormats format, OutputStreamWriter out)
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
			String m = (String) obj;
			out.write(m);
		}
		return out;
	}

	public String writeToString(Object obj, SOMFormats format) throws IOException {
		OutputStream os = new ByteArrayOutputStream();
		os = writeToOutputStream(obj, format, os);
		os.close();
		return os.toString();

	}

	public File writeToFile(Object obj, SOMFormats format, File f) throws IOException {
		FileOutputStream fis = new FileOutputStream(f);
		BufferedOutputStream bis = new BufferedOutputStream(fis);
		OutputStream os = writeToOutputStream(obj, format, bis);
		os.close();
		return f;
	}
	public File writeToFile(Object obj, SOMFormats format, Path p) throws IOException {
		return writeToFile(obj, format, p.toFile());
	}
	public File writeToFile(Object obj, SOMFormats format,String filePath) throws IOException {
		return writeToFile(obj, format, Paths.get(filePath));
	}

	public SOMFormats getFormatFromFilename(String name) {
		if(name==null) {
			return null;
		}
		for (SOMFormats format : SOMFormats.values()) {
			String[] possibleFileExtensions = format.getFileExtensionString();
			for (int i = 0; i < possibleFileExtensions.length; i++) {
				String string = possibleFileExtensions[i];
				if (name.toLowerCase().endsWith(string.toLowerCase())) {
					return format;
				}
			}

		}
		return null;
	}

}
