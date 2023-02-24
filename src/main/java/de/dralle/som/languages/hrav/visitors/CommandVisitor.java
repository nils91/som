/**
 * 
 */
package de.dralle.som.languages.hrav.visitors;

import de.dralle.som.Opcode;
import de.dralle.som.languages.hrav.generated.HRAVGrammarBaseVisitor;
import de.dralle.som.languages.hrav.generated.HRAVGrammarParser.CommandContext;
import de.dralle.som.languages.hrav.model.HRAVCommand;

/**
 * @author Nils
 *
 */
public class CommandVisitor extends HRAVGrammarBaseVisitor<HRAVCommand> {
	private HRAVCommand c;

	@Override
	public HRAVCommand visitCommand(CommandContext ctx) {
		c = new HRAVCommand();
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
