/**
 * 
 */
package de.dralle.som.languages.hrav.visitors;

import de.dralle.som.languages.hrav.generated.HRAVGrammarBaseVisitor;
import de.dralle.som.languages.hrav.generated.HRAVGrammarParser.DirectiveContext;
import de.dralle.som.languages.hrav.generated.HRAVGrammarParser.LineContext;
import de.dralle.som.languages.hrav.generated.HRAVGrammarParser.ProgramContext;
import de.dralle.som.languages.hrav.generated.HRAVGrammarParser.Symbol_decContext;
import de.dralle.som.languages.hrav.model.HRAVCommand;
import de.dralle.som.languages.hrav.model.HRAVModel;
import de.dralle.som.languages.hrav.model.MemoryAddress;

/**
 * @author Nils
 *
 */
public class ProgramVisitor extends HRAVGrammarBaseVisitor<HRAVModel> {

	private HRAVModel model;

	@Override
	public HRAVModel visitLine(LineContext ctx) {
		if (ctx.symbol_dec() != null) {
			ctx.symbol_dec().accept(this);
		} else if (ctx.directive() != null) {
			ctx.directive().accept(this);
		} else if (ctx.command() != null) {
			HRAVCommand c = ctx.command().accept(new CommandVisitor());
			model.addCommand(c);
		}
		return model;
	}

	@Override
	public HRAVModel visitSymbol_dec(Symbol_decContext ctx) {
		model.addSymbol(ctx.SYMBOL().toString(), ctx.int_or_symbol().accept(new MemoryAddressVisitor()));
		return model;
	}

	@Override
	public HRAVModel visitProgram(ProgramContext ctx) {
		model = new HRAVModel();
		for (LineContext line : ctx.line()) {
			line.accept(this);
		}
		return model;
	}

	@Override
	public HRAVModel visitDirective(DirectiveContext ctx) {
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
