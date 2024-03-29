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
		if (ctx.NEG_INT() != null) {
			address.setAddressOffset(Integer.parseInt(ctx.NEG_INT().getText()));
		} else if (ctx.INT() != null) {
			address.setAddressOffset(Integer.parseInt(ctx.INT().getText()));
		}
		return address;
	}

	@Override
	public HRASMemoryAddress visitInt_or_symbol(Int_or_symbolContext ctx) {
		address = new HRASMemoryAddress();
		if (ctx.offset_specify() != null) {
			ctx.offset_specify().accept(this);
		}
		if (ctx.getChild(0) != null) {
			address.setSymbol(ctx.getChild(0).getText());
			return address;
		}
		return null;
	}

	

}
