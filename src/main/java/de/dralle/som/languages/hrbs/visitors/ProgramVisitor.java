/**
 * 
 */
package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.model.HRBSModel;
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

	@Override
	public HRBSModel visitLine(de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.LineContext ctx) {
		
		if (ctx.directive() != null) {
			ctx.directive().accept(this);
		}else if(ctx.command()!=null) {
			model.addCommand(ctx.command().accept(new CommandVisitor()));
		}else if(ctx.symbol_dec()!=null) {
			model.addSymbol(ctx.symbol_dec().accept(new HRBSSymbolVisitor()));
		}

		return model;
	}

	@Override
	public HRBSModel visitProgram(de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.ProgramContext ctx) {
		model = new HRBSModel();
		for (de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.LineContext l : ctx.line()) {
			l.accept(this);
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
