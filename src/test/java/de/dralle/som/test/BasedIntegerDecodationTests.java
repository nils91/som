package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.dralle.som.Util;

class BasedIntegerDecodationTests {

	public static Stream<Arguments> generateBasePrefixes() {
		Stream.Builder<Arguments> builder = Stream.builder();
		for (int i = 2; i <= 64; i++) {
			String generatedString = i + "b";
			builder.add(Arguments.of(i, generatedString));
		}
		return builder.build();
	}

	@ParameterizedTest
	@MethodSource("generateBasePrefixes")
	void testBaseExtraction(int expectedBase, String intPrefix) {
		assertEquals(expectedBase, Util.getBaseFromPrefix(intPrefix));
	}

	public static Stream<Arguments> generateDecodeExamples() {
		return Stream.of(Arguments.of(2, "2b10"), Arguments.of(6, "3b20"), Arguments.of(12, "4b30"),
				Arguments.of(2, "0b10"), Arguments.of(16, "0o20"), Arguments.of(30, "0d30"), Arguments.of(16, "0x10"),
				Arguments.of(32, "0h20"), Arguments.of(30, "30"), Arguments.of(63, "0x3f"), Arguments.of(3856, "0xf10"),
				Arguments.of(-32, "-0h20"), Arguments.of(-30, "-30"));
	}

	

	@ParameterizedTest
	@MethodSource("generateDecodeExamples")
	void testIntDecode(int expectedValue, String str) {
		assertEquals(expectedValue, Util.decodeInt(str));
	}

}
