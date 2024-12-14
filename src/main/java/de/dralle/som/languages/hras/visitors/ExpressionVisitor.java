package de.dralle.som.languages.hras.visitors;

import de.dralle.som.languages.hras.generated.HRASGrammarBaseVisitor;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Absolute_exprContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Additive_exprContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Factorial_exprContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Multiplicative_exprContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Par_exprContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Power_exprContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Primary_exprContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Signed_integerContext;
import de.dralle.som.languages.hras.model.HRASAbsoluteExpressionNode;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASDivisionExpressionNode;
import de.dralle.som.languages.hras.model.HRASFactorialExpressionNode;
import de.dralle.som.languages.hras.model.HRASIntegerNode;
import de.dralle.som.languages.hras.model.HRASMinusExpressionNode;
import de.dralle.som.languages.hras.model.HRASModuloExpression;
import de.dralle.som.languages.hras.model.MultiplicationExpression;
import de.dralle.som.languages.hras.model.PlusExpressionNode;
import de.dralle.som.languages.hras.model.PowerExpressionNode;

public class ExpressionVisitor extends HRASGrammarBaseVisitor<HRASAbstractExpressionNode>{

	@Override
	public HRASAbstractExpressionNode visitSigned_integer(Signed_integerContext ctx) {
		return new HRASIntegerNode(ctx.accept(new HRASNumberVisitor()));
	}

	@Override
	public HRASAbstractExpressionNode visitPrimary_expr(Primary_exprContext ctx) {
		return ctx.additive_expr().accept(this);
	}

	@Override
	public HRASAbstractExpressionNode visitAdditive_expr(Additive_exprContext ctx) {
		HRASAbstractExpressionNode child1 = ctx.multiplicative_expr().accept(this);
		HRASAbstractExpressionNode child0 = null;
		if(ctx.additive_expr()!=null) {
			child0=ctx.additive_expr().accept(this);
			if(ctx.PLUS()!=null) {
				return new PlusExpressionNode(child0,child1);
			}else if(ctx.DASH()!=null) {
				return new HRASMinusExpressionNode(child0,child1);
			}
		}
		return child1;
	}

	@Override
	public HRASAbstractExpressionNode visitMultiplicative_expr(Multiplicative_exprContext ctx) {
		HRASAbstractExpressionNode child1 = ctx.power_expr().accept(this);
		HRASAbstractExpressionNode child0 = null;
		if(ctx.multiplicative_expr()!=null) {
			child0=ctx.multiplicative_expr().accept(this);
			if(ctx.MUL()!=null) {
				return new MultiplicationExpression(child0,child1);
			}else if(ctx.DIV()!=null) {
				return new HRASDivisionExpressionNode(child0,child1);
			}else if(ctx.MOD()!=null) {
				return new HRASModuloExpression(child0,child1);
			}
		}
		return child1;
	}

	@Override
	public HRASAbstractExpressionNode visitPower_expr(Power_exprContext ctx) {
		HRASAbstractExpressionNode child0 = ctx.factorial_expr().accept(this);
		HRASAbstractExpressionNode child1 = null;
		if(ctx.power_expr()!=null) {
			child1=ctx.power_expr().accept(this);
				return new PowerExpressionNode(child0,child1);
			
		}
		return child0;
	}

	@Override
	public HRASAbstractExpressionNode visitFactorial_expr(Factorial_exprContext ctx) {
		HRASAbstractExpressionNode child0 = ctx.absolute_expr().accept(this);
		if(ctx.EXCL()!=null) {
			return new HRASFactorialExpressionNode(child0);
		}
		return child0;
	}

	@Override
	public HRASAbstractExpressionNode visitAbsolute_expr(Absolute_exprContext ctx) {
		HRASAbstractExpressionNode child0 = ctx.par_expr().accept(this);
		if(ctx.PIPE()!=null&&ctx.PIPE().size()==2) {
			return new HRASAbsoluteExpressionNode(child0);
		}
		return child0;
	}

	@Override
	public HRASAbstractExpressionNode visitPar_expr(Par_exprContext ctx) {
		if(ctx.signed_integer()!=null) {
			return ctx.signed_integer().accept(this);
		}
		if(ctx.primary_expr()!=null) {
			return ctx.primary_expr().accept(this);
		}
		return null;
	}

}
