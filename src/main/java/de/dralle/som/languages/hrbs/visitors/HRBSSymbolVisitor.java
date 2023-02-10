/**
 * 
 */
package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.Opcode;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Cnt_specifyContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Symbol_decContext;
import de.dralle.som.languages.hrbs.model.HRBSCommand;
import de.dralle.som.languages.hrbs.model.HRBSSymbol;
import de.dralle.som.languages.hras.generated.HRASGrammarBaseVisitor;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.CommandContext;
import de.dralle.som.languages.hras.model.Command;

/**
 * @author Nils
 *
 */
public class HRBSSymbolVisitor extends HRBSGrammarBaseVisitor<HRBSSymbol> {
	private HRBSSymbol s;

	@Override
	public HRBSSymbol visitCnt_specify(Cnt_specifyContext ctx) {
		if(ctx.INT()!=null) {
s.setBitCnt(Integer.parseInt(ctx.INT().getText()));			
		}else if(ctx.BI_N()!=null) {
			s.setBitCntISN(true);
		}
		return s;
	}

	@Override
	public HRBSSymbol visitSymbol_dec(Symbol_decContext ctx) {
	s=new HRBSSymbol();
	s.setBitCnt(1);
	if(ctx.SYMBOL()!=null) {
		s.setName(ctx.SYMBOL().getText());
	}
	if(ctx.cnt_specify()!=null) {
		ctx.cnt_specify().accept(this);
	}
	if(ctx.symbol_os()!=null)
	{
		s.setTargetSymbol(ctx.symbol_os().accept(new MemoryAddressVisitor()));}
	return s;
	}



}
