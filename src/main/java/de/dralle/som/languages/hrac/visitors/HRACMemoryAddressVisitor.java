/**
 * 
 */
package de.dralle.som.languages.hrac.visitors;

import de.dralle.som.languages.hrac.generated.HRACGrammarBaseVisitor;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Symbol_osContext;
import de.dralle.som.languages.hrac.model.HRACMemoryAddress;
import de.dralle.som.languages.hrac.model.HRACMemoryOffset;
import de.dralle.som.languages.hrac.model.HRACSymbol;
import de.dralle.som.languages.hras.generated.HRASGrammarBaseVisitor;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Int_or_symbolContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Offset_specifyContext;
import de.dralle.som.languages.hras.model.MemoryAddress;

/**
 * @author Nils
 *
 */
public class HRACMemoryAddressVisitor extends HRACGrammarBaseVisitor<HRACMemoryAddress> {

	private HRACMemoryAddress address;

	@Override
	public HRACMemoryAddress visitSymbol_os(Symbol_osContext ctx) {
		address=new HRACMemoryAddress();
		if(ctx.SYMBOL()!=null) {
			HRACSymbol s = new HRACSymbol();
			s.setName(ctx.SYMBOL().getText());
			address.setSymbol(s);
		}
		if(ctx.offset_specify()!=null) {
			ctx.offset_specify().accept(this);
		}
		return address;
	}

	@Override
	public HRACMemoryAddress visitOffset_specify(
			de.dralle.som.languages.hrac.generated.HRACGrammarParser.Offset_specifyContext ctx) {
		if(ctx.offset_specify_number()!=null) {
			HRACMemoryOffset offset = ctx.offset_specify_number().accept(new HRACOSVisitor());
			address.setOffsetSpecial(offset.getDirectiveName()!=null);
			address.setOffset(offset.getOffset());
			address.setOffsetSpecialName(offset.getDirectiveName());
		}
		return address;
	}



}
