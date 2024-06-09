/**
 * 
 */
package de.dralle.som.languages.hras.visitors;

import de.dralle.som.languages.hras.generated.HRASGrammarBaseVisitor;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Int_or_symbolContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Offset_specifyContext;
import de.dralle.som.languages.hras.model.AbstractHRASMemoryAddress;
import de.dralle.som.languages.hras.model.ExpressionHRASMemoryAddress;
import de.dralle.som.languages.hras.model.SymbolHRASMemoryAddress;

/**
 * @author Nils
 *
 */
public class MemoryAddressVisitor extends HRASGrammarBaseVisitor<AbstractHRASMemoryAddress> {

	private AbstractHRASMemoryAddress address;

	@Override
	public AbstractHRASMemoryAddress visitOffset_specify(Offset_specifyContext ctx) {
		if (ctx.primary_expr() != null) {
			address.setAddressOffset(ctx.primary_expr().accept(new ExpressionVisitor()));
		}
		return address;
	}

	@Override
	public AbstractHRASMemoryAddress visitInt_or_symbol(Int_or_symbolContext ctx) {
		if(ctx.primary_expr()!=null) {
			address=new ExpressionHRASMemoryAddress(ctx.primary_expr().accept(new ExpressionVisitor()));
		}
		else  {
			address=new SymbolHRASMemoryAddress(ctx.SYMBOL().getText());
		}
		if (ctx.offset_specify() != null) {
			ctx.offset_specify().accept(this);
		}
		return address;
		
	}

	

}
