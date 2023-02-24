/**
 * 
 */
package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Cmd_headContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Cmd_head_paramContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.CommandContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Command_defContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.CommandsContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.DirectivesContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Import_stmtContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Symbol_blkContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Symbol_definitionsContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Symbol_nsContext;
import de.dralle.som.languages.hrbs.model.HRBSModel;
import de.dralle.som.languages.hrbs.model.HRBSSymbol;
import de.dralle.som.languages.hrbs.model.HRBSSymbolType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dralle.som.languages.hras.generated.HRASGrammarBaseVisitor;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.DirectiveContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.LineContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.ProgramContext;
import de.dralle.som.languages.hras.generated.HRASGrammarParser.Symbol_decContext;
import de.dralle.som.languages.hras.model.Command;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hras.model.MemoryAddress;

/**
 * @author Nils
 *
 */
public class ProgramVisitor extends HRBSGrammarBaseVisitor<HRBSModel> {

	private HRBSModel model;

	public ProgramVisitor() {
		model = new HRBSModel();
	}

	public ProgramVisitor(HRBSModel model) {
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
		if (ctx.directives() != null) {
			ctx.directives().accept(this);
		}
		if (ctx.symbol_definitions() != null) {
			ctx.symbol_definitions().accept(this);
		}
		if (ctx.commands() != null) {
			ctx.commands().accept(this);
		}
		return model;
	}

	@Override
	public HRBSModel visitCommands(CommandsContext ctx) {
		for (CommandContext cc : ctx.command()) {
			model.addCommand(cc.accept(new HRBSCommandVisitor()));
		}
		return model;
	}

	@Override
	public HRBSModel visitSymbol_definitions(Symbol_definitionsContext ctx) {
		if (ctx.symbol_blk() != null) {
			for (Symbol_blkContext bloc : ctx.symbol_blk()) {
				bloc.accept(this);
			}
		}
		if (ctx.symbol_ns() != null) {
			for (Symbol_nsContext s : ctx.symbol_ns()) {
				model.addSymbol(s.accept(new HRBSSymbolVisitor()));
			}
		}
		return model;
	}

	@Override
	public HRBSModel visitSymbol_blk(Symbol_blkContext ctx) {
		HRBSSymbolType symbolType = HRBSSymbolType.local;
		if (ctx.LOCAL() != null) {
			symbolType = HRBSSymbolType.local;
		} else if (ctx.SHARED() != null) {
			symbolType = HRBSSymbolType.shared;
		} else if (ctx.GLOBAL() != null) {
			symbolType = HRBSSymbolType.global;
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
				HRBSModel lclModel = com.accept(new ProgramVisitor());
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
		}
		for (HRBSModel hrbsModel0 : childModels) {
			for (HRBSModel hrbsModel1 : childModels) {
				hrbsModel0.addChild(hrbsModel1);
			}
		}
		return model;
	}

	@Override
	public HRBSModel visitDirective(de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.DirectiveContext ctx) {
		if (ctx.HEAP() != null) {
			model.setHeapSize(Integer.parseInt(ctx.INT().getText()));
		} else if (ctx.D_N() != null) {
			model.setMinimumN(Integer.parseInt(ctx.INT().getText()));
		}
		return model;
	}

}
