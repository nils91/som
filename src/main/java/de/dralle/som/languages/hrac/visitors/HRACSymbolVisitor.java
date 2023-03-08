/**
 * 
 */
package de.dralle.som.languages.hrac.visitors;

import de.dralle.som.languages.hrac.generated.HRACGrammarBaseVisitor;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Cnt_specifyContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Symbol_decContext;
import de.dralle.som.languages.hrac.model.HRACMemoryOffset;
import de.dralle.som.languages.hrac.model.HRACSymbol;

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
			HRACMemoryOffset cnt = ctx.cnt_specify().accept(new HRACOSVisitor());
			s.setBitCntSpecial(cnt.getDirectiveName()!=null);
			s.setSpecialName(cnt.getDirectiveName());
			s.setBitCnt(cnt.getOffset());
		}
		if (ctx.symbol_os() != null) {
			s.setTargetSymbol(ctx.symbol_os().accept(new HRACMemoryAddressVisitor()));
		}
		return s;
	}

}
