/**
 * 
 */
package de.dralle.som.writehooks;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import de.dralle.som.SOMBitcodeRunner;
import de.dralle.som.Util;
import de.dralle.som.writehooks.AbstractWriteHook;

/**
 * @author Nils Dralle
 *
 */
public class StdWriteHook extends AbstractWriteHook {
	private List<Boolean> readbuffer=new ArrayList();
	private List<Boolean> writebuffer=new ArrayList();
	@Override
	public boolean read(SOMBitcodeRunner runner) {
		if(readbuffer.isEmpty()) {
			InputStreamReader isr = new InputStreamReader(System.in, StandardCharsets.US_ASCII);
			int rad = 0;
			try {
				rad = isr.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < 8; i++) {
				readbuffer.add(Util.getBit(rad, 7-i));
			}
		}
		Boolean value = readbuffer.get(0);
		readbuffer.remove(0);
		return value;
	}

	/**
	 * Always return true to always allow reading from input.
	 */
	@Override
	public boolean hasDataAvailable() {
		return true;
	}

	@Override
	public boolean write(boolean accumulatorValue, SOMBitcodeRunner runner) {	
		writebuffer.add(accumulatorValue);
		if(writebuffer.size()>=8) {
			int c=Util.getAsUnsignedInt(writebuffer);
			System.out.print((char)c);
		}
		return false;
	}

}
