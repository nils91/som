/**
 * 
 */
package de.dralle.som.languages.hrac.visitors;

import de.dralle.som.Util;
import de.dralle.som.languages.hrac.generated.HRACGrammarBaseVisitor;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.MemadrContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Symbol_osContext;
import de.dralle.som.languages.hrac.model.AbstractHRACMemoryAddress;
import de.dralle.som.languages.hrac.model.FixedHRACMemoryAddress;
import de.dralle.som.languages.hrac.model.HRACSymbol;
import de.dralle.som.languages.hrac.model.NamedHRACMemoryAddress;
import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;

/**
 * @author Nils
 *
 */
public class HRACMemoryAddressVisitor extends HRACGrammarBaseVisitor<AbstractHRACMemoryAddress> {

	@Override
	public AbstractHRACMemoryAddress visitMemadr(MemadrContext ctx) {
		return new FixedHRACMemoryAddress(ctx.par_expr().accept(new HRACExpressionVisitor()));
	}

	private AbstractHRACMemoryAddress address;

	@Override
	public AbstractHRACMemoryAddress visitSymbol_os(Symbol_osContext ctx) {
		if (ctx.SYMBOL() != null) { //is a named address
			address=new NamedHRACMemoryAddress(ctx.SYMBOL().getText());
		}
		if(ctx.memadr()!=null) {
			address=ctx.memadr().accept(this);
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
			HRACAbstractExpressionNode offset = ctx.offset_specify_number().accept(new HRACOSVisitor());
			address.setOffset(offset);
		}
		return address;
	}

}
