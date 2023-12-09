/**
 * 
 */
package de.dralle.som;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import de.dralle.som.languages.hrac.HRACParser;
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.HRASParser;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hrav.HRAVParser;
import de.dralle.som.languages.hrav.model.HRAVModel;
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
		ClassLoader cl = getClass().getClassLoader();
		InputStream is = cl.getResourceAsStream(path);
		BufferedInputStream bis = new BufferedInputStream(is);
		HRBSParser hp = new HRBSParser();
		HRBSModel m = hp.parse(bis);
		bis.close();
		return m;
	}

	public HRBSModel readHRBSFileInternalFirst(String path) throws IOException {
		try {
			return readHRBSFileFromInternal(path);
		} catch (IOException e) {
		}
		return readHRBSFile(path);
	}

	public HRBSModel loadHRBSByName(String name) throws IOException {
		return readHRBSFileInternalFirst("includes/hrbs/" + name + ".hrbs");
	}

	public <T> T loadByName(String name, SOMFormats format) {
		List<String> paths = new ArrayList<>();
		String[] possibleFileExt = format.getFileExtensionString();
		for (int i = 0; i < possibleFileExt.length; i++) {
			String string = possibleFileExt[i];
			if (!string.startsWith(".")) {
				string = "." + string;
			}
			// Paths to be used to get ressources need forward slashes, so to mantain
			// compatibility with windows, dont use Paths.get. It works with eclipse, but
			// not with the exportet jar
			paths.add("includes" + "/" + format.name() + "/" + name + string);
			paths.add("includes" + "/" + format.name().toLowerCase() + "/" + name + string);
			paths.add("includes" + "/" + format.name().toUpperCase() + "/" + name + string);
			paths.add("includes" + "/" + format.getShortName() + "/" + name + string);
			paths.add("includes" + "/" + format.getShortName().toLowerCase() + "/" + name + string);
			paths.add("includes" + "/" + format.getShortName().toUpperCase() + "/" + name + string);

			// Including this anyway because eclipse apparently messes with things
			paths.add(Paths.get("includes", format.name(), name + string).toString());
			paths.add(Paths.get("includes", format.name().toLowerCase(), name + string).toString());
			paths.add(Paths.get("includes", format.name().toUpperCase(), name + string).toString());
			paths.add(Paths.get("includes", format.getShortName(), name + string).toString());
			paths.add(Paths.get("includes", format.getShortName().toLowerCase(), name + string).toString());
			paths.add(Paths.get("includes", format.getShortName().toUpperCase(), name + string).toString());

		}
		// Alright then, why not include more shit, ECLIPSE?
		List<String> apaths=new ArrayList<>();
		for (String string : paths) {			
			if (!string.startsWith("/")) {
				apaths.add("/" + string);
			}
			apaths.add("/" + string);
			apaths.add("\\" + string);
		}
		paths.addAll(apaths);
		T model = null;
		for (String string : paths) {
			try {
				ClassLoader cl = getClass().getClassLoader();
				String aStr = string;
				InputStream is = cl.getResourceAsStream(aStr);
				BufferedInputStream bis = new BufferedInputStream(is);
				try {
					model = (T) loadFromInputStream(bis, format);
				} catch (Exception e) {
				}
				try {
					bis.close();
				} catch (Exception e) {
				}
				if (model != null) {
					break;
				}
			} catch (Exception e) {

			}
		}
		if (model == null) {
			for (String string : paths) {
				try {
					model = loadFromFile(string, format);
				} catch (Exception e) {

				}
				if (model != null) {
					break;
				}
			}
		}
		return model;
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

	public void writeBinaryFile(byte[] content, String path) throws IOException {
		File f = new File(path);
		FileOutputStream fis = new FileOutputStream(f);
		BufferedOutputStream bis = new BufferedOutputStream(fis);
		bis.write(content);
		bis.close();
	}

	public void writeBinaryFile(IMemspace memspace, String path) throws IOException {
		writeBinaryFile(c.memspaceToByteArray(memspace), path);
	}

	public IMemspace loadCompressedBinaryFile(String path) throws IOException {
		ZipFile f = new ZipFile(path);
		ZipEntry somBinary = f.getEntry("BIN");
		InputStream fis = f.getInputStream(somBinary);
		BufferedInputStream bis = new BufferedInputStream(fis);
		List<Byte> bytes = new ArrayList<>();
		int b;
		while ((b = bis.read()) != -1) {
			bytes.add((byte) b);
		}
		bis.close();
		f.close();
		IMemspace m = c.byteListToMemspace(bytes);
		return m;
	}

	public byte[] loadCompressedBinaryFileFromStream(InputStream is) throws IOException {
		ZipInputStream f = new ZipInputStream(is);
		ZipEntry somBinary = f.getNextEntry();
		List<Byte> bytes = new ArrayList<>();
		int b;
		while ((b = f.read()) != -1) {
			bytes.add((byte) b);
		}
		f.close();
		return c.byteListToByteArray(bytes);
	}

	public void writeCompressedBinaryFile(byte[] content, String path) throws IOException {
		File f = new File(path);
		FileOutputStream fis = new FileOutputStream(f);
		BufferedOutputStream bis = new BufferedOutputStream(fis);
		ZipOutputStream zos = new ZipOutputStream(bis);
		ZipEntry ze = new ZipEntry("BIN");
		zos.putNextEntry(ze);
		zos.write(content);
		zos.closeEntry();
		zos.close();
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

	public <T> T loadFromFile(String path, SOMFormats sourceFormat) throws IOException {
		return (T) loadFromFile(new File(path), sourceFormat);
	}

	public Object loadFromFile(File f) throws IOException {
		return loadFromFile(f, getFormatFromFilename(f.getName()));
	}

	public Object loadFromPath(Path p) throws IOException {
		return loadFromFile(p.toFile());
	}

	public Object loadFromFile(String path) throws IOException {
		return loadFromFile(new File(path));
	}

	public Object loadFromInputStream(InputStream source, SOMFormats sourceFormat) throws IOException {
		if (sourceFormat.equals(SOMFormats.HRAC)) {
			return new HRACParser().parse(source);
		}
		if (sourceFormat.equals(SOMFormats.HRAP)) {
			return new HRACParser().parse(source);
		}
		if (sourceFormat.equals(SOMFormats.HRAS)) {
			return new HRASParser().parse(source);
		}
		if (sourceFormat.equals(SOMFormats.HRAV)) {
			return new HRAVParser().parse(source);
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
		if (sourceFormat.equals(SOMFormats.B64)) {
			Scanner s = new Scanner(source, StandardCharsets.UTF_8.name()).useDelimiter("\\A");
			String result = s.hasNext() ? s.next() : "";
			s.close();
			return result;
		}
		if (sourceFormat.equals(SOMFormats.CBIN)) {
			List<Byte> bytes = new ArrayList<>();
			int b;
			while ((b = source.read()) != -1) {
				bytes.add((byte) b);
			}
			byte[] m = new Compiler().byteListToByteArray(bytes);
			return m;
		}
		if (sourceFormat.equals(SOMFormats.IMAGE)) {
			BufferedImage img = ImageIO.read(source);
			return img;
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
		} else if (format.equals(SOMFormats.CBIN)) {
			byte[] arr = (byte[]) obj;
			for (int i = 0; i < arr.length; i++) {
				byte c = arr[i];
				out.write(c);
			}
			return out;
		} else if (format.equals(SOMFormats.IMAGE)) {
			// create the object of ByteArrayOutputStream class
			ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();

			// write the image into the object of ByteArrayOutputStream class
			ImageIO.write((RenderedImage) obj, "png", outStreamObj);

			// create the byte array from image
			byte[] arr = outStreamObj.toByteArray();
			outStreamObj.close();
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
		if (format.equals(SOMFormats.HRAP)) {
			HRACModel m = (HRACModel) obj;
			String str = m.asCode();
			out.write(str);
		}
		if (format.equals(SOMFormats.HRAS)) {
			HRASModel m = (HRASModel) obj;
			String str = m.asCode();
			out.write(str);
		}
		if (format.equals(SOMFormats.HRAV)) {
			HRAVModel m = (HRAVModel) obj;
			String str = m.asCode();
			out.write(str);
		}
		if (format.equals(SOMFormats.AB)) {
			String m = (String) obj;
			out.write(m);
		}
		if (format.equals(SOMFormats.B64)) {
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

	public File writeToFile(Object obj, SOMFormats format, String filePath) throws IOException {
		return writeToFile(obj, format, Paths.get(filePath));
	}

	public SOMFormats getFormatFromFilename(String name) {
		if (name == null) {
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

	public SOMFormats getFormatFromName(String name) {
		if (name == null) {
			return null;
		}
		for (SOMFormats format : SOMFormats.values()) {
			if (format.name().toLowerCase().equals(name.toLowerCase())) {
				return format;
			}
			if (format.getShortName().toLowerCase().equals(name.toLowerCase())) {
				return format;
			}
		}
		return null;
	}

	public SOMFormats getFormatFromFilename(File file) {
		return getFormatFromFilename(file.getName());
	}
}
