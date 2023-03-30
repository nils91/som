/**
 * 
 */
package de.dralle.som.languages.hrac;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import de.dralle.som.languages.hrac.generated.HRACGrammarLexer;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser;
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.generated.HRASGrammarLexer;
import de.dralle.som.languages.hras.generated.HRASGrammarParser;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.ProgramContext;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hras.visitors.ProgramVisitor;

/**
 * @author Nils
 *
 */
public class HRACParser {
	public HRACModel parse(InputStream is) throws IOException {
		HRACGrammarLexer lexer = new HRACGrammarLexer(new ANTLRInputStream(is));
		HRACGrammarParser parser = new HRACGrammarParser(new CommonTokenStream(lexer));
		de.dralle.som.languages.hrac.generated.HRACGrammarParser.ProgramContext pt = parser.program();
		HRACModel model = pt.accept(new de.dralle.som.languages.hrac.visitors.HRACProgramVisitor());
		return model;
	}
	public HRACModel parse(String s) throws IOException {
		return parse(new ByteArrayInputStream(s.getBytes()));
	}
}
