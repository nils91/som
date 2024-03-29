/**
 * 
 */
package de.dralle.som.languages.hrac.visitors;

import de.dralle.som.Opcode;
import de.dralle.som.languages.hrac.generated.HRACGrammarBaseVisitor;
import de.dralle.som.languages.hrac.model.HRACCommand;
import de.dralle.som.languages.hrac.model.HRACSymbol;

/**
 * @author Nils
 *
 */
public class HRACCommandVisitor extends HRACGrammarBaseVisitor<HRACCommand> {
	private HRACCommand c;

	@Override
	public HRACCommand visitCommand(de.dralle.som.languages.hrac.generated.HRACGrammarParser.CommandContext ctx) {
		c = new HRACCommand();
		if (ctx.SYMBOL() != null) {
			HRACSymbol s = new HRACSymbol();
			s.setName(ctx.SYMBOL().getText());
			c.setLabel(s);
		}
		if (ctx.NAR() != null) {
			c.setOp(Opcode.NAR);
		} else if (ctx.NAW() != null) {
			c.setOp(Opcode.NAW);
		}
		if (ctx.symbol_os() != null) {
			c.setTarget(ctx.symbol_os().accept(new HRACMemoryAddressVisitor()));
		}
		return c;
	}

}
