/**
 * 
 */
package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Import_stmtContext;
import de.dralle.som.languages.hrbs.model.HRBSModel;

import java.io.IOException;
import java.util.List;

import de.dralle.som.FileLoader;
import de.dralle.som.languages.hras.generated.HRASGrammarBaseVisitor;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.DirectiveContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.LineContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.ProgramContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Symbol_decContext;
import de.dralle.som.languages.hras.model.Command;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hras.model.MemoryAddress;

/**
 * @author Nils
 *
 */
public class HRBSImportVisitor extends HRBSGrammarBaseVisitor<HRBSModel> {

	@Override
	public HRBSModel visitImport_stmt(Import_stmtContext ctx) {
		if(ctx.NAME()!=null) {
			try {
				return new FileLoader().loadHRBSByName(ctx.NAME().getText());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(ctx.FILEPATH()!=null) {
			String path=ctx.FILEPATH().getText();
			try {
				return new FileLoader().readHRBSFile(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}



}
