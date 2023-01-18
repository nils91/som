/**
 * 
 */
package de.dralle.som.languages.hrac.visitors;

import de.dralle.som.languages.hrac.generated.HRACGrammarBaseVisitor;
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
public class ProgramVisitor extends HRACGrammarBaseVisitor<HRASModel> {

	private HRASModel model;

	@Override
	public HRASModel visitLine(LineContext ctx) {
		if (ctx.symbol_dec() != null) {
			ctx.symbol_dec().accept(this);
		} else if (ctx.directive() != null) {
			ctx.directive().accept(this);
		} else if (ctx.command() != null) {
			Command c = ctx.command().accept(new CommandVisitor());
			model.addCommand(c);
		}
		return model;
	}

	@Override
	public HRASModel visitSymbol_dec(Symbol_decContext ctx) {
		model.addSymbol(ctx.SYMBOL().toString(), ctx.int_or_symbol().accept(new MemoryAddressVisitor()));
		return model;
	}

	@Override
	public HRASModel visitProgram(ProgramContext ctx) {
		model = new HRASModel();
		for (LineContext line : ctx.line()) {
			line.accept(this);
		}
		return model;
	}

	@Override
	public HRASModel visitDirective(DirectiveContext ctx) {
		if (ctx.int_or_symbol() != null) {
			MemoryAddress address = ctx.int_or_symbol().accept(new MemoryAddressVisitor());
			if (ctx.START() != null) {
				model.setStartAdress(address);
				model.setStartAddressExplicit(true);
				model.setNextCommandAddress(address.clone());
			} else if (ctx.CONT() != null) {
				model.setNextCommandAddress(address);
			}
		} else if (ctx.INT() != null) {
			model.setN(Integer.parseInt(ctx.INT().getText()));
		}
		return model;
	}

}
