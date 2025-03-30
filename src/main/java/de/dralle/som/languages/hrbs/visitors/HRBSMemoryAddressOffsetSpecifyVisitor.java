/**
 * 
 */
package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Directive_accessContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Offset_specify_numberContext;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSDirectiveNode;

/**
 * @author Nils
 *
 */
public class HRBSMemoryAddressOffsetSpecifyVisitor extends HRBSGrammarBaseVisitor<HRBSAbstractExpressionNode> {

	private HRBSAbstractExpressionNode o;

	public HRBSMemoryAddressOffsetSpecifyVisitor() {

	}

	@Override
	public HRBSAbstractExpressionNode visitDirective_access(Directive_accessContext ctx) {
		o = new HRBSDirectiveNode(ctx.directive_name().getText());
		return o;
	}

	@Override
	public HRBSAbstractExpressionNode visitOffset_specify(HRBSGrammarParser.Offset_specifyContext ctx) {
		o = ctx.offset_specify_number().accept(this);
		return o;
	}

	@Override
	public HRBSAbstractExpressionNode visitOffset_specify_number(Offset_specify_numberContext ctx) {
		if (ctx.primary_expr() != null) {
			o = ((ctx.primary_expr().accept(new HRBSExpressionVisitor())));
		}
		if (ctx.directive_access() != null) {
			o = ctx.directive_access().accept(this);
		}
		return o;
	}

}
