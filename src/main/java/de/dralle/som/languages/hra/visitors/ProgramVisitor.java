/**
 * 
 */
package de.dralle.som.languages.hra.visitors;

import de.dralle.som.languages.hra.generated.HRAGrammarBaseVisitor;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.CommandContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.DirectiveContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.LineContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.ProgramContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.Symbol_decContext;
import de.dralle.som.languages.hra.model.HRAModel;

/**
 * @author Nils
 *
 */
public class ProgramVisitor extends HRAGrammarBaseVisitor<HRAModel> {

	private HRAModel model;

	@Override
	public HRAModel visitLine(LineContext ctx) {
		if(ctx.symbol_dec()!=null) {
			ctx.symbol_dec().accept(this);
		}else if(ctx.directive()!=null) {
			ctx.directive().accept(this);
		}else if(ctx.command()!=null) {
			ctx.command().accept(new CommandVisitor());
		}
		return model;
	}

	@Override
	public HRAModel visitSymbol_dec(Symbol_decContext ctx) {
		// TODO Auto-generated method stub
		return super.visitSymbol_dec(ctx);
	}

	@Override
	public HRAModel visitProgram(ProgramContext ctx) {
		model=new HRAModel();
		for (LineContext line : ctx.line()) {
			line.accept(this);
		}
		return model;
	}

	@Override
	public HRAModel visitCommand(CommandContext ctx) {
		// TODO Auto-generated method stub
		return super.visitCommand(ctx);
	}

	@Override
	public HRAModel visitDirective(DirectiveContext ctx) {
		// TODO Auto-generated method stub
		return super.visitDirective(ctx);
	}

}
