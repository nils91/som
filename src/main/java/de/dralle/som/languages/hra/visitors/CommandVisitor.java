/**
 * 
 */
package de.dralle.som.languages.hra.visitors;

import de.dralle.som.Opcode;
import de.dralle.som.languages.hra.generated.HRAGrammarBaseVisitor;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.CommandContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.DirectiveContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.Int_or_symbolContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.LineContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.Offset_specifyContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.ProgramContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.Symbol_decContext;
import de.dralle.som.languages.hra.model.Command;
import de.dralle.som.languages.hra.model.HRAModel;

/**
 * @author Nils
 *
 */
public class CommandVisitor extends HRAGrammarBaseVisitor<Command> {
	private Command c;

	@Override
	public Command visitCommand(CommandContext ctx) {
		c = new Command();
		if (ctx.NAR() != null) {
			c.setOp(Opcode.NAR);
		} else if (ctx.NAW() != null) {
			c.setOp(Opcode.NAW);
		}
		if (ctx.int_or_symbol() != null) {
			c.setAddress(ctx.int_or_symbol().accept(new MemoryAddressVisitor()));
			return c;
		}
		return null;
	}

}
