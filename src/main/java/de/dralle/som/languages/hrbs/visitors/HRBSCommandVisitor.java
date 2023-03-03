/**
 * 
 */
package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.Opcode;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Symbol_osContext;
import de.dralle.som.languages.hrbs.model.HRBSCommand;
import de.dralle.som.languages.hrbs.model.HRBSSymbol;
import de.dralle.som.languages.hrbs.model.HRBSSymbolType;
import de.dralle.som.languages.hras.generated.HRASGrammarBaseVisitor;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.CommandContext;
import de.dralle.som.languages.hras.model.Command;

/**
 * @author Nils
 *
 */
public class HRBSCommandVisitor extends HRBSGrammarBaseVisitor<HRBSCommand> {
	private HRBSCommand c;

	@Override
	public HRBSCommand visitCommand(de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.CommandContext ctx) {
		c = new HRBSCommand();
		HRBSSymbolType labelType = null;
		if(ctx.def_scope()!=null) {
			labelType=ctx.def_scope().accept(new HBRSSymbolTypeVisitor());
		}
		boolean hasCmd = false;
		if (ctx.NAR() != null) {
			hasCmd = true;
			c.setCmd(Opcode.NAR.name());
		} else if (ctx.NAW() != null) {
			hasCmd = true;
			c.setCmd(Opcode.NAW.name());
		} else if (ctx.NAME().size() == 2) {
			hasCmd = true;
			// cmd has label
			c.setLabel(ctx.NAME(0).getText());
			c.setCmd(ctx.NAME(1).getText());
		}
		if (ctx.NAME().size() == 1) {
			if (hasCmd) {
				// is label
				c.setLabel(ctx.NAME(0).getText());
			} else {
				c.setCmd(ctx.NAME(0).getText());
			}
		}
		if (ctx.symbol_os() != null) {
			for (Symbol_osContext soc : ctx.symbol_os()) {
				c.addTarget(soc.accept(new HRBSMemoryAddressVisitor()));
			}
		}
		return c;
	}

}
