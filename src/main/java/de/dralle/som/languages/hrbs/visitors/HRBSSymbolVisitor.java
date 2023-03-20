/**
 * 
 */
package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.Opcode;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Cnt_specifyContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Def_scopeContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Symbol_decContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Symbol_nsContext;
import de.dralle.som.languages.hrbs.model.HRBSCommand;
import de.dralle.som.languages.hrbs.model.HRBSSymbol;
import de.dralle.som.languages.hrbs.model.HRBSSymbolType;
import de.dralle.som.languages.hras.generated.HRASGrammarBaseVisitor;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.CommandContext;
import de.dralle.som.languages.hras.model.HRASCommand;

/**
 * @author Nils
 *
 */
public class HRBSSymbolVisitor extends HRBSGrammarBaseVisitor<HRBSSymbol> {
	private HRBSSymbol s;

	public HRBSSymbolVisitor() {
		s = new HRBSSymbol();
	}

	public HRBSSymbolVisitor(HRBSSymbolType type) {
		this();
		s.setType(type);
	}

	public HRBSSymbolVisitor(HRBSSymbol s) {
		this.s = s;
	}

	@Override
	public HRBSSymbol visitCnt_specify(Cnt_specifyContext ctx) {
		if (ctx.INT() != null) {
			s.setBitCnt(Integer.parseInt(ctx.INT().getText()));
		}
		return s;
	}

	@Override
	public HRBSSymbol visitSymbol_dec(Symbol_decContext ctx) {
		s.setBitCnt(1);
		if (ctx.NAME() != null) {
			s.setName(ctx.NAME().getText());
		}
		if (ctx.cnt_specify() != null) {
			ctx.cnt_specify().accept(this);
		}
		if (ctx.symbol_os() != null) {
			s.setTargetSymbol(ctx.symbol_os().accept(new HRBSMemoryAddressVisitor()));
		}
		return s;
	}

	@Override
	public HRBSSymbol visitSymbol_ns(Symbol_nsContext ctx) {
		HRBSSymbolType symbolType = HRBSSymbolType.local;
		if(ctx.def_scope()!=null) {
			symbolType= ctx.def_scope().accept(new HBRSSymbolTypeVisitor());
		}else {
			
		}
		s.setType(symbolType);
		return ctx.symbol_dec().accept(this);
	}

}
