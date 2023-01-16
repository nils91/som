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
import de.dralle.som.languages.hra.model.Command;
import de.dralle.som.languages.hra.model.HRAModel;
import de.dralle.som.languages.hra.model.MemoryAddress;

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
			Command c = ctx.command().accept(new CommandVisitor());
			model.addCommand(c);
		}
		return model;
	}

	@Override
	public HRAModel visitSymbol_dec(Symbol_decContext ctx) {
		model.addSymbol(ctx.SYMBOL().toString(), ctx.int_or_symbol().accept(new MemoryAddressVisitor()).resolve(model));
		return model;
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
	public HRAModel visitDirective(DirectiveContext ctx) {
		MemoryAddress address = ctx.int_or_symbol().accept(new MemoryAddressVisitor());
		if(ctx.D_N()!=null) {
			model.setN(address.resolve(model));
		}else if(ctx.HEAP()!=null) {
			model.setHeap(address.resolve(model));
		}else if (ctx.START()!=null) {
			model.setStartAdress(address.resolve(model));
			model.setStartAddressExplicit(true);
			model.setNextCommandAddress(model.getStartAddress());
		}else if(ctx.CONT()!=null) {
			model.setNextCommandAddress(address.resolve(model));
		}
		return model;
	}

}
