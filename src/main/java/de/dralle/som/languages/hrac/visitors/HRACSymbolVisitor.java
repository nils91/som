/**
 * 
 */
package de.dralle.som.languages.hrac.visitors;

import de.dralle.som.Opcode;
import de.dralle.som.languages.hrac.generated.HRACGrammarBaseVisitor;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Cnt_specifyContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Symbol_decContext;
import de.dralle.som.languages.hrac.model.HRACCommand;
import de.dralle.som.languages.hrac.model.HRACSymbol;
import de.dralle.som.languages.hras.generated.HRASGrammarBaseVisitor;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.CommandContext;
import de.dralle.som.languages.hras.model.Command;

/**
 * @author Nils
 *
 */
public class HRACSymbolVisitor extends HRACGrammarBaseVisitor<HRACSymbol> {
	private HRACSymbol s;

	@Override
	public HRACSymbol visitCnt_specify(Cnt_specifyContext ctx) {
		if(ctx.INT()!=null) {
s.setBitCnt(Integer.parseInt(ctx.INT().getText()));			
		}else if(ctx.BI_N()!=null) {
			s.setBitCntSpecial(true);
		}
		return s;
	}

	@Override
	public HRACSymbol visitSymbol_dec(Symbol_decContext ctx) {
	s=new HRACSymbol();
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
