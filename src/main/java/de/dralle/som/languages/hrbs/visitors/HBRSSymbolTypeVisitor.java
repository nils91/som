package de.dralle.som.languages.hrbs.visitors;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;

import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Def_scopeContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Def_scope_sharedContext;
import de.dralle.som.languages.hrbs.model.HBRSSymbolTypeExtended;
import de.dralle.som.languages.hrbs.model.HRBSSymbol;
import de.dralle.som.languages.hrbs.model.HRBSSymbolType;
import de.dralle.som.languages.hrbs.model.IHBRSSymbolType;

public class HBRSSymbolTypeVisitor extends HRBSGrammarBaseVisitor<IHBRSSymbolType>{

	@Override
	public IHBRSSymbolType visitDef_scope_shared(Def_scope_sharedContext ctx) {
		List<String> sharedWith=new ArrayList<>();
		for (TerminalNode string : ctx.NAME()) {
			sharedWith.add(string.getText());
		}
		return HBRSSymbolTypeExtended.getSharedType(sharedWith);
	}

	@Override
	public IHBRSSymbolType visitDef_scope(Def_scopeContext ctx) {
		if(ctx.LOCAL()!=null) {
			return HBRSSymbolTypeExtended.getLocalType();
		}
		if(ctx.def_scope_shared()!=null) {
			return ctx.def_scope_shared().accept(this);
		}
		if(ctx.GLOBAL()!=null) {
			return HBRSSymbolTypeExtended.getGlobalType();
		}
		return HBRSSymbolTypeExtended.getLocalType();
	}

}
