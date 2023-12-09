/**
 * 
 */
package de.dralle.som;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Target;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;

import de.dralle.som.languages.hrac.HRACParser;
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.HRASParser;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hrav.model.HRAVModel;
import de.dralle.som.languages.hrbs.model.HRBSModel;

/**
 * @author Nils
 *
 */
public class Compiler {
	public Compiler() {

	}

	public List<SOMFormats> findCompilePath(SOMFormats start, SOMFormats target) {
		return findCompilePath(start, target, null);
	}

	public List<SOMFormats> findCompilePath(SOMFormats start, SOMFormats target, List<SOMFormats> path) {
		if (path == null) {
			path = new ArrayList<>();
		}
		List<SOMFormats> cPath = new ArrayList<>();
		if (!path.contains(start)) {
			path.add(start);
			cPath.add(start);
		} else {
			return null;
		}
		if (start != null && start.equals(target)) {
			return cPath;
		} else {
			SOMFormats[] availTargets = ATOMIC_COMPILE_PATHS.get(start);
			if (availTargets == null) {
				return null;
			}
			for (int i = 0; i < availTargets.length; i++) {
				SOMFormats somFormats = availTargets[i];
				List<SOMFormats> ccPathh = findCompilePath(somFormats, target, path);
				if (ccPathh != null) {
					cPath.addAll(ccPathh);
					return cPath;
				}
			}
			return null;
		}
	}

	public static final Map<SOMFormats, SOMFormats[]> ATOMIC_COMPILE_PATHS = Stream
			.of(new AbstractMap.SimpleImmutableEntry<SOMFormats, SOMFormats[]>(SOMFormats.AB,
					new SOMFormats[] { SOMFormats.BIN }),
					new AbstractMap.SimpleImmutableEntry<SOMFormats, SOMFormats[]>(SOMFormats.BIN,
							new SOMFormats[] { SOMFormats.AB, SOMFormats.CBIN, SOMFormats.IMAGE, SOMFormats.B64,SOMFormats.HRAV }),
					new AbstractMap.SimpleImmutableEntry<SOMFormats, SOMFormats[]>(SOMFormats.HRAV,
							new SOMFormats[] { SOMFormats.BIN,SOMFormats.HRAS }),
					new AbstractMap.SimpleImmutableEntry<SOMFormats, SOMFormats[]>(SOMFormats.HRAS,
							new SOMFormats[] { SOMFormats.HRAV,SOMFormats.HRAC }),
					new AbstractMap.SimpleImmutableEntry<SOMFormats, SOMFormats[]>(SOMFormats.HRAC,
							new SOMFormats[] { SOMFormats.HRAS,SOMFormats.HRBS, SOMFormats.HRAP }),
					new AbstractMap.SimpleImmutableEntry<SOMFormats, SOMFormats[]>(SOMFormats.HRAP,
							new SOMFormats[] { SOMFormats.HRAC }),
					new AbstractMap.SimpleImmutableEntry<SOMFormats, SOMFormats[]>(SOMFormats.HRBS,
							new SOMFormats[] { SOMFormats.HRAC }),
					new AbstractMap.SimpleImmutableEntry<SOMFormats, SOMFormats[]>(SOMFormats.CBIN,
							new SOMFormats[] { SOMFormats.BIN }),
					new AbstractMap.SimpleImmutableEntry<SOMFormats, SOMFormats[]>(SOMFormats.IMAGE,
							new SOMFormats[] { SOMFormats.BIN }),
					new AbstractMap.SimpleImmutableEntry<SOMFormats, SOMFormats[]>(SOMFormats.B64,
							new SOMFormats[] { SOMFormats.BIN }))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
public HRACModel compileHRAS2HRAC(HRASModel m) {
	return HRACModel.compileFromHRAS(m);
}
	public <T> T compile(Object sourceModel, SOMFormats sourceFormat, SOMFormats targetFormat) {
		List<SOMFormats> cPath = findCompilePath(sourceFormat, targetFormat);
		if (cPath == null || cPath.size() == 0) {
			return null;
		}
		if (cPath.size() == 1) {
			return (T) sourceModel;
		} else if (cPath.size() == 2) {
			return compileDirect(sourceModel, cPath.get(0), cPath.get(1));
		} else {
			Object somModel = sourceModel;
			for (int i = 0; i < cPath.size() - 1; i++) {
				SOMFormats compileStartFormat = cPath.get(i);
				SOMFormats compileTargetFormat = cPath.get(i + 1);
				somModel = compileDirect(somModel, compileStartFormat, compileTargetFormat);

			}
			return (T) somModel;
		}
	}

	public <T> T compileDirect(Object sourceModel, SOMFormats sourceFormat, SOMFormats targetFormat) {
		if (sourceFormat.equals(SOMFormats.HRBS) && targetFormat.equals(SOMFormats.HRAC)) {
			return (T) compileHRBStoHRAC((HRBSModel) sourceModel);
		}
		if (sourceFormat.equals(SOMFormats.HRAC) && targetFormat.equals(SOMFormats.HRAS)) {
			return (T) compileHRACtoHRAS((HRACModel) sourceModel);
		}if (sourceFormat.equals(SOMFormats.HRAC) && targetFormat.equals(SOMFormats.HRBS)) {
			return (T) compileHRAC2HRBS((HRACModel) sourceModel);
		}
		if (sourceFormat.equals(SOMFormats.HRAC) && targetFormat.equals(SOMFormats.HRAP)) {
			return (T) compileHRAC2HRAP((HRACModel) sourceModel);
		}
		if (sourceFormat.equals(SOMFormats.HRAP) && targetFormat.equals(SOMFormats.HRAC)) {
			return (T) ((HRACModel) sourceModel); //no compilation needed. HRAP is no language in itself, but a modified ("precompiled") version of HRAC
		}
		if (sourceFormat.equals(SOMFormats.HRAS) && targetFormat.equals(SOMFormats.HRAV)) {
			return (T) compileHRAStoHRAV((HRASModel) sourceModel);
		}
		if (sourceFormat.equals(SOMFormats.HRAS) && targetFormat.equals(SOMFormats.HRAC)) {
			return (T) compileHRAS2HRAC((HRASModel) sourceModel);
		}
		if (sourceFormat.equals(SOMFormats.HRAV) && targetFormat.equals(SOMFormats.BIN)) {
			return (T) compileHRAVtoMemspace((HRAVModel) sourceModel);
		}
		if (sourceFormat.equals(SOMFormats.HRAV) && targetFormat.equals(SOMFormats.HRAS)) {
			return (T) HRASModel.compileFromHRAV((HRAVModel) sourceModel);
		}
		if (sourceFormat.equals(SOMFormats.BIN) && targetFormat.equals(SOMFormats.AB)) {
			return (T) memSpaceToABString((IMemspace) sourceModel);
		}
		if (sourceFormat.equals(SOMFormats.BIN) && targetFormat.equals(SOMFormats.CBIN)) {
			return (T) compressMemspace((IMemspace) sourceModel);
		}
		if (sourceFormat.equals(SOMFormats.CBIN) && targetFormat.equals(SOMFormats.BIN)) {
			return (T) compressedArrayToMemspace((byte[]) sourceModel);
		}
		if (sourceFormat.equals(SOMFormats.IMAGE) && targetFormat.equals(SOMFormats.BIN)) {
			return (T) Image2Memspace((RenderedImage) sourceModel);
		}
		if (sourceFormat.equals(SOMFormats.BIN) && targetFormat.equals(SOMFormats.IMAGE)) {
			return (T) memspace2Image((IMemspace) sourceModel);
		}
		if (sourceFormat.equals(SOMFormats.BIN) && targetFormat.equals(SOMFormats.HRAV)) {
			return (T) HRAVModel.compileFromMemspace((IMemspace) sourceModel);
		}
		if (sourceFormat.equals(SOMFormats.AB) && targetFormat.equals(SOMFormats.BIN)) {
			return (T) abStringToMemspace((String) sourceModel);
		}
		if (sourceFormat.equals(SOMFormats.B64) && targetFormat.equals(SOMFormats.BIN)) {
			return (T) base64String2Memspace((String) sourceModel);
		}
		if (sourceFormat.equals(SOMFormats.BIN) && targetFormat.equals(SOMFormats.B64)) {
			return (T) memspace2Base64String((IMemspace) sourceModel);
		}
		return null;
	}

	private HRBSModel compileHRAC2HRBS(HRACModel sourceModel) {
		return HRBSModel.compileFromHRAC(sourceModel, "MAIN");
	}
	private HRACModel compileHRAC2HRAP(HRACModel sourceModel) {
		HRACModel clopne = sourceModel.clone();
		clopne.precompile("", new HashMap<>(), true);
		return clopne;
	}

	private String memspace2Base64String(IMemspace sourceModel) {
		byte[] data = memspaceToByteArray(sourceModel);
		return new String(Base64.getEncoder().encode(data));
	}

	private IMemspace base64String2Memspace(String sourceModel) {
		byte[] arr = Base64.getDecoder().decode(sourceModel);
		return new ByteArrayMemspace(arr);
	}

	private IMemspace Image2Memspace(RenderedImage sourceModel) {
		byte[] arr = Util.image2ByteArray(sourceModel);
		ByteArrayMemspace mem = new ByteArrayMemspace(arr);
		return mem;
	}

	private RenderedImage memspace2Image(IMemspace sourceModel) {
		byte[] data = memspaceToByteArray(sourceModel);
		return Util.byteArray2Image(data);
	}

	public HRACModel compileHRBStoHRAC(HRBSModel m) {
		return m.compileToHRAC("GL", null, "HRBS_START");
	}

	public HRASModel compileHRACtoHRAS(HRACModel m) {
		return m.compileToHRAS();
	}
	public HRAVModel compileHRAStoHRAV(HRASModel m) {
		return m.compileToHRAV();
	}
	public IMemspace abStringToMemspace(String s) {
		return abStringToMemspace(s.toCharArray());
	}

	public IMemspace compileHRAVtoMemspace(HRAVModel model) {
		return model.compileToMemspace();
	}

	public String memSpaceToABString(IMemspace memspace) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < memspace.getSize(); i++) {
			sb.append(memspace.getBit(i) ? '1' : '0');
		}
		return sb.toString();
	}

	public byte[] compressMemspace(IMemspace memspace) {
		ByteArrayMemspace bam = null;
		if (memspace instanceof ByteArrayMemspace) {
			bam = (ByteArrayMemspace) memspace;
		} else {
			bam = new ByteArrayMemspace();
			bam.copy(memspace);
		}
		byte[] unc = bam.getUnderlyingByteArray();
		// Create a ByteArrayOutputStream to write the compressed data to
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		ZipEntry entry = new ZipEntry("BIN");
		entry.setSize(unc.length);
		try {
			zos.putNextEntry(entry);
			zos.write(unc);
			zos.closeEntry();
			zos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	public IMemspace compressedArrayToMemspace(byte[] content) {
		ByteArrayInputStream bais = new ByteArrayInputStream(content);
		ZipInputStream zis = new ZipInputStream(bais);
		try {
			zis.getNextEntry();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Create a ByteArrayOutputStream to write the decompressed data to
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// Create a buffer for reading and writing the data
		byte[] buffer = new byte[1024];
		int len;

		// Read and write the decompressed data to the ByteArrayOutputStream
		try {
			while ((len = zis.read(buffer)) > 0) {
				baos.write(buffer, 0, len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Get the decompressed data as a byte array
		byte[] data = baos.toByteArray();
		ByteArrayMemspace bam = new ByteArrayMemspace(data);
		return bam;

	}

	public IMemspace booleanListToMemspace(List<Boolean> bits) {
		ISomMemspace m = new BooleanArrayMemspace(bits.size());
		for (int i = 0; i < bits.size(); i++) {
			m.setBit(i, bits.get(i));
		}
		int n = m.getN();
		m.resize((int) Math.pow(2, n), true);
		return m;
	}

	public IMemspace abStringToMemspace(char[] chars) {
		List<Boolean> bits = new ArrayList<>();
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
		return booleanListToMemspace(bits);
	}

	public IMemspace byteListToMemspace(List<Byte> bytes) {
		return byteArrayToMemspace(byteListToByteArray(bytes));
	}

	public byte[] byteListToByteArray(List<Byte> bytes) {
		byte[] byteArray = new byte[bytes.size()];
		for (int i = 0; i < byteArray.length; i++) {
			byteArray[i] = bytes.get(i);
		}
		return byteArray;
	}

	public IMemspace byteArrayToMemspace(byte[] byteArray) {
		ISomMemspace m = new ByteArrayMemspace(byteArray);
		int n = m.getN();
		m.resize((int) Math.pow(2, n), true);
		return m;
	}

	public byte[] memspaceToByteArray(IMemspace memspace) {
		ByteArrayMemspace helper = new ByteArrayMemspace();
		if (memspace instanceof ByteArrayMemspace) {
			helper = (ByteArrayMemspace) memspace;
		} else {
			helper.resize(memspace.getSize(), false);
			for (int i = 0; i < memspace.getSize(); i++) {
				helper.setBit(i, memspace.getBit(i));
			}
		}
		return helper.getUnderlyingByteArray();
	}

}
