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
		HRBSModel mtr=null;
		if(ctx.NAME()!=null&&ctx.DIRECTIVE_VALUE_STR()==null) {
			try {
				mtr= new FileLoader().loadHRBSByName(ctx.NAME(0).getText());
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(ctx.NAME(1)!=null&&mtr!=null) {
				mtr.setName(ctx.NAME(1).getText());
			}
		}
		else if(ctx.DIRECTIVE_VALUE_STR()!=null) {
			String path=ctx.DIRECTIVE_VALUE_STR().getText();
			path=path.replaceAll("\"|'", "");
			try {
				mtr= new FileLoader().readHRBSFile(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(ctx.NAME(0)!=null&&mtr!=null) {
				mtr.setName(ctx.NAME(0).getText());
			}
		}
		return mtr;
	}



}
