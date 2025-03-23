package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.dralle.som.languages.hrac.model.HRACForDupBoundingRangeProvider;

class RangingTests {

	private static Stream<Arguments> provideRangeTestData() {// lower bound, upper bound, step, lower exclusive, upper
																// exclusive, expected
		return Stream.of(Arguments.of(1, 2, 1, false, false, new int[] { 1, 2 }),
				Arguments.of(1, 2, 1, true, false, new int[] { 2 }),
				Arguments.of(1, 2, 1, false, true, new int[] { 1 }), Arguments.of(1, 2, 1, true, true, new int[] {}),
				Arguments.of(1, 2, 2, false, false, new int[] { 1 }),
				Arguments.of(1, 2, 2, true, false, new int[] { 2 }),
				Arguments.of(1, 2, 2, false, true, new int[] { 1 }), Arguments.of(1, 2, 2, true, true, new int[] {}),
				Arguments.of(1, 2, 3, false, false, new int[] { 1 }),
				Arguments.of(1, 2, 3, true, false, new int[] { 2 }),
				Arguments.of(1, 2, 3, false, true, new int[] { 1 }), Arguments.of(1, 2, 3, true, true, new int[] {}),

				Arguments.of(1, 5, 1, false, false, new int[] { 1, 2, 3, 4, 5 }),
				Arguments.of(1, 5, 1, true, false, new int[] { 2, 3, 4, 5 }),
				Arguments.of(1, 5, 1, false, true, new int[] { 1, 2, 3, 4 }),
				Arguments.of(1, 5, 1, true, true, new int[] { 2, 3, 4 }),
				Arguments.of(1, 5, 2, false, false, new int[] { 1, 3, 5 }),
				Arguments.of(1, 5, 2, true, false, new int[] { 2, 4 }),
				Arguments.of(1, 5, 2, false, true, new int[] { 1, 3 }),
				Arguments.of(1, 5, 2, true, true, new int[] { 2, 4 }),
				Arguments.of(1, 5, 3, false, false, new int[] { 1, 4 }),
				Arguments.of(1, 5, 3, true, false, new int[] { 2, 5 }),
				Arguments.of(1, 5, 3, false, true, new int[] { 1, 4 }),
				Arguments.of(1, 5, 3, true, true, new int[] { 2 }),

				Arguments.of(2, 1, 1, false, false, new int[] { 2, 1 }),
				Arguments.of(2, 1, 1, true, false, new int[] { 1 }),
				Arguments.of(2, 1, 1, false, true, new int[] { 2 }), Arguments.of(2, 1, 1, true, true, new int[] {}),
				Arguments.of(2, 1, 2, false, false, new int[] { 2 }),
				Arguments.of(2, 1, 2, true, false, new int[] { 1 }),
				Arguments.of(2, 1, 2, false, true, new int[] { 2 }), Arguments.of(2, 1, 2, true, true, new int[] {}),
				Arguments.of(2, 1, 3, false, false, new int[] { 2 }),
				Arguments.of(2, 1, 3, true, false, new int[] { 1 }),
				Arguments.of(2, 1, 3, false, true, new int[] { 2 }), Arguments.of(2, 1, 3, true, true, new int[] {}),

				Arguments.of(5, 1, 1, false, false, new int[] { 5, 4, 3, 2, 1 }),
				Arguments.of(5, 1, 1, true, false, new int[] { 4, 3, 2, 1 }),
				Arguments.of(5, 1, 1, false, true, new int[] { 5, 4, 3, 2 }),
				Arguments.of(5, 1, 1, true, true, new int[] { 4, 3, 2 }),
				Arguments.of(5, 1, 2, false, false, new int[] { 5, 3, 1 }),
				Arguments.of(5, 1, 2, true, false, new int[] { 4, 2 }),
				Arguments.of(5, 1, 2, false, true, new int[] { 5, 3 }),
				Arguments.of(5, 1, 2, true, true, new int[] { 4, 2 }),
				Arguments.of(5, 1, 3, false, false, new int[] { 5, 2 }),
				Arguments.of(5, 1, 3, true, false, new int[] { 4, 1 }),
				Arguments.of(5, 1, 3, false, true, new int[] { 5, 2 }),
				Arguments.of(5, 1, 3, true, true, new int[] { 4 }),

				Arguments.of(1, 2, 2, false, false, new int[] { 1 }),
				Arguments.of(1, 2, 2, false, false, new int[] { 1 }),
				Arguments.of(1, 2, 2, false, false, new int[] { 1 }));
	}

	private HRACForDupBoundingRangeProvider hracForDup;

	@ParameterizedTest
	@MethodSource("provideRangeTestData")
	void hracRangeGen(int lower, int upper, int step, boolean rangeStartExclusive, boolean rangeEndExclusive,
			int[] expected) throws IOException {
		hracForDup.setRangeStart(lower);
		hracForDup.setRangeEnd(upper);
		hracForDup.setStepSize(step);
		hracForDup.setRangeStartBoundExclusive(rangeStartExclusive);
		hracForDup.setRangeEndBoundExclusive(rangeEndExclusive);
		assertArrayEquals(expected, hracForDup.getRangeAsIntArray(null));
	}

	@BeforeEach
	void setUp() throws Exception {
		hracForDup = new HRACForDupBoundingRangeProvider();
	}

}
