/**
 * 
 */
package de.dralle.som.languages.hras.visitors;

import de.dralle.som.languages.hras.generated.HRASGrammarBaseVisitor;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.DirectiveContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.LineContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.OtiContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.ProgramContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Symbol_decContext;
import de.dralle.som.languages.hras.model.AbstractHRASMemoryAddress;
import de.dralle.som.languages.hras.model.HRASCommand;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hras.model.SymbolHRASMemoryAddress;

/**
 * @author Nils
 *
 */
public class ProgramVisitor extends HRASGrammarBaseVisitor<HRASModel> {

	private HRASModel model;

	@Override
	public HRASModel visitLine(LineContext ctx) {
		if (ctx.symbol_dec() != null) {
			ctx.symbol_dec().accept(this);
		} else if (ctx.directive() != null) {
			ctx.directive().accept(this);
		} else if (ctx.command() != null) {
			HRASCommand c = ctx.command().accept(new CommandVisitor());
			model.addCommand(c);
		}else if(ctx.oti()!=null) {
			ctx.oti().accept(this);
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
	public HRASModel visitOti(OtiContext ctx) {
		if(ctx.OTI_SET()!=null) {
			model.addInitOnceValue(ctx.int_or_symbol().accept(new MemoryAddressVisitor()), true);
		}if(ctx.OTI_CLEAR()!=null) {
			model.addInitOnceValue(ctx.int_or_symbol().accept(new MemoryAddressVisitor()), false);
		}
		return model;
	}

	@Override
	public HRASModel visitDirective(DirectiveContext ctx) {
		if (ctx.int_or_symbol() != null) {
			AbstractHRASMemoryAddress address = ctx.int_or_symbol().accept(new MemoryAddressVisitor());
			if (ctx.START() != null) {
				model.setStartAdress(address);
				model.setStartAddressExplicit(true);
				model.setNextCommandAddress(address.clone());
			} else if (ctx.CONT() != null) {
				model.setNextCommandAddress(address);
			}
		} else if (ctx.primary_expr() != null) {
			model.setN(ctx.primary_expr().accept(new ExpressionVisitor()));
		}
		return model;
	}

}
