/**
 * 
 */
package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Custom_command_call_no_paramContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Directive_accessContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Fixed_addressContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Instance_idContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Symbol_targetContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Symbol_target_nameContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Target_argumentContext;
import de.dralle.som.languages.hrbs.model.AbstractHRBSMemoryAddress;
import de.dralle.som.languages.hrbs.model.HRBSMemoryAddressOffset;
import de.dralle.som.languages.hrbs.model.HRBSSymbol;
import de.dralle.som.languages.hras.generated.HRASGrammarBaseVisitor;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Int_or_symbolContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Offset_specifyContext;
import de.dralle.som.languages.hras.model.HRASMemoryAddress;

/**
 * @author Nils
 *
 */
public class HRBSMemoryAddressVisitor extends HRBSGrammarBaseVisitor<AbstractHRBSMemoryAddress> {

	@Override
	public AbstractHRBSMemoryAddress visitSymbol_target(Symbol_targetContext ctx) {
		if (ctx.AMP() != null) {
			address.setDeref(true);
		}
		if(ctx.symbol_target_name()!=null) {
			ctx.symbol_target_name().accept(this);
		}
		return address;
	}

	@Override
	public AbstractHRBSMemoryAddress visitFixed_address(Fixed_addressContext ctx) {
		// TODO Auto-generated method stub
		return super.visitFixed_address(ctx);
	}

	@Override
	public AbstractHRBSMemoryAddress visitTarget_argument(Target_argumentContext ctx) {
		if(ctx.symbol_target()!=null) {
			ctx.symbol_target().accept(this);
		}
		if(ctx.fixed_address()!=null) {
			ctx.fixed_address().accept(this);
		}
		if (ctx.offset_specify() != null) {
			for (int i = 0; i < ctx.offset_specify().size(); i++) {
				HRBSGrammarParser.Offset_specifyContext os = ctx.offset_specify(i);
				HRBSMemoryAddressOffset ofs = os.accept(new HRBSMemoryAddressOffsetSpecifyVisitor());
				if (i == 0) {
					address.setOffset(ofs);
				} else {
					address.setDerefOffset(ofs);
				}
			}
		}
		return address;
	}

	private AbstractHRBSMemoryAddress address;

	public HRBSMemoryAddressVisitor() {
		address = new AbstractHRBSMemoryAddress();
	}

	@Override
	public AbstractHRBSMemoryAddress visitSymbol_target_name(Symbol_target_nameContext ctx) {		
		address.setSymbol(new HRBSSymbol(ctx.NAME().getText()));
		if (ctx.custom_command_call_no_param() != null) {
			ctx.custom_command_call_no_param().accept(this);
		}
		return address;
	}

	@Override
	public AbstractHRBSMemoryAddress visitCustom_command_call_no_param(Custom_command_call_no_paramContext ctx) {
		address.setTgtCmd(ctx.NAME().getText());
		if (ctx.instance_id() != null) {
			ctx.instance_id().accept(this);
		}
		return address;
	}

	@Override
	public AbstractHRBSMemoryAddress visitInstance_id(Instance_idContext ctx) {
		if (ctx.NAME() != null) {
			address.setTgtCmdInst(ctx.NAME().getText());
			address.setTgtCmdInstIsDirective(false);
		} else if (ctx.directive_access() != null) {
			address.setTgtCmdInstIsDirective(true);
			ctx.directive_access().accept(this);
		}
		return address;
	}

	@Override
	public AbstractHRBSMemoryAddress visitDirective_access(Directive_accessContext ctx) {
		address.setTgtCmdInst(ctx.directive_name().getText());
		address.setTgtCmdInstIsDirective(true);
		return address;
	}

	@Override
	public AbstractHRBSMemoryAddress visitOffset_specify(
			de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Offset_specifyContext ctx) {
		if (ctx.getChild(1) != null) {
			address.setOffset(Integer.parseInt(ctx.getChild(1).getText()));
		}
		return address;
	}

}
