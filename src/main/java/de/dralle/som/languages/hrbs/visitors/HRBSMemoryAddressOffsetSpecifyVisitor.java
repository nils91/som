/**
 * 
 */
package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Directive_accessContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Offset_specify_numberContext;
import de.dralle.som.languages.hrbs.model.AbstractHRBSMemoryAddress;
import de.dralle.som.languages.hrbs.model.HRBSMemoryAddressOffset;
import de.dralle.som.languages.hrbs.model.HRBSSymbol;
import de.dralle.som.Util;
import de.dralle.som.languages.hras.generated.HRASGrammarBaseVisitor;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Int_or_symbolContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Offset_specifyContext;
import de.dralle.som.languages.hras.model.SymbolHRASMemoryAddress;

/**
 * @author Nils
 *
 */
public class HRBSMemoryAddressOffsetSpecifyVisitor extends HRBSGrammarBaseVisitor<HRBSMemoryAddressOffset> {

	private HRBSMemoryAddressOffset o;

	public HRBSMemoryAddressOffsetSpecifyVisitor() {
		o=new HRBSMemoryAddressOffset();
	}
	public HRBSMemoryAddressOffsetSpecifyVisitor(HRBSMemoryAddressOffset o) {
		this.o=o;
	}

	@Override
	public HRBSMemoryAddressOffset visitOffset_specify(HRBSGrammarParser.Offset_specifyContext ctx) {
		ctx.offset_specify_number().accept(this);
		return o;
	}
	@Override
	public HRBSMemoryAddressOffset visitOffset_specify_number(Offset_specify_numberContext ctx) {
		if(ctx.INT()!=null) {
			o.setOffset(Util.decodeInt(ctx.INT().getText()));
		}
		if(ctx.NEG_INT()!=null) {
			o.setOffset(Util.decodeInt(ctx.NEG_INT().getText()));
		}
		if(ctx.directive_access()!=null) {
			ctx.directive_access().accept(this);
		}
		return o;
	}
	@Override
	public HRBSMemoryAddressOffset visitDirective_access(Directive_accessContext ctx) {
		o.setDirectiveAccessName(ctx.directive_name().getText());
		return o;
	}



}
