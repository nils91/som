/**
 * 
 */
package de.dralle.som.languages.hrav;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import de.dralle.som.languages.hrav.generated.HRAVGrammarLexer;
import de.dralle.som.languages.hrav.generated.HRAVGrammarParser;
import de.dralle.som.languages.hrav.generated.HRAVGrammarParser.ProgramContext;
import de.dralle.som.languages.hrav.model.HRAVModel;
import de.dralle.som.languages.hrav.visitors.HRAVProgramVisitor;

/**
 * @author Nils
 *
 */
public class HRAVParser {
	public HRAVModel parse(InputStream is) throws IOException {
		HRAVGrammarLexer lexer = new HRAVGrammarLexer(CharStreams.fromStream(is));
		HRAVGrammarParser parser = new HRAVGrammarParser(new CommonTokenStream(lexer));
		ProgramContext pt = parser.program();
		HRAVModel model = pt.accept(new HRAVProgramVisitor());
		return model;
	}
	public HRAVModel parse(String s) throws IOException {
		return parse(new ByteArrayInputStream(s.getBytes()));
	}
}
