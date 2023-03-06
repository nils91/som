/**
 * 
 */
package de.dralle.som.languages.hrac.visitors;

import de.dralle.som.languages.hrac.generated.HRACGrammarBaseVisitor;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Cnt_specifyContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Symbol_decContext;
import de.dralle.som.languages.hrac.model.HRACSymbol;

/**
 * @author Nils
 *
 */
public class HRACSymbolVisitor extends HRACGrammarBaseVisitor<HRACSymbol> {
	private HRACSymbol s;

	@Override
	public HRACSymbol visitCnt_specify(Cnt_specifyContext ctx) {
		if (ctx.INT() != null) {
			s.setBitCnt(Integer.parseInt(ctx.INT().getText()));
		} else if (ctx.AT() != null) {
			s.setBitCntSpecial(true);
		}
		return s;
	}

	@Override
	public HRACSymbol visitSymbol_dec(Symbol_decContext ctx) {
		s = new HRACSymbol();
		s.setBitCnt(1);
		if (ctx.SYMBOL() != null) {
			s.setName(ctx.SYMBOL().getText());
		}
		if (ctx.cnt_specify() != null) {
			ctx.cnt_specify().accept(this);
		}
		if (ctx.symbol_os() != null) {
			s.setTargetSymbol(ctx.symbol_os().accept(new MemoryAddressVisitor()));
		}
		return s;
	}

}
