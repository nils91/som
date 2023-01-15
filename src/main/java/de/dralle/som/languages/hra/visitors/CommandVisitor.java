/**
 * 
 */
package de.dralle.som.languages.hra.visitors;

import de.dralle.som.Opcode;
import de.dralle.som.languages.hra.HRAModel;
import de.dralle.som.languages.hra.generated.HRAGrammarBaseVisitor;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.CommandContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.DirectiveContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.Int_or_symbolContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.LineContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.Offset_specifyContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.ProgramContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.Symbol_decContext;
import de.dralle.som.languages.hra.model.Command;

/**
 * @author Nils
 *
 */
public class CommandVisitor extends HRAGrammarBaseVisitor<Command> {
	private Command c;

	@Override
	public Command visitOffset_specify(Offset_specifyContext ctx) {
		if (ctx.NEG_INT() != null) {
			c.setAddressOffset(Integer.parseInt(ctx.NEG_INT().toString()));
		} else if (ctx.INT() != null) {
			c.setAddressOffset(Integer.parseInt(ctx.INT().toString()));
		}
		return c;
	}

	@Override
	public Command visitInt_or_symbol(Int_or_symbolContext ctx) {
		if (ctx.offset_specify() != null) {
			ctx.offset_specify().accept(this);
		}
		if (ctx.getChild(0) != null) {
			c.setTgtSymbol(ctx.getChild(0).toString());
			return c;
		}
		return null;
	}

	@Override
	public Command visitCommand(CommandContext ctx) {
		c = new Command();
		if (ctx.NAR() != null) {
			c.setOp(Opcode.NAR);
		} else if (ctx.NAW() != null) {
			c.setOp(Opcode.NAW);
		}
		if (ctx.int_or_symbol() != null) {
			return ctx.int_or_symbol().accept(this);
		}
		return null;
	}

}
