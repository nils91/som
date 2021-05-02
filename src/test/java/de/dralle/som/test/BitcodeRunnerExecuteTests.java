package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.dralle.som.SOMBitcodeRunner;

class BitcodeRunnerExecuteTests {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testReturnCode0() throws IOException {
		File f=new File("test/fixtures/ab/n4_minimal_return0.ab");
		BufferedReader br = new BufferedReader(new FileReader(f));
		String entireFile="";
		String nextLine=null;
		do {
			nextLine=br.readLine();
			if(nextLine!=null) {
				entireFile+=nextLine;
			}			
		}while(nextLine!=null);
		br.close();
		SOMBitcodeRunner runner=new SOMBitcodeRunner(entireFile);
		assertTrue(runner.execute());
	}
	@Test
	void testReturnCode1() throws IOException {
		File f=new File("test/fixtures/ab/n4_minimal_return1.ab");
		BufferedReader br = new BufferedReader(new FileReader(f));
		String entireFile="";
		String nextLine=null;
		do {
			nextLine=br.readLine();
			if(nextLine!=null) {
				entireFile+=nextLine;
			}			
		}while(nextLine!=null);
		br.close();
		SOMBitcodeRunner runner=new SOMBitcodeRunner(entireFile);
		assertFalse(runner.execute());
	}
	@Test
	void testOpcodeREAD() throws IOException {
		File f=new File("test/fixtures/ab/test_read.ab");
		BufferedReader br = new BufferedReader(new FileReader(f));
		String entireFile="";
		String nextLine=null;
		do {
			nextLine=br.readLine();
			if(nextLine!=null) {
				entireFile+=nextLine;
			}			
		}while(nextLine!=null);
		br.close();
		SOMBitcodeRunner runner=new SOMBitcodeRunner(entireFile);
		assertTrue(runner.execute());
		//should have written accumulator to 1
		assertTrue(runner.getBit(0));
	}
	@Test
	void testOpcodeWRITE() throws IOException {
		File f=new File("test/fixtures/ab/test_write.ab");
		BufferedReader br = new BufferedReader(new FileReader(f));
		String entireFile="";
		String nextLine=null;
		do {
			nextLine=br.readLine();
			if(nextLine!=null) {
				entireFile+=nextLine;
			}			
		}while(nextLine!=null);
		br.close();
		SOMBitcodeRunner runner=new SOMBitcodeRunner(entireFile);
		assertTrue(runner.execute());
		//should have written bit 7 to 1
		assertTrue(runner.getBit(7));
	}
	@Test
	void testOpcodeNAND() throws IOException {
		File f=new File("test/fixtures/ab/test_nand.ab");
		BufferedReader br = new BufferedReader(new FileReader(f));
		String entireFile="";
		String nextLine=null;
		do {
			nextLine=br.readLine();
			if(nextLine!=null) {
				entireFile+=nextLine;
			}			
		}while(nextLine!=null);
		br.close();		
		SOMBitcodeRunner runner=new SOMBitcodeRunner(entireFile);
		boolean accBefore = runner.getBit(0);
		assertTrue(runner.execute());
		boolean accAfter = runner.getBit(0);
		//the NAND in that particular test should have inverted the accumulator
		assertNotEquals(accBefore, accAfter);
	}
	@Test
	void testOpcodeCJMP() throws IOException {
		File f=new File("test/fixtures/ab/test_cjmp.ab");
		BufferedReader br = new BufferedReader(new FileReader(f));
		String entireFile="";
		String nextLine=null;
		do {
			nextLine=br.readLine();
			if(nextLine!=null) {
				entireFile+=nextLine;
			}			
		}while(nextLine!=null);
		br.close();
		SOMBitcodeRunner runner=new SOMBitcodeRunner(entireFile);
		assertTrue(runner.execute());
		//if CJMP worked, it would´ve jumped over the command that set accumulator to 0
		assertTrue(runner.getBit(0));
	}
}
