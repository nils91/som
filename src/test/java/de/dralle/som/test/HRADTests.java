package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import de.dralle.som.Compiler;
import de.dralle.som.FileLoader;
import de.dralle.som.SOMFormats;
import de.dralle.som.languages.hrad.model.HRADModel;

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

}
