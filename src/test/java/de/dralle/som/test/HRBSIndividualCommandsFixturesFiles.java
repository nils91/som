/**
 * 
 */
package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import de.dralle.som.AbstractCommandAddressListenerDP;
import de.dralle.som.AbstractUnconditionalDebugPoint;
import de.dralle.som.Compiler;
import de.dralle.som.FileLoader;
import de.dralle.som.IMemspace;
import de.dralle.som.ISomMemspace;
import de.dralle.som.Opcode;
import de.dralle.som.SOMBitcodeRunner;
import de.dralle.som.SOMFormats;
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hrbs.model.HRBSModel;

/**
 * @author Nils
 *
 */
class HRBSIndividualCommandsFixturesFiles {

	private Compiler c;
	private FileLoader f;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		c = new Compiler();
		f=new FileLoader();
	}
                         
	@AfterEach
	void tearDown() throws Exception {
	}
	@Test
	@Timeout(10)
	void testINV0() throws IOException {
		String hrbsCode="import \"test/fixtures/hrbs/individual_commands/INV0.hrbs\"\n\nMAIN:\n\tDEBUG: INV0;";
		HRBSModel hrbsModel=(HRBSModel) f.loadFromString(hrbsCode, SOMFormats.HRBS);
		HRACModel hracModel=c.compile(hrbsModel, SOMFormats.HRBS	, SOMFormats.HRAC);
		HRASModel hrasModel=c.compile(hrbsModel, SOMFormats.HRBS	, SOMFormats.HRAS);
		IMemspace memspace=c.compile(hrbsModel, SOMFormats.HRBS	, SOMFormats.BIN);
		int dbgAdr = hrasModel.resolveSymbolToAddress("MAIN_null_DEBUG");
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		runner.addDebugPoint(new AbstractCommandAddressListenerDP("DEBUG",dbgAdr) {
			
			@Override
			public boolean trigger(int cmdAddress, Opcode op, int tgtAddress, ISomMemspace memspace) {
				memspace.setBit(0, false);
				return true;
			}
		});
		runner.execute();
		// should have written accumulator to 1
		assertTrue(runner.getMemspace().getAccumulatorValue());
	}

}
