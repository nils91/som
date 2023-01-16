/**
 * 
 */
package de.dralle.som.languages.hra.visitors;

import de.dralle.som.Opcode;
import de.dralle.som.languages.hra.generated.HRAGrammarBaseVisitor;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.CommandContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.DirectiveContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.Int_or_symbolContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.LineContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.Offset_specifyContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.ProgramContext;
import de.dralle.som.languages.hra.generated.HRAGrammarParser.Symbol_decContext;
import de.dralle.som.languages.hra.model.Command;
import de.dralle.som.languages.hra.model.HRAModel;
import de.dralle.som.languages.hra.model.MemoryAddress;

/**
 * @author Nils
 *
 */
public class MemoryAddressVisitor extends HRAGrammarBaseVisitor<MemoryAddress> {

	private MemoryAddress address;

	@Override
	public MemoryAddress visitOffset_specify(Offset_specifyContext ctx) {
		if (ctx.NEG_INT() != null) {
			address.setAddressOffset(Integer.parseInt(ctx.NEG_INT().toString()));
		} else if (ctx.INT() != null) {
			address.setAddressOffset(Integer.parseInt(ctx.INT().toString()));
		}
		return address;
	}

	@Override
	public MemoryAddress visitInt_or_symbol(Int_or_symbolContext ctx) {
		address = new MemoryAddress();
		if (ctx.offset_specify() != null) {
			ctx.offset_specify().accept(this);
		}
		if (ctx.getChild(0) != null) {
			address.setSymbol(ctx.getChild(0).toString());
			return address;
		}
		return null;
	}

	

}
