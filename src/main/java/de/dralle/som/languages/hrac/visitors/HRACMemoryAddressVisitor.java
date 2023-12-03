/**
 * 
 */
package de.dralle.som.languages.hrac.visitors;

import de.dralle.som.languages.hrac.generated.HRACGrammarBaseVisitor;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Symbol_osContext;
import de.dralle.som.languages.hrac.model.AbstractHRACMemoryAddress;
import de.dralle.som.languages.hrac.model.HRACMemoryOffset;
import de.dralle.som.languages.hrac.model.HRACSymbol;
import de.dralle.som.languages.hrac.model.NamedHRACMemoryAddress;

/**
 * @author Nils
 *
 */
public class HRACMemoryAddressVisitor extends HRACGrammarBaseVisitor<AbstractHRACMemoryAddress> {

	private AbstractHRACMemoryAddress address;

	@Override
	public AbstractHRACMemoryAddress visitSymbol_os(Symbol_osContext ctx) {
		if (ctx.SYMBOL() != null) { //is a named address
			address=new NamedHRACMemoryAddress(ctx.SYMBOL().getText());
		}
		if(ctx.memadr()!=null) {
			ctx.memadr().accept(this);
		}
		if (ctx.offset_specify() != null) {
			ctx.offset_specify().accept(this);
		}
		return address;
	}

	@Override
	public AbstractHRACMemoryAddress visitOffset_specify(
			de.dralle.som.languages.hrac.generated.HRACGrammarParser.Offset_specifyContext ctx) {
		if (ctx.offset_specify_number() != null) {
			HRACMemoryOffset offset = ctx.offset_specify_number().accept(new HRACOSVisitor());
			address.setOffsetSpecial(offset.getDirectiveName() != null);
			address.setOffset(offset.getOffset());
			address.setOffsetSpecialName(offset.getDirectiveName());
		}
		return address;
	}

}
