/**
 * 
 */
package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Cmd_headContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Cmd_head_paramContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.CommandContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Command_defContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.DirectivesContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Import_stmtContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.OtiContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Symbol_blkContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Symbol_definitionContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Symbol_nsContext;
import de.dralle.som.languages.hrbs.model.HRBSModel;
import de.dralle.som.languages.hrbs.model.HRBSSymbol;
import de.dralle.som.languages.hrbs.model.HRBSSymbolType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.dralle.som.Util;
import de.dralle.som.languages.hras.generated.HRASGrammarBaseVisitor;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.DirectiveContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.LineContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.ProgramContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Symbol_decContext;
import de.dralle.som.languages.hras.model.HRASCommand;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hras.model.SymbolHRASMemoryAddress;

/**
 * @author Nils
 *
 */
public class HRBSProgramVisitor extends HRBSGrammarBaseVisitor<HRBSModel> {

	@Override
	public HRBSModel visitSymbol_definition(Symbol_definitionContext ctx) {
		if (ctx.symbol_blk() != null) {
				ctx.symbol_blk().accept(this);
		}
		if (ctx.symbol_ns() != null) {
				model.addSymbol(ctx.symbol_ns().accept(new HRBSSymbolVisitor()));
		}
		return model;
	}

	private HRBSModel model;

	public HRBSProgramVisitor() {
		model = new HRBSModel();
	}

	public HRBSProgramVisitor(HRBSModel model) {
		this.model = model;
	}

	@Override
	public HRBSModel visitCmd_head(Cmd_headContext ctx) {
		model.setName(ctx.NAME().getText());
		for (Cmd_head_paramContext chp : ctx.cmd_head_param()) {
			chp.accept(this);
		}
		return model;
	}

	@Override
	public HRBSModel visitCmd_head_param(Cmd_head_paramContext ctx) {
		model.addParam(ctx.NAME().getText());
		return model;
	}

	@Override
	public HRBSModel visitCommand_def(Command_defContext ctx) {
		ctx.cmd_head().accept(this);
		if (ctx.directive() != null) {
			for (de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.DirectiveContext iterable_element : ctx.directive()) {
				iterable_element.accept(this);
			}			
		}
		if (ctx.symbol_definition() != null) {for (Symbol_definitionContext iterable_element : ctx.symbol_definition()) {
			iterable_element.accept(this);			
		}
		}
		if (ctx.command() != null) {
		for (CommandContext iterable_element : ctx.command()) {
			iterable_element.accept(this);			
		}
		}
		if(ctx.oti()!=null) {
			for (OtiContext iterable_element : 	ctx.oti()) {
				iterable_element.accept(this);	
			}
		}
		return model;
	}


	@Override
	public HRBSModel visitOti(OtiContext ctx) {
		if(ctx.OTI_SET()!=null) {
			model.addInitOnceItem(ctx.target_argument().accept(new HRBSMemoryAddressVisitor()), true);
		}else if(ctx.OTI_CLEAR()!=null) {
			model.addInitOnceItem(ctx.target_argument().accept(new HRBSMemoryAddressVisitor()), false);
		}
		return model;
	}

	@Override
	public HRBSModel visitSymbol_blk(Symbol_blkContext ctx) {
		HRBSSymbolType symbolType = HRBSSymbolType.local;
	if(ctx.def_scope()!=null) {
		symbolType=ctx.def_scope().accept(new HBRSSymbolTypeVisitor());
	}
		for (de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Symbol_decContext sd : ctx.symbol_dec()) {
			HRBSSymbol symbol = sd.accept(new HRBSSymbolVisitor(symbolType));
			model.addSymbol(symbol);
		}
		return model;
	}

	@Override
	public HRBSModel visitDirectives(DirectivesContext ctx) {
		for (de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.DirectiveContext dc : ctx.directive()) {
			dc.accept(this);
		}
		return model;
	}

	@Override
	public HRBSModel visitProgram(de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.ProgramContext ctx) {
		List<HRBSModel> childModels = new ArrayList<HRBSModel>();
		List<HRBSModel> importedModels = new ArrayList<HRBSModel>();
		if (ctx.import_stmt() != null) {
			for (Import_stmtContext imp : ctx.import_stmt()) {
				HRBSModel imported = imp.accept(new HRBSImportVisitor());
				importedModels.add(imported);
			}
		}
		if (ctx.command_def() != null) {
			for (Command_defContext com : ctx.command_def()) {
				HRBSModel lclModel = com.accept(new HRBSProgramVisitor());
				if ("MAIN".equalsIgnoreCase(lclModel.getName())) {
					model = lclModel;
				} else {
					childModels.add(lclModel);
				}
			}
		}
		if ((model.getName() == null || model.getName().trim().equals("")) && childModels.size() == 1) {
			// if no MAIN model in file and only one model, return that
			model = childModels.get(0);
		} else {
			for (HRBSModel hrbsModel : childModels) {
				model.addChild(hrbsModel);
			}
		}
		for (HRBSModel hrbsModel : importedModels) {
			model.addChild(hrbsModel);
			for (HRBSModel hrbsModel0 : childModels) {//add all imported to all child				
					hrbsModel0.addChild(hrbsModel);
				}
			
		}
		for (HRBSModel hrbsModel0 : childModels) {
			for (HRBSModel hrbsModel1 : childModels) {
				hrbsModel0.addChild(hrbsModel1);
			}
		}
		return model;
	}

	@Override
	public HRBSModel visitDirective(HRBSGrammarParser.DirectiveContext ctx) {
		if (ctx.directive_name() != null) {
			String name=ctx.directive_name().getText();
			String value=null;
			if(ctx.primary_expr()!=null) {
				value=ctx.primary_expr().accept(new HRBSExpressionVisitor()).compileToHRAC().calculateNumericalValue()+"";
			}
			if(ctx.DIRECTIVE_VALUE_STR()!=null) {
				value=ctx.DIRECTIVE_VALUE_STR().getText().substring(1, ctx.DIRECTIVE_VALUE_STR().getText().length()-1);
			}
			model.addDirective(name,value);
		} 
		return model;
	}

}
