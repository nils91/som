/**
 * 
 */
package de.dralle.som.languages.hrac.visitors;

import de.dralle.som.languages.hrac.generated.HRACGrammarBaseVisitor;
import de.dralle.som.languages.hrac.model.HRACModel;
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
public class ProgramVisitor extends HRACGrammarBaseVisitor<HRACModel> {

	private HRACModel model;

	@Override
	public HRACModel visitLine(de.dralle.som.languages.hrac.generated.HRACGrammarParser.LineContext ctx) {
		
		if (ctx.directive() != null) {
			ctx.directive().accept(this);
		}else if(ctx.command()!=null) {
			model.addCommand(ctx.command().accept(new CommandVisitor()));
		}

		return model;
	}

	@Override
	public HRACModel visitProgram(de.dralle.som.languages.hrac.generated.HRACGrammarParser.ProgramContext ctx) {
		model = new HRACModel();
		for (de.dralle.som.languages.hrac.generated.HRACGrammarParser.LineContext l : ctx.line()) {
			l.accept(this);
		}
		return model;
	}

	@Override
	public HRACModel visitDirective(de.dralle.som.languages.hrac.generated.HRACGrammarParser.DirectiveContext ctx) {
		if (ctx.HEAP() != null) {
			model.setHeapSize(Integer.parseInt(ctx.INT().getText()));
		}
		return model;
	}

}
