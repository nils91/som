/**
 * 
 */
package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Symbol_osContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Symbol_target_nnameContext;
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

	public HRBSMemoryAddressVisitor() {
		address=new HRBSMemoryAddress();
	}
	@Override
	public HRBSMemoryAddress visitSymbol_os(Symbol_osContext ctx) {
		if(ctx.AMP()!=null) {
			address.setDeref(true);
		}
		if(ctx.symbol_target_nname()!=null) {
			ctx.symbol_target_nname().accept(this);
		}
		if(ctx.offset_specify()!=null) {
			HRBSMemoryAddressOffsetSpecifyVisitor maov = new HRBSMemoryAddressOffsetSpecifyVisitor(address);
			for (int i = 0; i < ctx.offset_specify().size();i++) {
				maov.setNxtOffset(i);
				de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Offset_specifyContext os = ctx.offset_specify(i);
				os.accept(maov);
			}
		}
		return address;
	}

	@Override
	public HRBSMemoryAddress visitSymbol_target_nname(Symbol_target_nnameContext ctx) {
		boolean isBuiltIn=ctx.builtins()!=null;
		if(isBuiltIn) {
			address.setSymbol(new HRBSSymbol(ctx.builtins().getText()));
			if(ctx.NAME().size()>=1) {
				address.setTgtCmd(ctx.NAME(0).getText());				
			}
		}else {
			if(ctx.NAME().size()==1) {
				address.setSymbol(new HRBSSymbol(ctx.NAME(0).getText()));
			}
			if(ctx.NAME().size()>=2) {
				address.setTgtCmd(ctx.NAME(0).getText());
				address.setSymbol(new HRBSSymbol(ctx.NAME(1).getText()));
			}
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
