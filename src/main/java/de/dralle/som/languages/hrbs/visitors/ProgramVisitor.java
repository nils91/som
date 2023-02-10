/**
 * 
 */
package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Command_defContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Import_stmtContext;
import de.dralle.som.languages.hrbs.model.HRBSModel;

import java.util.HashMap;
import java.util.Map;

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
public class ProgramVisitor extends HRBSGrammarBaseVisitor<HRBSModel> {

	private HRBSModel model;

	public ProgramVisitor() {
		model=new HRBSModel();
	}
	public ProgramVisitor(HRBSModel model) {
		this.model=model;
	}
	@Override
	public HRBSModel visitProgram(de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.ProgramContext ctx) {
		if(ctx.import_stmt()!=null) {
			for (Import_stmtContext imp : ctx.import_stmt()) {
				model.addChild(imp.accept(new HRBSImportVisitor()));
			}
		}
		if(ctx.command_def()!=null) {
			for (Command_defContext com : ctx.command_def()) {				
				HRBSModel lclModel = com.accept(new ProgramVisitor());
				if("MAIN".equalsIgnoreCase(lclModel.getName())) {
					model=lclModel;
				}else {
					model.addChild(lclModel);
				}
			}
		}
		return model;
	}

	@Override
	public HRBSModel visitDirective(de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.DirectiveContext ctx) {
		if (ctx.HEAP() != null) {
			model.setHeapSize(Integer.parseInt(ctx.INT().getText()));
		}else if (ctx.D_N()!=null){
			model.setMinimumN(Integer.parseInt(ctx.INT().getText()));
		}
		return model;
	}

}
