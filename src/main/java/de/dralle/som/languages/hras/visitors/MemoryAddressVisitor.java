/**
 * 
 */
package de.dralle.som.languages.hras.visitors;

import de.dralle.som.languages.hras.generated.HRASGrammarBaseVisitor;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Int_or_symbolContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Offset_specifyContext;
import de.dralle.som.languages.hras.model.SymbolHRASMemoryAddress;

/**
 * @author Nils
 *
 */
public class MemoryAddressVisitor extends HRASGrammarBaseVisitor<SymbolHRASMemoryAddress> {

	private SymbolHRASMemoryAddress address;

	@Override
	public SymbolHRASMemoryAddress visitOffset_specify(Offset_specifyContext ctx) {
		if (ctx.primary_expr() != null) {
			address.setAddressOffset(ctx.primary_expr().accept(new ExpressionVisitor()));
		}
		return address;
	}

	@Override
	public SymbolHRASMemoryAddress visitInt_or_symbol(Int_or_symbolContext ctx) {
		address = new SymbolHRASMemoryAddress();
		if (ctx.offset_specify() != null) {
			ctx.offset_specify().accept(this);
		}
		if(ctx.primary_expr()!=null) {
			address.setSymbol(ctx.primary_expr().accept(new ExpressionVisitor()));
			return address;
		}
		else  {
			address.setSymbol(ctx.SYMBOL().getText());
			return address;
		}
	}

	

}
