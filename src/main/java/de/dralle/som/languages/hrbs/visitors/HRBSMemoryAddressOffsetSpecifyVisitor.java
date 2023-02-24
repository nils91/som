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
public class HRBSMemoryAddressOffsetSpecifyVisitor extends HRBSGrammarBaseVisitor<HRBSMemoryAddress> {

	private HRBSMemoryAddress address;
	private int nxtOffset;

	public int getNxtOffset() {
		return nxtOffset;
	}
	public void setNxtOffset(int nxtOffset) {
		this.nxtOffset = nxtOffset;
	}
	public HRBSMemoryAddressOffsetSpecifyVisitor() {
		address=new HRBSMemoryAddress();
	}
	public HRBSMemoryAddressOffsetSpecifyVisitor(HRBSMemoryAddress address) {
		this.address=address;
	}

	@Override
	public HRBSMemoryAddress visitOffset_specify(
			de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Offset_specifyContext ctx) {
		int offset = Integer.parseInt(ctx.getChild(1).getText());
		if( nxtOffset==0) {
			address.setOffset(offset);
		}else {
			address.setDerefOffset(offset);
		}
		return address;
	}



}
