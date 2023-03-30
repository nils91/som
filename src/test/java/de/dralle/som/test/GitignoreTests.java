package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.dralle.som.Main;

class GitignoreTests {

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
	void testIfRegenRequired() throws IOException {
		String prototypeFile = ".gitignore.prototype";
        String actualFile = ".gitignore";

        List<String> prototypeLines = Files.readAllLines(Paths.get(prototypeFile));
        List<String> actualLines = Files.readAllLines(Paths.get(actualFile));
        List<String> expectedLines = new ArrayList<>(prototypeLines);
        Main.generateAllNewLinesForFormatsAndExceludedFolders(expectedLines);

        assertEquals(expectedLines.size(), actualLines.size(),"Number of lines mismatch (--regenerate gitignore)");

        for (int i = 0; i < expectedLines.size(); i++) {
            String prototypeLine = expectedLines.get(i);
            String actualLine = actualLines.get(i);
            assertEquals(prototypeLine, actualLine,"Line " + (i + 1) + " mismatch (--regenerate gitignore)");
        }
	}

}
