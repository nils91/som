/**
 * 
 */
package de.dralle.som.languages.hras.visitors;

import de.dralle.som.languages.hras.generated.HRASGrammarBaseVisitor;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Int_or_symbolContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Offset_specifyContext;
import de.dralle.som.languages.hras.model.HRASMemoryAddress;

/**
 * @author Nils
 *
 */
public class MemoryAddressVisitor extends HRASGrammarBaseVisitor<HRASMemoryAddress> {

	private HRASMemoryAddress address;

	@Override
	public HRASMemoryAddress visitOffset_specify(Offset_specifyContext ctx) {
		if (ctx.neg_integer() != null) {
			address.setAddressOffset(ctx.neg_integer().accept(new HRASNumberVisitor()));
		} else if (ctx.integer() != null) {
			address.setAddressOffset(ctx.integer().accept(new HRASNumberVisitor()));
		}
		return address;
	}

	@Override
	public HRASMemoryAddress visitInt_or_symbol(Int_or_symbolContext ctx) {
		address = new HRASMemoryAddress();
		if (ctx.offset_specify() != null) {
			ctx.offset_specify().accept(this);
		}
		if(ctx.integer()!=null) {
			address.setSymbol(ctx.integer().accept(new HRASNumberVisitor()));
			return address;
		}
		else  {
			address.setSymbol(ctx.SYMBOL().getText());
			return address;
		}
	}

	

}
