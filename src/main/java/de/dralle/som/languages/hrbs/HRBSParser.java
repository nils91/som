/**
 * 
 */
package de.dralle.som.languages.hrbs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import de.dralle.som.languages.hrbs.generated.HRBSGrammarLexer;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser;
import de.dralle.som.languages.hrbs.model.HRBSModel;
import de.dralle.som.languages.hras.generated.HRASGrammarLexer;
import de.dralle.som.languages.hras.generated.HRASGrammarParser;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.ProgramContext;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hras.visitors.ProgramVisitor;

/**
 * @author Nils
 *
 */
public class HRBSParser {
	public HRBSModel parse(InputStream is) throws IOException {
		HRBSGrammarLexer lexer = new HRBSGrammarLexer(CharStreams.fromStream(is));
		HRBSGrammarParser parser = new HRBSGrammarParser(new CommonTokenStream(lexer));
		de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.ProgramContext pt = parser.program();
		HRBSModel model = pt.accept(new de.dralle.som.languages.hrbs.visitors.HRBSProgramVisitor());
		return model;
	}
	public HRBSModel parse(String s) throws IOException {
		return parse(new ByteArrayInputStream(s.getBytes()));
	}
}
