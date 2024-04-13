package de.dralle.som.languages.hrav.visitors;

import de.dralle.som.languages.hrav.generated.HRAVGrammarBaseVisitor;
import de.dralle.som.languages.hrav.generated.HRAVGrammarParser.Based_intContext;
import de.dralle.som.languages.hrav.generated.HRAVGrammarParser.Binary_intContext;
import de.dralle.som.languages.hrav.generated.HRAVGrammarParser.Decimal_intContext;
import de.dralle.som.languages.hrav.generated.HRAVGrammarParser.Hex_intContext;
import de.dralle.som.languages.hrav.generated.HRAVGrammarParser.NumberContext;
import de.dralle.som.languages.hrav.generated.HRAVGrammarParser.Octal_intContext;

public class HRAVNumberVisitor extends HRAVGrammarBaseVisitor<Integer>{

	@Override
	public Integer visitNumber(NumberContext ctx) {
		return ctx.children.get(0).accept(this);
	}

	@Override
	public Integer visitBased_int(Based_intContext ctx) {
		String base=null;
		String n=null;
		base=ctx.INT().get(0).getText();
		if(ctx.EINT()!=null) {
			n=ctx.EINT().getText();
		}else {
			n=ctx.INT().get(1).getText();
		}
		return Integer.parseInt(n, Integer.parseInt(base));
	}

	@Override
	public Integer visitBinary_int(Binary_intContext ctx) {
		String n=null;
		n=ctx.INT().getText();		
	return Integer.parseInt(n, 2);
	}

	@Override
	public Integer visitOctal_int(Octal_intContext ctx) {
		String n=null;
			n=ctx.INT().getText();		
		return Integer.parseInt(n, 8);
	}

	@Override
	public Integer visitHex_int(Hex_intContext ctx) {
		String n=null;
		if(ctx.INT()!=null) {
			n=ctx.INT().getText();
		}else {
			n=ctx.EINT().getText();
		}
		return Integer.parseInt(n, 16);
	}

	@Override
	public Integer visitDecimal_int(Decimal_intContext ctx) {
		String n=ctx.INT().getText();
		return Integer.parseInt(n);
	}

}
