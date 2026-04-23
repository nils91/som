package de.dralle.som.languages.hrad.visitors;

import de.dralle.som.Util;
import de.dralle.som.languages.hrad.generated.HRADGrammarBaseVisitor;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.NumberContext;

public class HRADNumberVisitor extends HRADGrammarBaseVisitor<Integer> {



	@Override
	public Integer visitNumber(NumberContext ctx) {
		if(ctx.INT()!=null) {
			return Integer.parseInt(ctx.INT().getText());
		}
		if(ctx.PREFIXED_INT()!=null) {
			return Util.decodeInt(ctx.PREFIXED_INT().getText());
		}
		return null;
	}

}
