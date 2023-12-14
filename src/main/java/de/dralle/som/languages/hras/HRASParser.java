/**
 * 
 */
package de.dralle.som.languages.hras;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import de.dralle.som.languages.hras.generated.HRASGrammarLexer;
import de.dralle.som.languages.hras.generated.HRASGrammarParser;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.ProgramContext;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hras.visitors.ProgramVisitor;

/**
 * @author Nils
 *
 */
public class HRASParser {
	public HRASModel parse(InputStream is) throws IOException {
		HRASGrammarLexer lexer = new HRASGrammarLexer(CharStreams.fromStream(is));
		HRASGrammarParser parser = new HRASGrammarParser(new CommonTokenStream(lexer));
		ProgramContext pt = parser.program();
		HRASModel model = pt.accept(new ProgramVisitor());
		return model;
	}
	public HRASModel parse(String s) throws IOException {
		return parse(new ByteArrayInputStream(s.getBytes()));
	}
}
