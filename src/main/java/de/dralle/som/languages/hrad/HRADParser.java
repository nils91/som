/**
 * 
 */
package de.dralle.som.languages.hrad;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hrav.generated.HRAVGrammarLexer;
import de.dralle.som.languages.hrav.generated.HRAVGrammarParser;
import de.dralle.som.languages.hrav.generated.HRAVGrammarParser.ProgramContext;
import de.dralle.som.languages.hrav.model.HRAVModel;
import de.dralle.som.languages.hrav.visitors.HRAVProgramVisitor;

/**
 * @author Nils
 *
 */
public class HRADParser {
	public HRADModel parse(InputStream is) throws IOException {
		HRAVGrammarLexer lexer = new HRADGrammarLexer(CharStreams.fromStream(is));
		HRAVGrammarParser parser = new HRADGrammarParser(new CommonTokenStream(lexer));
		ProgramContext pt = parser.program();
		HRAVModel model = pt.accept(new HRADProgramVisitor());
		return model;
	}

	public HRADModel parse(String s) throws IOException {
		return parse(new ByteArrayInputStream(s.getBytes()));
	}
}
