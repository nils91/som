/**
 * 
 */
package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Commad_labelContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Custom_command_call_no_paramContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Directive_accessContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Instance_idContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Target_argumentContext;
import de.dralle.som.languages.hrbs.model.HRBSCommand;
import de.dralle.som.languages.hrbs.model.HRBSSymbolType;

/**
 * @author Nils
 *
 */
public class HRBSCommandVisitor extends HRBSGrammarBaseVisitor<HRBSCommand> {
	private HRBSCommand c;

	@Override
	public HRBSCommand visitCommand(de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.CommandContext ctx) {
		c = new HRBSCommand();
		if(ctx.commad_label()!=null) {
			ctx.commad_label().accept(this);
		}
		if(ctx.offset_specify_values()!=null) {
		c.setRange(	ctx.offset_specify_values().accept(new HRBSRangeVisitor()));
		}
		if (ctx.custom_command_call_no_param()!=null) {
			ctx.custom_command_call_no_param().accept(this);
		}
		
		if (ctx.target_argument() != null) {
			for (Target_argumentContext tac : ctx.target_argument()) {
				c.addTarget(tac.accept(new HRBSMemoryAddressVisitor()));
			}
		}
		return c;
	}

	@Override
	public HRBSCommand visitCustom_command_call_no_param(Custom_command_call_no_paramContext ctx) {
		if(ctx.NAME()!=null) {
			c.setCmd(ctx.NAME().getText());
		}
		if(ctx.instance_id()!=null) {
			ctx.instance_id().accept(this);
		}
		return c;
	}

	@Override
	public HRBSCommand visitInstance_id(Instance_idContext ctx) {
		if(ctx.NAME()!=null) {
			c.setInstIdDirective(false);
			c.setCllInstId(ctx.NAME().getText());
		}else if(ctx.directive_access()!=null) {
			c.setInstIdDirective(true);
ctx.directive_access().accept(this);
		}
		return c;
	}

	@Override
	public HRBSCommand visitDirective_access(Directive_accessContext ctx) {
		c.setCllInstId(ctx.directive_name().getText());
		return c;
	}

	@Override
	public HRBSCommand visitCommad_label(Commad_labelContext ctx) {
		if (ctx.NAME()!=null) {
			c.setLabel(ctx.NAME().getText());
		}
		HRBSSymbolType labelType = HRBSSymbolType.local;
		if(ctx.def_scope()!=null) {
			labelType=ctx.def_scope().accept(new HBRSSymbolTypeVisitor());
		}
		c.setLabelType(labelType);
		return c;
	}

}
