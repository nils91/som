package de.dralle.som.languages.hrad;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.stream.Stream;

public class Util {

	public static void refactorDirectory(String rootPath, String target, String replacement) throws IOException {
		Path root = Path.of(rootPath);
		try (Stream<Path> paths = Files.walk(root)) {
			paths.sorted(Comparator.reverseOrder()).forEach(path -> {
				try {
					if (Files.isRegularFile(path)) {
						String content = Files.readString(path);
						if (content.contains(target)) {
							String newContent = content.replace(target, replacement);
							Files.writeString(path, newContent, StandardOpenOption.TRUNCATE_EXISTING);
						}
					}
					String fileName = path.getFileName().toString();
					if (fileName.contains(target)) {
						String newName = fileName.replace(target, replacement);
						Files.move(path, path.resolveSibling(newName));
					}
				} catch (IOException e) {
					System.err.println("Fehler: " + path + " -> " + e.getMessage());
				}
			});
		}
	}

	public static void main(String[] args) throws IOException {
		refactorDirectory("src/main/java/de/dralle/som/languages/hrad/visitors", "hrav", "hrad");
		refactorDirectory("src/main/java/de/dralle/som/languages/hrad/visitors", "HRAV", "HRAD");
		refactorDirectory("src/main/java/de/dralle/som/languages/hrad/visitors", "Hrav", "Hrad");
	}

}
