/**
 * 
 */
package de.dralle.som.languages.hra;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import de.dralle.som.languages.hra.generated.HRAGrammarLexer;
import de.dralle.som.languages.hra.generated.HRAGrammarParser;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.ProgramContext;
import de.dralle.som.languages.hra.model.HRAModel;
import de.dralle.som.languages.hra.visitors.ProgramVisitor;

/**
 * @author Nils
 *
 */
public class HRAParser {
	public HRAModel parse(InputStream is) throws IOException {
		HRAGrammarLexer lexer = new HRAGrammarLexer(new ANTLRInputStream(is));
		HRAGrammarParser parser = new HRAGrammarParser(new CommonTokenStream(lexer));
		ProgramContext pt = parser.program();
		HRAModel model = pt.accept(new ProgramVisitor());
		return model;
	}
	public HRAModel parse(String s) throws IOException {
		return parse(new ByteArrayInputStream(s.getBytes()));
	}
}
