/**
 * 
 */
package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Import_stmtContext;
import de.dralle.som.languages.hrbs.model.HRBSModel;

import java.io.IOException;
import java.util.List;

import de.dralle.som.FileLoader;
import de.dralle.som.SOMFormats;
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
public class HRBSImportVisitor extends HRBSGrammarBaseVisitor<HRBSModel> {

	@Override
	public HRBSModel visitImport_stmt(Import_stmtContext ctx) {
		HRBSModel mtr=null;
		Object loadedModel=null;
		String using=null;
		String loadName=null;
		String reName=null;
		if(ctx.NAME()!=null&&ctx.DIRECTIVE_VALUE_STR()==null) {
			try {
				loadName = ctx.NAME(0).getText();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(ctx.NAME(1)!=null) {
				reName=ctx.NAME(1).getText();
			}
			if(ctx.NAME(2)!=null) {
				using=ctx.NAME(2).getText();
			}
			SOMFormats format = SOMFormats.HRBS;
			if(using!=null) {
				format=new FileLoader().getFormatFromName(using);
			}
			try {
				loadedModel=new FileLoader().loadByName(loadName, format);
			} catch (IOException e) {
			}
			if(format!=SOMFormats.HRBS) {
				mtr=new de.dralle.som.Compiler().compile(loadedModel, format, SOMFormats.HRBS);
			}else {
				mtr=(HRBSModel) loadedModel;
			}
			if(reName!=null&&mtr!=null) {
				mtr.setName(reName);
			}
		}
		else if(ctx.DIRECTIVE_VALUE_STR()!=null) {
			String path=ctx.DIRECTIVE_VALUE_STR().getText();
			String reName1=null;
			String using1=null;
			path=path.replaceAll("\"|'", "");			
			if(ctx.NAME(0)!=null) {
				reName1=ctx.NAME(0).getText();
			}
			if(ctx.NAME(1)!=null){
				using1=ctx.NAME(1).getText();
			}
			SOMFormats format = SOMFormats.HRBS;
			if(using1!=null) {
				format=new FileLoader().getFormatFromName(using1);
			}
			try {
				loadedModel=new FileLoader().loadFromFile(path,format);
			} catch (IOException e) {
			}
			if(format!=SOMFormats.HRBS) {
				mtr=new de.dralle.som.Compiler().compile(loadedModel, format, SOMFormats.HRBS);
			}else {
				mtr=(HRBSModel) loadedModel;
			}
			if(reName1!=null&&mtr!=null) {
				mtr.setName(reName1);
			}
		}
		return mtr;
	}



}
