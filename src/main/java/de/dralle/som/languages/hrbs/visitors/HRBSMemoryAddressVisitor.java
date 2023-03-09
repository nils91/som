/**
 * 
 */
package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Custom_command_call_no_paramContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Instance_idContext;
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
				maov.setNxtOffset(i);HRBSGrammarParser.Offset_specifyContext os = ctx.offset_specify(i);
				os.accept(maov);
			}
		}
		return address;
	}

	@Override
	public HRBSMemoryAddress visitSymbol_target_nname(Symbol_target_nnameContext ctx) {
				address.setSymbol(new HRBSSymbol(ctx.NAME
						().getText()));
	if(ctx.custom_command_call_no_param()!=null) {
		ctx.custom_command_call_no_param().accept(this);
	}
		return address;
	}
	@Override
	public HRBSMemoryAddress visitCustom_command_call_no_param(Custom_command_call_no_paramContext ctx) {
		address.setTgtCmd(ctx.NAME().getText());
		if(ctx.instance_id()!=null) {
			ctx.instance_id().accept(this);
		}
		return address;
	}
	@Override
	public HRBSMemoryAddress visitInstance_id(Instance_idContext ctx) {
		address.setTgtCmdInst(ctx.NAME().getText());
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
