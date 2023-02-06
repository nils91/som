/**
 * 
 */
package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.Opcode;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.model.HRBSCommand;
import de.dralle.som.languages.hrbs.model.HRBSSymbol;
import de.dralle.som.languages.hras.generated.HRASGrammarBaseVisitor;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.CommandContext;
import de.dralle.som.languages.hras.model.Command;

/**
 * @author Nils
 *
 */
public class CommandVisitor extends HRBSGrammarBaseVisitor<HRBSCommand> {
	private HRBSCommand c;

	@Override
	public HRBSCommand visitCommand(de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.CommandContext ctx) {
		c=new HRBSCommand();
		if(ctx.SYMBOL()!=null) {
			HRBSSymbol s = new HRBSSymbol();
			s.setName(ctx.SYMBOL().getText());
			c.setLabel(null);
		}
		if(ctx.NAR()!=null) {
			c.setCmd(Opcode.NAR);
		}else if(ctx.NAW()!=null) {
			c.setCmd(Opcode.NAW);
		}
		if(ctx.symbol_os()!=null) {
		c.setTarget(ctx.symbol_os().accept(new MemoryAddressVisitor()));
		}
		return c;
	}

	

}
