package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Def_scopeContext;
import de.dralle.som.languages.hrbs.model.HRBSSymbol;
import de.dralle.som.languages.hrbs.model.HRBSSymbolType;

public class HBRSSymbolTypeVisitor extends HRBSGrammarBaseVisitor<HRBSSymbolType>{


	@Override
	public HRBSSymbolType visitDef_scope(Def_scopeContext ctx) {
		if(ctx.LOCAL()!=null) {
			return HRBSSymbolType.local;
		}
		if(ctx.SHARED()!=null) {
			return HRBSSymbolType.shared;
		}
		if(ctx.GLOBAL()!=null) {
			return HRBSSymbolType.global;
		}
		return HRBSSymbolType.local;
	}

}
