/**
 * 
 */
package de.dralle.som.languages.hrav.visitors;

import de.dralle.som.languages.hrav.generated.HRAVGrammarBaseVisitor;
import de.dralle.som.languages.hrav.generated.HRAVGrammarParser.DirectiveContext;
import de.dralle.som.languages.hrav.generated.HRAVGrammarParser.LineContext;
import de.dralle.som.languages.hrav.generated.HRAVGrammarParser.ProgramContext;
import de.dralle.som.languages.hrav.model.HRAVCommand;
import de.dralle.som.languages.hrav.model.HRAVModel;

/**
 * @author Nils
 *
 */
public class HRAVProgramVisitor extends HRAVGrammarBaseVisitor<HRAVModel> {

	private HRAVModel model;

	@Override
	public HRAVModel visitLine(LineContext ctx) {
		if (ctx.directive() != null) {
			ctx.directive().accept(this);
		} else if (ctx.command() != null) {
			HRAVCommand c = ctx.command().accept(new HRAVCommandVisitor());
			model.addCommand(c);
		}
		return model;
	}	

	@Override
	public HRAVModel visitProgram(ProgramContext ctx) {
		model = new HRAVModel();
		for (LineContext line : ctx.line()) {
			line.accept(this);
		}
		return model;
	}

	@Override
	public HRAVModel visitDirective(DirectiveContext ctx) {
		if (ctx.INT() != null) {
			int address = Integer.parseInt(ctx.INT().getText());
			if (ctx.START() != null) {
				model.setStartAdress(address);
				model.setStartAddressExplicit(true);
				model.setNextCommandAddress(address);
			} else if (ctx.CONT() != null) {
				model.setNextCommandAddress(address);
			}else if(ctx.D_N()!=null) {
				model.setN(address);
			}
		}
		return model;
	}

}
