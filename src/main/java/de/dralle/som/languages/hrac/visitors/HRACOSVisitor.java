package de.dralle.som.languages.hrac.visitors;

import de.dralle.som.languages.hrac.generated.HRACGrammarBaseVisitor;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Cnt_specifyContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Directive_accessContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Offset_specify_numberContext;
import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACDirectiveNode;

public class HRACOSVisitor extends HRACGrammarBaseVisitor<HRACAbstractExpressionNode> {
	private HRACAbstractExpressionNode o;

	@Override
	public HRACAbstractExpressionNode visitCnt_specify(Cnt_specifyContext ctx) {
		o = (ctx.par_expr().accept(new HRACExpressionVisitor()));
		return o;
	}

	@Override
	public HRACAbstractExpressionNode visitDirective_access(Directive_accessContext ctx) {
		o = (new HRACDirectiveNode(ctx.directive_name().getText()));
		return o;
	}

	@Override
	public HRACAbstractExpressionNode visitOffset_specify_number(Offset_specify_numberContext ctx) {
		o = (ctx.par_expr().accept(new HRACExpressionVisitor()));
		return o;
	}
}
