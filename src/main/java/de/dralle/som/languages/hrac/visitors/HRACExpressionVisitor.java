package de.dralle.som.languages.hrac.visitors;

import de.dralle.som.Util;
import de.dralle.som.languages.hrac.generated.HRACGrammarBaseVisitor;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Absolute_exprContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Additive_exprContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Directive_accessContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Factorial_exprContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Integer_or_directiveContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Multiplicative_exprContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Par_exprContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Power_exprContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Primary_exprContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Signed_integer_or_directiveContext;
import de.dralle.som.languages.hrac.model.expressiontree.HRACAbsoluteExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACDirectiveNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACDivisionExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACFactorialExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACIntegerNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACMinusExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACModuloExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACMultiplicationExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACNegationExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACPlusExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACPowerExpressionNode;

public class HRACExpressionVisitor
		extends HRACGrammarBaseVisitor<de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractDirectiveExpressionTreeNode> {

	@Override
	public HRACAbstractDirectiveExpressionTreeNode visitAbsolute_expr(Absolute_exprContext ctx) {
		HRACAbstractDirectiveExpressionTreeNode child0 = ctx.par_expr().accept(this);
		if (ctx.PIPE() != null && ctx.PIPE().size() == 2) {
			return new HRACAbsoluteExpressionNode(child0);
		}
		return child0;
	}

	@Override
	public HRACAbstractDirectiveExpressionTreeNode visitAdditive_expr(Additive_exprContext ctx) {
		HRACAbstractDirectiveExpressionTreeNode child1 = ctx.multiplicative_expr().accept(this);
		HRACAbstractDirectiveExpressionTreeNode child0 = null;
		if (ctx.additive_expr() != null) {
			child0 = ctx.additive_expr().accept(this);
			if (ctx.PLUS() != null) {
				return new HRACPlusExpressionNode(child0, child1);
			} else if (ctx.DASH() != null) {
				return new HRACMinusExpressionNode(child0, child1);
			}
		}
		return child1;
	}

	@Override
	public HRACAbstractDirectiveExpressionTreeNode visitDirective_access(Directive_accessContext ctx) {
		return new HRACDirectiveNode(ctx.directive_name().getText());
	}

	@Override
	public HRACAbstractDirectiveExpressionTreeNode visitFactorial_expr(Factorial_exprContext ctx) {
		HRACAbstractDirectiveExpressionTreeNode child0 = ctx.absolute_expr().accept(this);
		if (ctx.EXCL() != null) {
			return new HRACFactorialExpressionNode(child0);
		}
		return child0;
	}

	@Override
	public HRACAbstractDirectiveExpressionTreeNode visitInteger_or_directive(Integer_or_directiveContext ctx) {
		if (ctx.INT() != null) {
			return new HRACIntegerNode(Util.decodeInt(ctx.INT().getText()));
		} else if (ctx.directive_access() != null) {
			return ctx.directive_access().accept(this);
		}
		return null;
	}

	@Override
	public HRACAbstractDirectiveExpressionTreeNode visitMultiplicative_expr(Multiplicative_exprContext ctx) {
		HRACAbstractDirectiveExpressionTreeNode child1 = ctx.power_expr().accept(this);
		HRACAbstractDirectiveExpressionTreeNode child0 = null;
		if (ctx.multiplicative_expr() != null) {
			child0 = ctx.multiplicative_expr().accept(this);
			if (ctx.MUL() != null) {
				return new HRACMultiplicationExpressionNode(child0, child1);
			} else if (ctx.DIV() != null) {
				return new HRACDivisionExpressionNode(child0, child1);
			} else if (ctx.MOD() != null) {
				return new HRACModuloExpressionNode(child0, child1);
			}
		}
		return child1;
	}

	@Override
	public HRACAbstractDirectiveExpressionTreeNode visitPar_expr(Par_exprContext ctx) {
		if (ctx.signed_integer_or_directive() != null) {
			return ctx.signed_integer_or_directive().accept(this);
		}
		if (ctx.primary_expr() != null) {
			return ctx.primary_expr().accept(this);
		}
		return null;
	}

	@Override
	public HRACAbstractDirectiveExpressionTreeNode visitPower_expr(Power_exprContext ctx) {
		HRACAbstractDirectiveExpressionTreeNode child0 = ctx.factorial_expr().accept(this);
		HRACAbstractDirectiveExpressionTreeNode child1 = null;
		if (ctx.power_expr() != null) {
			child1 = ctx.power_expr().accept(this);
			return new HRACPowerExpressionNode(child0, child1);

		}
		return child0;
	}

	@Override
	public HRACAbstractDirectiveExpressionTreeNode visitPrimary_expr(Primary_exprContext ctx) {
		return ctx.additive_expr().accept(this);
	}

	@Override
	public HRACAbstractDirectiveExpressionTreeNode visitSigned_integer_or_directive(Signed_integer_or_directiveContext ctx) {
		HRACAbstractDirectiveExpressionTreeNode childNode = ctx.integer_or_directive().accept(this);
		if (ctx.DASH() != null) {
			return new HRACNegationExpressionNode(childNode);
		}
		return childNode;
	}

}
