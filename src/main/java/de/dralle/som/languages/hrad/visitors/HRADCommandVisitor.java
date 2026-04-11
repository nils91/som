/**
 * 
 */
package de.dralle.som.languages.hrad.visitors;

import de.dralle.som.Opcode;
import de.dralle.som.languages.hrad.generated.HRADGrammarBaseVisitor;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.CommandContext;
import de.dralle.som.languages.hrad.model.HRADCommand;

/**
 * @author Nils
 *
 */
public class HRADCommandVisitor extends HRADGrammarBaseVisitor<HRADCommand> {
	private HRADCommand c;

	@Override
	public HRADCommand visitCommand(CommandContext ctx) {
		c = new HRADCommand();
		if (ctx.NAR() != null) {
			c.setOp(Opcode.NAR);
		} else if (ctx.NAW() != null) {
			c.setOp(Opcode.NAW);
		}
		if (ctx.number() != null) {
			c.setAddress(ctx.number().accept(new HRADNumberVisitor()));
			return c;
		}
		return null;
	}

}
