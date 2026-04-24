package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import de.dralle.som.Compiler;
import de.dralle.som.FileLoader;
import de.dralle.som.SOMFormats;
import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hrad.model.directive.HRADIntegerDirectiveValue;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrad.model.expressiontree.visitors.HRADDirectiveTreeCalculateValueVisitor;
import de.dralle.som.languages.hrav.model.HRAVModel;

class HRADTests {
	static List<File> hravFileProvider() {
		List<File> fileList = new ArrayList<>();
		getFiles(new File("test/fixtures/hrav"), fileList);
		if (fileList.isEmpty()) {
			// Make sure the list has at least on entry, but skip it in test, to make junit
			// happy
			fileList.add(null);
		}
		return fileList;
	}

	private static void getFiles(File folder, List<File> fileList) {
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					SOMFormats format = new FileLoader().getFormatFromFilename(file);
					if (format != null) {
						fileList.add(file);
					}
				} else if (file.isDirectory()) {
					getFiles(file, fileList);
				}
			}
		}
	}

	@ParameterizedTest
	@MethodSource("hravFileProvider")
	void HRAVParseTests(File file) throws IOException { // Test parsing all the HRAV files in fixtures
		if (file != null) {
			HRADModel model = (HRADModel) new FileLoader().loadFromFile(file, SOMFormats.HRAD);
			assertNotNull(model);
		}
	}

	@ParameterizedTest
	@MethodSource("hravFileProvider")
	void HRAVParseTestsSameN(File file) throws IOException { // Test parsing all the HRAV files in fixtures with hrad
																// and hrav and compare parsed n
		if (file != null) {
			HRADModel hradmodel = (HRADModel) new FileLoader().loadFromFile(file, SOMFormats.HRAD);
			HRAVModel hravmodel = (HRAVModel) new FileLoader().loadFromFile(file, SOMFormats.HRAV);
			assertEquals(hravmodel.getN(), ((HRADIntegerDirectiveValue) hradmodel.getDirectives().get("n")
					.accept(new HRADDirectiveTreeCalculateValueVisitor())).getValue().intValue());
		}
	}

	@ParameterizedTest
	@MethodSource("hravFileProvider")
	void HRAVParseTestsSameStart(File file) throws IOException { // Test parsing all the HRAV files in fixtures with
																	// hrad and hrav and compare parsed n
		if (file != null) {
			HRADModel hradmodel = (HRADModel) new FileLoader().loadFromFile(file, SOMFormats.HRAD);
			HRAVModel hravmodel = (HRAVModel) new FileLoader().loadFromFile(file, SOMFormats.HRAV);
			assertEquals(hravmodel.getStartAdress(), ((HRADIntegerDirectiveValue) hradmodel.getDirectives().get("start")
					.accept(new HRADDirectiveTreeCalculateValueVisitor())).getValue().intValue());
		}
	}

	@ParameterizedTest
	@MethodSource("hravFileProvider")
	void HRAVParseTestsCompileSameN(File file) throws IOException { // Test parsing all the HRAV files in fixtures with
																	// hrad and hrav and compare parsed n
		if (file != null) {
			HRADModel hradmodel = (HRADModel) new FileLoader().loadFromFile(file, SOMFormats.HRAD);
			HRAVModel hravmodel = new Compiler().compile(hradmodel, SOMFormats.HRAV);
			assertEquals(hravmodel.getN(), ((HRADIntegerDirectiveValue) hradmodel.getDirectives().get("n")
					.accept(new HRADDirectiveTreeCalculateValueVisitor())).getValue().intValue());
		}
	}

	@ParameterizedTest
	@MethodSource("hravFileProvider")
	void HRAVParseTestsCompileSameStart(File file) throws IOException { // Test parsing all the HRAV files in fixtures
																		// with hrad and hrav and compare parsed n
		if (file != null) {
			HRADModel hradmodel = (HRADModel) new FileLoader().loadFromFile(file, SOMFormats.HRAD);
			HRAVModel hravmodel = new Compiler().compile(hradmodel, SOMFormats.HRAV);
			assertEquals(hravmodel.getStartAdress(), ((HRADIntegerDirectiveValue) hradmodel.getDirectives().get("start")
					.accept(new HRADDirectiveTreeCalculateValueVisitor())).getValue().intValue());
		}
	}

	@Test
	void testsimpleDirectiveParseNotNull() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader().loadFromFile("test/fixtures/hrad/SimpleDirectiveParse.hrad");
		assertNotNull(hradmodel);
	}

	@Test
	void testsimpleDirectiveParseDirectivesParsed() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader().loadFromFile("test/fixtures/hrad/SimpleDirectiveParse.hrad");
		Map<String, HRADAbstractDirectiveExpressionTreeNode> parseDirectives = hradmodel.getDirectives();
		assertIterableEquals(Arrays.asList("a", "b", "c"), parseDirectives.keySet());
	}

	@Test
	void testsimpleDirectiveParseDirectivesParsedValue() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader().loadFromFile("test/fixtures/hrad/SimpleDirectiveParse.hrad");
		List<String> expectedDirectives = Arrays.asList("a", "b", "c");
		Map<String, HRADAbstractDirectiveExpressionTreeNode> parsedDirectives = hradmodel.getDirectives();
		for (Iterator iterator = expectedDirectives.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			HRADAbstractDirectiveExpressionTreeNode dValueTree = parsedDirectives.get(string);
			assertEquals(42,
					((HRADIntegerDirectiveValue) dValueTree.accept(new HRADDirectiveTreeCalculateValueVisitor()))
							.getValue().intValue());

		}
	}

	@Test
	void testComplexDirectiveParseNotNull() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/ComplexDirectiveParse.hrad");
		assertNotNull(hradmodel);
	}

	@Test
	void testComplexDirectiveParseDirectivesParsed() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/ComplexDirectiveParse.hrad");
		assertIterableEquals(Arrays.asList("a", "b", "c"), hradmodel.getDirectives().keySet());
	}

	@Test
	void testComplexDirectiveParseDirectivesParsedValue() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/ComplexDirectiveParse.hrad");
		List<String> expectedDirectives = Arrays.asList("a", "b", "c");
		Map<String, HRADAbstractDirectiveExpressionTreeNode> parsedDirectives = hradmodel.getDirectives();
		for (Iterator<String> iterator = expectedDirectives.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			HRADAbstractDirectiveExpressionTreeNode dValueTree = parsedDirectives.get(string);
			assertEquals(42,
					((HRADIntegerDirectiveValue) dValueTree.accept(new HRADDirectiveTreeCalculateValueVisitor()))
							.getValue().intValue());

		}
	}

	@Test
	void testsimpleDirectiveFunctionParseNotNull() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/SimpleDirectiveFunctionParse.hrad");
		assertNotNull(hradmodel);
	}

	@Test
	void testsimpleDirectiveFunctionParseDirectivesParsed() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/SimpleDirectiveFunctionParse.hrad");
		assertIterableEquals(Arrays.asList("a", "f", "b"), hradmodel.getDirectives().keySet());
	}

	@Test
	void testsimpleDirectiveFunctionParseDirectivesParsedValue() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/SimpleDirectiveFunctionParse.hrad");

		Map<String, HRADAbstractDirectiveExpressionTreeNode> parsedDirectives = hradmodel.getDirectives();

		HRADAbstractDirectiveExpressionTreeNode dValueTree = parsedDirectives.get("b");
		assertEquals(1, ((HRADIntegerDirectiveValue) dValueTree.accept(new HRADDirectiveTreeCalculateValueVisitor()))
				.getValue().intValue());

	}

	@Test
	void testComplexDirectiveFunctionParseNotNull() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/ComplexDirectiveFunctionParse.hrad");
		assertNotNull(hradmodel.getN());
	}

	@Test
	void testComplexDirectiveFunctionParseDirectivesParsed() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/ComplexDirectiveFunctionParse.hrad");
		assertIterableEquals(Arrays.asList("a", "f", "b"), hradmodel.getDirectives().keySet());
	}

	@Test
	void testComplexDirectiveFunctionParseDirectivesParsedValue() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/ComplexDirectiveFunctionParse.hrad");
		Map<String, HRADAbstractDirectiveExpressionTreeNode> parsedDirectives = hradmodel.getDirectives();

		HRADAbstractDirectiveExpressionTreeNode dValueTree = parsedDirectives.get("b");
		assertEquals(1, ((HRADIntegerDirectiveValue) dValueTree.accept(new HRADDirectiveTreeCalculateValueVisitor()))
				.getValue().intValue());

	}
	@Test
	void testUnknownDirectiveReturn0() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/directive_unknown.hrad");
		Map<String, HRADAbstractDirectiveExpressionTreeNode> parsedDirectives = hradmodel.getDirectives();

		HRADAbstractDirectiveExpressionTreeNode dValueTree = parsedDirectives.get("c");
		assertEquals(0, ((HRADIntegerDirectiveValue) dValueTree.accept(new HRADDirectiveTreeCalculateValueVisitor()))
				.getValue().intValue());

	}
	@Test
	void testGciFindBackwardForward() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/gci_find_backward_forward.hrad");
		Map<String, HRADAbstractDirectiveExpressionTreeNode> compiledDirectives = new LinkedHashMap<String, HRADAbstractDirectiveExpressionTreeNode>();

		hradmodel.compileToHRAV(compiledDirectives, null);
		HRADAbstractDirectiveExpressionTreeNode dValueTree = compiledDirectives.get("c");
		assertEquals(1, ((HRADIntegerDirectiveValue) dValueTree.accept(new HRADDirectiveTreeCalculateValueVisitor()))
				.getValue().intValue());

	}
	@Test
	void testGciFindBackward() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/gci_find_backward.hrad");
		Map<String, HRADAbstractDirectiveExpressionTreeNode> compiledDirectives = new LinkedHashMap<String, HRADAbstractDirectiveExpressionTreeNode>();

		hradmodel.compileToHRAV(compiledDirectives, null);
		HRADAbstractDirectiveExpressionTreeNode dValueTree = compiledDirectives.get("c");
		assertEquals(1, ((HRADIntegerDirectiveValue) dValueTree.accept(new HRADDirectiveTreeCalculateValueVisitor()))
				.getValue().intValue());

	}
	@Test
	void testGciFindCorrect() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/gci_find_correct.hrad");
		Map<String, HRADAbstractDirectiveExpressionTreeNode> compiledDirectives = new LinkedHashMap<String, HRADAbstractDirectiveExpressionTreeNode>();

		hradmodel.compileToHRAV(compiledDirectives, null);
		HRADAbstractDirectiveExpressionTreeNode dValueTree = compiledDirectives.get("c");
		assertEquals(4, ((HRADIntegerDirectiveValue) dValueTree.accept(new HRADDirectiveTreeCalculateValueVisitor()))
				.getValue().intValue());

	}
	@Test
	void testGciFindForwardClosest() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/gci_find_forward_closest.hrad");
		Map<String, HRADAbstractDirectiveExpressionTreeNode> compiledDirectives = new LinkedHashMap<String, HRADAbstractDirectiveExpressionTreeNode>();

		hradmodel.compileToHRAV(compiledDirectives, null);
		HRADAbstractDirectiveExpressionTreeNode dValueTree = compiledDirectives.get("c");
		assertEquals(4, ((HRADIntegerDirectiveValue) dValueTree.accept(new HRADDirectiveTreeCalculateValueVisitor()))
				.getValue().intValue());

	}
	@Test
	void testGciFindForward() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/gci_find_forward.hrad");
		Map<String, HRADAbstractDirectiveExpressionTreeNode> compiledDirectives = new LinkedHashMap<String, HRADAbstractDirectiveExpressionTreeNode>();

		hradmodel.compileToHRAV(compiledDirectives, null);
		HRADAbstractDirectiveExpressionTreeNode dValueTree = compiledDirectives.get("c");
		assertEquals(4, ((HRADIntegerDirectiveValue) dValueTree.accept(new HRADDirectiveTreeCalculateValueVisitor()))
				.getValue().intValue());

	}
	@Test
	void testGciFindSelfName() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/gci_find_self_name.hrad");
		Map<String, HRADAbstractDirectiveExpressionTreeNode> compiledDirectives = new LinkedHashMap<String, HRADAbstractDirectiveExpressionTreeNode>();

		hradmodel.compileToHRAV(compiledDirectives, null);
		HRADAbstractDirectiveExpressionTreeNode dValueTree = compiledDirectives.get("c");
		assertEquals(3, ((HRADIntegerDirectiveValue) dValueTree.accept(new HRADDirectiveTreeCalculateValueVisitor()))
				.getValue().intValue());

	}
	@Test
	void testGciFindSelfNoParam() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/gci_find_self_noparam.hrad");
		Map<String, HRADAbstractDirectiveExpressionTreeNode> compiledDirectives = new LinkedHashMap<String, HRADAbstractDirectiveExpressionTreeNode>();

		hradmodel.compileToHRAV(compiledDirectives, null);
		HRADAbstractDirectiveExpressionTreeNode dValueTree = compiledDirectives.get("c");
		assertEquals(3, ((HRADIntegerDirectiveValue) dValueTree.accept(new HRADDirectiveTreeCalculateValueVisitor()))
				.getValue().intValue());

	}
	@Test
	void testGciNotFindForward() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/gci_not_find_forward_limit.hrad");
		Map<String, HRADAbstractDirectiveExpressionTreeNode> compiledDirectives = new LinkedHashMap<String, HRADAbstractDirectiveExpressionTreeNode>();

		hradmodel.compileToHRAV(compiledDirectives, null);
		HRADAbstractDirectiveExpressionTreeNode dValueTree = compiledDirectives.get("c");
		assertEquals(-1, ((HRADIntegerDirectiveValue) dValueTree.accept(new HRADDirectiveTreeCalculateValueVisitor()))
				.getValue().intValue());

	}
	@Test
	void testGciNotFindUnknown() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/gci_not_find_unknown.hrad");
		Map<String, HRADAbstractDirectiveExpressionTreeNode> compiledDirectives = new LinkedHashMap<String, HRADAbstractDirectiveExpressionTreeNode>();

		hradmodel.compileToHRAV(compiledDirectives, null);
		HRADAbstractDirectiveExpressionTreeNode dValueTree = compiledDirectives.get("c");
		assertEquals(-1, ((HRADIntegerDirectiveValue) dValueTree.accept(new HRADDirectiveTreeCalculateValueVisitor()))
				.getValue().intValue());

	}
	@Test
	void testSimpleTestCompile() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/test_simple_compile.hrad");
		HRAVModel hravModel=new Compiler().compile(hradmodel, SOMFormats.HRAV);
		
		assertEquals(3, hravModel.getCommands().size());

	}
	@Test
	void testSciForward() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/test_sci_jump_forward_compile.hrad");
		HRAVModel hravModel=new Compiler().compile(hradmodel, SOMFormats.HRAV);
		
		assertEquals(2, hravModel.getCommands().size());

	}
	@Test
	void testSciBackward() throws IOException {
		HRADModel hradmodel = (HRADModel) new FileLoader()
				.loadFromFile("test/fixtures/hrad/test_sci_jump_backward_compile.hrad");
		Map<String, HRADAbstractDirectiveExpressionTreeNode> compiledDirectives = new LinkedHashMap<String, HRADAbstractDirectiveExpressionTreeNode>();

		
		HRAVModel hravModel=hradmodel.compileToHRAV(compiledDirectives, null);
		
		assertEquals(4, hravModel.getCommands().size());

	}
}
