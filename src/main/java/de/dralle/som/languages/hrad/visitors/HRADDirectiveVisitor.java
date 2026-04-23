package de.dralle.som.languages.hrad.visitors;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import de.dralle.som.Util;
import de.dralle.som.languages.hrad.HRADSourceLocation;
import de.dralle.som.languages.hrad.generated.HRADGrammarBaseVisitor;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.DirectiveContext;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.Directive_functionContext;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.Directive_nameContext;
import de.dralle.som.languages.hrad.generated.HRADGrammarParser.Simple_directiveContext;
import de.dralle.som.languages.hrad.model.HRADAbstractDirectiveStatement;
import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hrad.model.HRADStringNamedDirectiveStatement;
import de.dralle.som.languages.hrad.model.HRADComplexNamedDirectiveStatement;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADIntegerNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADStringNode;

public class HRADDirectiveVisitor extends HRADGrammarBaseVisitor<HRADAbstractDirectiveStatement<?>> {
	private HRADAbstractDirectiveStatement<?> directive = null;

	@Override
	public HRADAbstractDirectiveStatement<?> visitDirective(DirectiveContext ctx) {
		if (ctx.simple_directive() != null) {
			ctx.simple_directive().accept(this);
		}
		if (ctx.directive_function() != null) {
			ctx.directive_function().accept(this);
		}
		return directive;
	}

	@Override
	public HRADAbstractDirectiveStatement<?> visitDirective_function(Directive_functionContext ctx) {
		if (ctx.directive_name() != null) {
			for (Directive_nameContext iterable_element : ctx.directive_name()) {
				iterable_element.accept(this);
			}
		}
		if (ctx.primary_expr() != null) {
			directive.setValue(ctx.primary_expr().accept(new HRADExpressionVisitor()));
		}
		return directive;
	}

	@Override
	public HRADAbstractDirectiveStatement<?> visitSimple_directive(Simple_directiveContext ctx) {
		if (ctx.directive_name() != null) {
			directive= ctx.directive_name().accept(this);
		}
		if (ctx.primary_expr() != null) {
			directive.setValue(ctx.primary_expr().accept(new HRADExpressionVisitor()));
		}
		return directive;
	}

	@Override
	public HRADAbstractDirectiveStatement<?> visitDirective_name(Directive_nameContext ctx) {
		String strName = null;
		HRADAbstractDirectiveExpressionTreeNode complexName = null;
		if (ctx.INT() != null) {
			strName = ctx.INT().getText();
		} else if (ctx.NAME() != null) {
			strName = ctx.NAME().getText();
		} else if (ctx.directive_access() != null) {
			complexName = ctx.directive_access().accept(new HRADExpressionVisitor());
		} else if (ctx.primary_expr() != null) {
			complexName = ctx.primary_expr().accept(new HRADExpressionVisitor());
		}
		if (strName != null) {
			if (directive == null) {
				directive = new HRADStringNamedDirectiveStatement(strName, null,
						new HRADSourceLocation("", ctx.start.getLine(), ctx.start.getStartIndex()));
			} else {// assume all following directive names are params
				List<HRADAbstractDirectiveExpressionTreeNode> params = directive.getParams();
				if (params == null) {
					params = new ArrayList<HRADAbstractDirectiveExpressionTreeNode>();
				}
				if (ctx.INT() != null) {
					params.add(new HRADIntegerNode(Util.decodeInt(ctx.INT().getText())));
				} else if (ctx.NAME() != null) {
					params.add(new HRADStringNode(ctx.NAME().getText()));
				} else if (ctx.directive_access() != null) {
					params.add(ctx.directive_access().accept(new HRADExpressionVisitor()));
				} else if (ctx.primary_expr() != null) {
					params.add(ctx.primary_expr().accept(new HRADExpressionVisitor()));
				}
			}
		}
		if (complexName != null) {
			if (directive == null) {
				directive = new HRADComplexNamedDirectiveStatement(complexName, null,
						new HRADSourceLocation("", ctx.start.getLine(), ctx.start.getStartIndex()));
			} else {// assume all following directive names are params
				List<HRADAbstractDirectiveExpressionTreeNode> params = directive.getParams();
				if (params == null) {
					params = new ArrayList<HRADAbstractDirectiveExpressionTreeNode>();
				}
				if (ctx.INT() != null) {
					params.add(new HRADIntegerNode(Util.decodeInt(ctx.INT().getText())));
				} else if (ctx.NAME() != null) {
					params.add(new HRADStringNode(ctx.NAME().getText()));
				} else if (ctx.directive_access() != null) {
					params.add(ctx.directive_access().accept(new HRADExpressionVisitor()));
				} else if (ctx.primary_expr() != null) {
					params.add(ctx.primary_expr().accept(new HRADExpressionVisitor()));
				}
			}
		}
		return directive;
	}

}
