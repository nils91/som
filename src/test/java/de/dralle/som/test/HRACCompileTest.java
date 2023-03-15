package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.dralle.som.Compiler;
import de.dralle.som.FileLoader;
import de.dralle.som.IMemspace;
import de.dralle.som.ISomMemspace;
import de.dralle.som.SOMBitcodeRunner;
import de.dralle.som.SOMFormats;
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASModel;

class HRACCompileTest {

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
	void testSimpleFforDup() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_for_simple.hrac",SOMFormats.HRAC);
		HRASModel hras= c.compile(model,SOMFormats.HRAC,SOMFormats.HRAS);
      		assertEquals(1+6+1, hras.getCommandCount());//1 added by compiler, 6 in loop (upper and lower are included) and 1 at the end
	}
	@Test
	void testRunningForDup() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_for_running.hrac",SOMFormats.HRAC);
		HRASModel hras= c.compile(model,SOMFormats.HRAC,SOMFormats.HRAS);
      		assertEquals(1+hras.getN()+2, hras.getCommandCount());//1 added by compiler, N in loop (upper and lower are included) and 2 at the end
	}
	@Test
	void testNestedRunningForDup() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_for_running_nested.hrac",SOMFormats.HRAC);
		HRASModel hras= c.compile(model,SOMFormats.HRAC,SOMFormats.HRAS);
      		assertEquals(1+hras.getN()+getSum(hras.getN()), hras.getCommandCount());//1 added by compiler, N in loop (upper and lower are included) and 2 at the end
	}
	@Test
	void testNestedRunningForDupPrecompiler() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_for_running_nested.hrac",SOMFormats.HRAC);
		HRACModel prec = model.clone();prec.precompile("", new HashMap<>());
		System.out.println(prec);
		}

	private int getSum(int n) {
		if(n==0) {
			return 0;
			
		}return n+getSum(n-1);
	}
	@Test
	void testFDSymbolGen() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_fd_smbol_gen.hrac",SOMFormats.HRAC);
		HRASModel hras=c.compile(model,SOMFormats.HRAC,SOMFormats.HRAS);
		IMemspace m= c.compile(model,SOMFormats.HRAC,SOMFormats.BIN);
      		assertNotNull(m);	}
	@Test
	void testFDCorrectAllocNumm() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_fd_smbol_gen2.hrac",SOMFormats.HRAC);
		HRASModel m= c.compile(model,SOMFormats.HRAC,SOMFormats.HRAS);
      	assertEquals(9+5, m.getSymbolCount());	}//9 symbols should be added by compiler
	@Test
	void testFDIndependentAlloc() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_fd_smbol_gen2.hrac",SOMFormats.HRAC);
		IMemspace m= c.compile(model,SOMFormats.HRAC,SOMFormats.BIN);
      		assertNotNull(new SOMBitcodeRunner((ISomMemspace) m).execute());	}
	@Test
	void testNReplAlloc() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_n_repl_alloc.hrac",SOMFormats.HRAC);
		model.precompile("", null);
		assertEquals(model.getN(),model.getSymbolByName("A").getBitCnt() ); }
}
