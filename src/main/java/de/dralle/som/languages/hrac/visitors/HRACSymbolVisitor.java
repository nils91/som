/**
 * 
 */
package de.dralle.som.languages.hrac.visitors;

import de.dralle.som.languages.hrac.generated.HRACGrammarBaseVisitor;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Symbol_decContext;
import de.dralle.som.languages.hrac.model.HRACSymbol;
import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractDirectiveExpressionTreeNode;

/**
 * @author Nils
 *
 */
public class HRACSymbolVisitor extends HRACGrammarBaseVisitor<HRACSymbol> {
	private HRACSymbol s;

	@Override
	public HRACSymbol visitSymbol_dec(Symbol_decContext ctx) {
		s = new HRACSymbol();
		s.setBitCnt(1);
		if (ctx.SYMBOL() != null) {
			s.setName(ctx.SYMBOL().getText());
		}
		if (ctx.cnt_specify() != null) {
			HRACAbstractDirectiveExpressionTreeNode cnt = ctx.cnt_specify().accept(new HRACOSVisitor());
			s.setBitCnt(cnt);
		}
		if (ctx.symbol_os() != null) {
			s.setTargetSymbol(ctx.symbol_os().accept(new HRACMemoryAddressVisitor()));
		}
		s.setOp(ctx.OP()!=null);
		return s;
	}

}
