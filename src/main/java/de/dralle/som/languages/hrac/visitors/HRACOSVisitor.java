package de.dralle.som.languages.hrac.visitors;

import de.dralle.som.Util;
import de.dralle.som.languages.hrac.generated.HRACGrammarBaseVisitor;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Cnt_specifyContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Directive_accessContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Offset_specify_numberContext;
import de.dralle.som.languages.hrac.model.HRACMemoryOffset;

public class HRACOSVisitor extends HRACGrammarBaseVisitor<HRACMemoryOffset> {
	private HRACMemoryOffset o;

	public HRACOSVisitor() {
		o = new HRACMemoryOffset();
	}

	@Override
	public HRACMemoryOffset visitDirective_access(Directive_accessContext ctx) {
		o.setDirectiveName(ctx.directive_name().getText());
		return o;
	}

	@Override
	public HRACMemoryOffset visitOffset_specify_number(Offset_specify_numberContext ctx) {
		if (ctx.NEG_INT() != null) {
			o.setOffset(Util.decodeInt(ctx.NEG_INT().getText()));
		}
		if (ctx.INT() != null) {
			o.setOffset(Util.decodeInt(ctx.INT().getText()));
		}
		if (ctx.directive_access() != null) {
			ctx.directive_access().accept(this);
		}
		return o;
	}

	@Override
	public HRACMemoryOffset visitCnt_specify(Cnt_specifyContext ctx) {

		if (ctx.INT() != null) {
			o.setOffset(Util.decodeInt(ctx.INT().getText()));
		}
		if (ctx.directive_access() != null) {
			ctx.directive_access().accept(this);
		}
		return o;
	}
}
