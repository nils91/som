/**
 * 
 */
package de.dralle.som.languages.hrad;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import de.dralle.som.languages.hrad.generated.HRADGrammarLexer;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.ProgramContext;
import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hrad.visitors.HRADProgramVisitor;

/**
 * @author Nils
 *
 */
public class HRADParser {
	public HRADModel parse(InputStream is) throws IOException {
		HRADGrammarLexer lexer = new HRADGrammarLexer(CharStreams.fromStream(is));
		HRADGrammarParser parser = new HRADGrammarParser(new CommonTokenStream(lexer));
		ProgramContext pt = parser.program();
		HRADModel model = pt.accept(new HRADProgramVisitor());
		return model;
	}

	public HRADModel parse(String s) throws IOException {
		return parse(new ByteArrayInputStream(s.getBytes()));
	}
}
