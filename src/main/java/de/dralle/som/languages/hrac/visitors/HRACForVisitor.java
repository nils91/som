package de.dralle.som.languages.hrac.visitors;

import de.dralle.som.languages.hrac.generated.HRACGrammarBaseVisitor;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Commadn_or_forContext;
import de.dralle.som.languages.hrac.model.HRACCommand;
import de.dralle.som.languages.hrac.model.HRACForDup;

public class HRACForVisitor extends HRACGrammarBaseVisitor<HRACForDup>{
	private HRACForDup f=null;
	public HRACForVisitor(HRACForDup f) {
		this.f=f;
	}
	public HRACForVisitor() {
		this(new HRACForDup());
	}
	@Override
	public HRACForDup visitCommadn_or_for(Commadn_or_forContext ctx) {
		// TODO Auto-generated method stub
		return super.visitCommadn_or_for(ctx);
	}
}
