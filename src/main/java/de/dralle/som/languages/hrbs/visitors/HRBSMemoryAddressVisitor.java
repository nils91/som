/**
 * 
 */
package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Symbol_osContext;
import de.dralle.som.languages.hrbs.model.HRBSMemoryAddress;
import de.dralle.som.languages.hrbs.model.HRBSSymbol;
import de.dralle.som.languages.hras.generated.HRASGrammarBaseVisitor;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Int_or_symbolContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Offset_specifyContext;
import de.dralle.som.languages.hras.model.MemoryAddress;

/**
 * @author Nils
 *
 */
public class HRBSMemoryAddressVisitor extends HRBSGrammarBaseVisitor<HRBSMemoryAddress> {

	private HRBSMemoryAddress address;

	@Override
	public HRBSMemoryAddress visitSymbol_os(Symbol_osContext ctx) {
		address=new HRBSMemoryAddress();
		if(ctx.getChild(0)!=null) {
			HRBSSymbol s = new HRBSSymbol();
			s.setName(ctx.getChild(0).getText());
			address.setSymbol(s);
		}
		if(ctx.offset_specify()!=null) {
			ctx.offset_specify().accept(this);
		}
		return address;
	}

	@Override
	public HRBSMemoryAddress visitOffset_specify(
			de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Offset_specifyContext ctx) {
		if(ctx.getChild(1)!=null) {
			address.setOffset(Integer.parseInt(ctx.getChild(1).getText()));
		}
		return address;
	}



}
