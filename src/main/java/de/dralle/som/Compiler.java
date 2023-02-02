/**
 * 
 */
package de.dralle.som;

import java.io.IOException;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import de.dralle.som.languages.hrac.HRACParser;
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.HRASParser;
import de.dralle.som.languages.hras.model.HRASModel;

/**
 * @author Nils
 *
 */
public class Compiler {
	private Object compile(Object sourceModel,SOMFormats sourceFormat,SOMFormats targetFormat){
		if(sourceFormat.equals(SOMFormats.HRAC)&&targetFormat.equals(SOMFormats.HRAS)){
			return compileHRACtoHRAS((HRACModel) sourceModel);
		}
		if(sourceFormat.equals(SOMFormats.HRAS)&&targetFormat.equals(SOMFormats.BIN)){
			return compileHRAStoMemspace((HRASModel) sourceModel);
		}
		if(sourceFormat.equals(SOMFormats.BIN)&&targetFormat.equals(SOMFormats.AB)){
			return memSpaceToABString((IMemspace) sourceModel);
		}
		if(sourceFormat.equals(SOMFormats.AB)&&targetFormat.equals(SOMFormats.BIN)){
			return abStringToMemspace((String) sourceModel);
		}
		return null;
	}
	
	public HRASModel compileHRACtoHRAS(HRACModel m) {
		return m.compileToHRAS();
	}
	public IMemspace abStringToMemspace(String s) {
		return abStringToMemspace(s.toCharArray());
	}
	public IMemspace compileHRAStoMemspace(HRASModel model) {
		return model.compileToMemspace();
	}

	public String memSpaceToABString(IMemspace memspace) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < memspace.getSize(); i++) {
			sb.append(memspace.getBit(i) ? '1' : '0');
		}
		return sb.toString();
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
		byte[] byteArray = new byte[bytes.size()];
		for (int i = 0; i < byteArray.length; i++) {
			byteArray[i] = bytes.get(i);
		}
		return byteArrayToMemspace(byteArray);
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
