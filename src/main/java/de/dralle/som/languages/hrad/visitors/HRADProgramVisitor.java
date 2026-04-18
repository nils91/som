/**
 * 
 */
package de.dralle.som.languages.hrad.visitors;

import de.dralle.som.languages.hrad.generated.HRADGrammarBaseVisitor;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.DirectiveContext;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.LineContext;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.OtiContext;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.ProgramContext;
import de.dralle.som.languages.hrad.model.HRADCommand;
import de.dralle.som.languages.hrad.model.HRADModel;

/**
 * @author Nils
 *
 */
public class HRADProgramVisitor extends HRADGrammarBaseVisitor<HRADModel> {

	private HRADModel model;

	@Override
	public HRADModel visitDirective(DirectiveContext ctx) {
		if (ctx.number() != null) {
			int address = ctx.number().accept(new HRADNumberVisitor());
			if (ctx.START() != null) {
				model.setStartAdress(address);
				model.setStartAddressExplicit(true);
				model.setNextCommandAddress(address);
			} else if (ctx.CONT() != null) {
				model.setNextCommandAddress(address);
			} else if (ctx.D_N() != null) {
				model.setN(address);
			}
		}
		return model;
	}

	@Override
	public HRADModel visitLine(LineContext ctx) {
		if (ctx.directive() != null) {
			ctx.directive().accept(this);
		} else if (ctx.command() != null) {
			HRADCommand c = ctx.command().accept(new HRADCommandVisitor());
			model.addCommand(c);
		} else if (ctx.oti() != null) {
			ctx.oti().accept(this);
		}
		return model;
	}

	@Override
	public HRADModel visitOti(OtiContext ctx) {
		if (ctx.OTI_CLEAR() != null) {
			model.addInitOnceAddress(ctx.number().accept(new HRADNumberVisitor()), false);
		} else { // setonce
			model.addInitOnceAddress(ctx.number().accept(new HRADNumberVisitor()), true);
		}
		return model;
	}

	@Override
	public HRADModel visitProgram(ProgramContext ctx) {
		model = new HRADModel();
		for (LineContext line : ctx.line()) {
			line.accept(this);
		}
		return model;
	}

}
