package de.dralle.som.languages.hrad.visitors;

import java.util.ArrayList;
import java.util.List;

import de.dralle.som.Util;
import de.dralle.som.languages.hrad.generated.HRADGrammarBaseVisitor;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.Absolute_exprContext;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.Additive_exprContext;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.Directive_accessContext;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.Directive_nameContext;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.Factorial_exprContext;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.Integer_or_directiveContext;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.Multiplicative_exprContext;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.Par_exprContext;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.Power_exprContext;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.Primary_exprContext;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.Signed_integer_or_directiveContext;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbsoluteExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADComplexNamedDirectiveNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADDivisionExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADFactorialExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADIntegerNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADMinusExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADModuloExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADMultiplicationExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADNegationExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADPlusExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADPowerExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADStringNamedDirectiveNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADStringNode;
import de.dralle.som.languages.hrad.generated.HRADGrammarBaseVisitor;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;

public class HRADExpressionVisitor extends HRADGrammarBaseVisitor<HRADAbstractDirectiveExpressionTreeNode> {

	@Override
	public HRADAbstractDirectiveExpressionTreeNode visitDirective_name(Directive_nameContext ctx) {
		if (ctx.INT() != null) {
			return new HRADIntegerNode(Util.decodeInt(ctx.INT().getText()));
		} else if (ctx.NAME() != null) {
			return new HRADStringNode(ctx.NAME().getText());
		} else if (ctx.directive_access() != null) {
			return ctx.directive_access().accept(new HRADExpressionVisitor());
		} else if (ctx.primary_expr() != null) {
			return ctx.primary_expr().accept(new HRADExpressionVisitor());
		}
		return null;
	}

	@Override
	public HRADAbstractDirectiveExpressionTreeNode visitAbsolute_expr(Absolute_exprContext ctx) {
		HRADAbstractDirectiveExpressionTreeNode child0 = ctx.par_expr().accept(this);
		if (ctx.PIPE() != null && ctx.PIPE().size() == 2) {
			return new HRADAbsoluteExpressionNode(child0);
		}
		return child0;
	}

	@Override
	public HRADAbstractDirectiveExpressionTreeNode visitAdditive_expr(Additive_exprContext ctx) {
		HRADAbstractDirectiveExpressionTreeNode child1 = ctx.multiplicative_expr().accept(this);
		HRADAbstractDirectiveExpressionTreeNode child0 = null;
		if (ctx.additive_expr() != null) {
			child0 = ctx.additive_expr().accept(this);
			if (ctx.PLUS() != null) {
				return new HRADPlusExpressionNode(child0, child1);
			} else if (ctx.DASH() != null) {
				return new HRADMinusExpressionNode(child0, child1);
			}
		}
		return child1;
	}

	@Override
	public HRADAbstractDirectiveExpressionTreeNode visitDirective_access(Directive_accessContext ctx) {
		HRADAbstractDirectiveExpressionTreeNode name = ctx.directive_name().accept(new HRADExpressionVisitor());
		HRADAbstractDirectiveNode<?> returnNode=null;
		if(name instanceof HRADIntegerNode) {
			returnNode= new HRADStringNamedDirectiveNode(((HRADIntegerNode)name).getValue()+"");
		}
		else if(name instanceof HRADStringNode) {
			returnNode= new HRADStringNamedDirectiveNode(((HRADStringNode)name).getValue()+"");
		}
		else{
			returnNode= new HRADComplexNamedDirectiveNode(name);
		}
		if(ctx.primary_expr()!=null) {
			List<HRADAbstractDirectiveExpressionTreeNode> pValues = returnNode.getParamValues();
			if(pValues==null) {
				pValues=new ArrayList<HRADAbstractDirectiveExpressionTreeNode>();
			}
			for (Primary_exprContext hradAbstractDirectiveExpressionTreeNode : ctx.primary_expr()) {
				pValues.add(hradAbstractDirectiveExpressionTreeNode.accept(new HRADExpressionVisitor()));
			}
			returnNode.setParamValues(pValues);
		}
		return returnNode;
	}

	@Override
	public HRADAbstractDirectiveExpressionTreeNode visitFactorial_expr(Factorial_exprContext ctx) {
		HRADAbstractDirectiveExpressionTreeNode child0 = ctx.absolute_expr().accept(this);
		if (ctx.EXCL() != null) {
			return new HRADFactorialExpressionNode(child0);
		}
		return child0;
	}

	@Override
	public HRADAbstractDirectiveExpressionTreeNode visitInteger_or_directive(Integer_or_directiveContext ctx) {
		if (ctx.number() != null) {
			return new HRADIntegerNode(ctx.number().accept(new HRADNumberVisitor()));
		} else if (ctx.directive_access() != null) {
			return ctx.directive_access().accept(this);
		} else if (ctx.DIRECTIVE_VALUE_STR()!=null) {
			return new HRADStringNode(ctx.DIRECTIVE_VALUE_STR().getText().substring(1, ctx.DIRECTIVE_VALUE_STR().getText().length()-1));
		}
		return null;
	}

	@Override
	public HRADAbstractDirectiveExpressionTreeNode visitMultiplicative_expr(Multiplicative_exprContext ctx) {
		HRADAbstractDirectiveExpressionTreeNode child1 = ctx.power_expr().accept(this);
		HRADAbstractDirectiveExpressionTreeNode child0 = null;
		if (ctx.multiplicative_expr() != null) {
			child0 = ctx.multiplicative_expr().accept(this);
			if (ctx.MUL() != null) {
				return new HRADMultiplicationExpressionNode(child0, child1);
			} else if (ctx.DIV() != null) {
				return new HRADDivisionExpressionNode(child0, child1);
			} else if (ctx.MOD() != null) {
				return new HRADModuloExpressionNode(child0, child1);
			}
		}
		return child1;
	}

	@Override
	public HRADAbstractDirectiveExpressionTreeNode visitPar_expr(Par_exprContext ctx) {
		if (ctx.signed_integer_or_directive() != null) {
			return ctx.signed_integer_or_directive().accept(this);
		}
		if (ctx.primary_expr() != null) {
			return ctx.primary_expr().accept(this);
		}
		return null;
	}

	@Override
	public HRADAbstractDirectiveExpressionTreeNode visitPower_expr(Power_exprContext ctx) {
		HRADAbstractDirectiveExpressionTreeNode child0 = ctx.factorial_expr().accept(this);
		HRADAbstractDirectiveExpressionTreeNode child1 = null;
		if (ctx.power_expr() != null) {
			child1 = ctx.power_expr().accept(this);
			return new HRADPowerExpressionNode(child0, child1);

		}
		return child0;
	}

	@Override
	public HRADAbstractDirectiveExpressionTreeNode visitPrimary_expr(Primary_exprContext ctx) {
		return ctx.additive_expr().accept(this);
	}

	@Override
	public HRADAbstractDirectiveExpressionTreeNode visitSigned_integer_or_directive(
			Signed_integer_or_directiveContext ctx) {
		HRADAbstractDirectiveExpressionTreeNode childNode = ctx.integer_or_directive().accept(this);
		if (ctx.DASH() != null) {
			return new HRADNegationExpressionNode(childNode);
		}
		return childNode;
	}

}
