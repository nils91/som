/**
 * 
 */
package de.dralle.som.languages.hras.visitors;

import de.dralle.som.Opcode;
import de.dralle.som.languages.hras.generated.HRASGrammarBaseVisitor;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.CommandContext;
import de.dralle.som.languages.hras.model.Command;

/**
 * @author Nils
 *
 */
public class CommandVisitor extends HRASGrammarBaseVisitor<Command> {
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
