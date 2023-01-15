/**
 * 
 */
package de.dralle.som.languages.hra;

import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import de.dralle.som.languages.hra.generated.HRAGrammarLexer;
import de.dralle.som.languages.hra.generated.HRAGrammarParser;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.ProgramContext;

/**
 * @author Nils
 *
 */
public class HRAParser {
	public void parse(InputStream is) {
		HRAGrammarLexer lexer = new HRAGrammarLexer(new ANTLRInputStream(is));
		HRAGrammarParser parser = new HRAGrammarParser(new CommonTokenStream(lexer));
		ProgramContext pt = parser.program();
		AtHousenumberData addressData = pt.accept(new AtAddressDataParseVisitor());
	}	
}
