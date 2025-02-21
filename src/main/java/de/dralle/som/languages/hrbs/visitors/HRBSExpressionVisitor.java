package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.Util;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Absolute_exprContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Additive_exprContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Directive_accessContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Factorial_exprContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Integer_or_directiveContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Multiplicative_exprContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Par_exprContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Power_exprContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Primary_exprContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Signed_integer_or_directiveContext;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbsoluteExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSDirectiveNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSDivisionExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSFactorialExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSIntegerNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSMinusExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSModuloExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSMultiplicationExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSNegationExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSPlusExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSPowerExpressionNode;

public class HRBSExpressionVisitor extends HRBSGrammarBaseVisitor<HRBSAbstractExpressionNode> {

	@Override
	public HRBSAbstractExpressionNode visitAbsolute_expr(Absolute_exprContext ctx) {
		HRBSAbstractExpressionNode child0 = ctx.par_expr().accept(this);
		if (ctx.PIPE() != null && ctx.PIPE().size() == 2) {
			return new HRBSAbsoluteExpressionNode(child0);
		}
		return child0;
	}

	@Override
	public HRBSAbstractExpressionNode visitAdditive_expr(Additive_exprContext ctx) {
		HRBSAbstractExpressionNode child1 = ctx.multiplicative_expr().accept(this);
		HRBSAbstractExpressionNode child0 = null;
		if (ctx.additive_expr() != null) {
			child0 = ctx.additive_expr().accept(this);
			if (ctx.PLUS() != null) {
				return new HRBSPlusExpressionNode(child0, child1);
			} else if (ctx.DASH() != null) {
				return new HRBSMinusExpressionNode(child0, child1);
			}
		}
		return child1;
	}

	@Override
	public HRBSAbstractExpressionNode visitDirective_access(Directive_accessContext ctx) {
		return new HRBSDirectiveNode(ctx.directive_name().getText());
	}

	@Override
	public HRBSAbstractExpressionNode visitFactorial_expr(Factorial_exprContext ctx) {
		HRBSAbstractExpressionNode child0 = ctx.absolute_expr().accept(this);
		if (ctx.EXCL() != null) {
			return new HRBSFactorialExpressionNode(child0);
		}
		return child0;
	}

	@Override
	public HRBSAbstractExpressionNode visitInteger_or_directive(Integer_or_directiveContext ctx) {
		if (ctx.INT() != null) {
			return new HRBSIntegerNode(Util.decodeInt(ctx.INT().getText()));
		} else if (ctx.directive_access() != null) {
			return ctx.directive_access().accept(this);
		}
		return null;
	}

	@Override
	public HRBSAbstractExpressionNode visitMultiplicative_expr(Multiplicative_exprContext ctx) {
		HRBSAbstractExpressionNode child1 = ctx.power_expr().accept(this);
		HRBSAbstractExpressionNode child0 = null;
		if (ctx.multiplicative_expr() != null) {
			child0 = ctx.multiplicative_expr().accept(this);
			if (ctx.MUL() != null) {
				return new HRBSMultiplicationExpressionNode(child0, child1);
			} else if (ctx.DIV() != null) {
				return new HRBSDivisionExpressionNode(child0, child1);
			} else if (ctx.MOD() != null) {
				return new HRBSModuloExpressionNode(child0, child1);
			}
		}
		return child1;
	}

	@Override
	public HRBSAbstractExpressionNode visitPar_expr(Par_exprContext ctx) {
		if (ctx.signed_integer_or_directive() != null) {
			return ctx.signed_integer_or_directive().accept(this);
		}
		if (ctx.primary_expr() != null) {
			return ctx.primary_expr().accept(this);
		}
		return null;
	}

	@Override
	public HRBSAbstractExpressionNode visitPower_expr(Power_exprContext ctx) {
		HRBSAbstractExpressionNode child0 = ctx.factorial_expr().accept(this);
		HRBSAbstractExpressionNode child1 = null;
		if (ctx.power_expr() != null) {
			child1 = ctx.power_expr().accept(this);
			return new HRBSPowerExpressionNode(child0, child1);

		}
		return child0;
	}

	@Override
	public HRBSAbstractExpressionNode visitPrimary_expr(Primary_exprContext ctx) {
		return ctx.additive_expr().accept(this);
	}

	@Override
	public HRBSAbstractExpressionNode visitSigned_integer_or_directive(Signed_integer_or_directiveContext ctx) {
		HRBSAbstractExpressionNode childNode = ctx.integer_or_directive().accept(this);
		if (ctx.DASH() != null) {
			return new HRBSNegationExpressionNode(childNode);
		}
		return childNode;
	}

}
