package de.dralle.som.languages.hras.visitors;

import de.dralle.som.Util;
import de.dralle.som.languages.hras.generated.HRASGrammarBaseVisitor;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Based_intContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Binary_intContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Decimal_intContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Hex_intContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.IntegerContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Neg_integerContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Octal_intContext;

public class HRASNumberVisitor extends HRASGrammarBaseVisitor<Integer>{

	@Override
	public Integer visitNeg_integer(Neg_integerContext ctx) {
	return ctx.integer().accept(this)*-1;
	}

	@Override
	public Integer visitInteger(IntegerContext ctx) {
		return ctx.children.get(0).accept(this);
	}
	

	@Override
	public Integer visitBased_int(Based_intContext ctx) {
		int base=10;
		String n=null;
		base=Util.getBaseFromPrefix(ctx.BASE_NUMBER_PREFIX().getText());
		if(ctx.EINT()!=null) {
			n=ctx.EINT().getText();
		}else {
			n=ctx.INT().getText();
		}
		return Integer.parseInt(n, base);
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
