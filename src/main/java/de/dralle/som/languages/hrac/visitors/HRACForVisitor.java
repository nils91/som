package de.dralle.som.languages.hrac.visitors;

import de.dralle.som.languages.hrac.generated.HRACGrammarBaseVisitor;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Commadn_or_forContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.For_duplicationContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.For_duplication_headContext;
import de.dralle.som.languages.hrac.model.HRACForDup;
import de.dralle.som.languages.hrac.model.HRACForDupBoundingRangeProvider;

public class HRACForVisitor extends HRACGrammarBaseVisitor<HRACForDup> {
	private HRACForDup f = null;

	public HRACForVisitor(HRACForDup f) {
		this.f = f;
	}

	public HRACForVisitor() {
		this(new HRACForDup());
	}

	@Override
	public HRACForDup visitCommadn_or_for(Commadn_or_forContext ctx) {
		if (ctx.command() != null) {
			f.setCmd(ctx.command().accept(new HRACCommandVisitor()));
		} else if (ctx.for_duplication() != null) {
			ctx.for_duplication().accept(this);
		}
		return f;
	}

	@Override
	public HRACForDup visitFor_duplication(For_duplicationContext ctx) {
		ctx.for_duplication_head().accept(this);
		f.setModel(ctx.program_blk().accept(new HRACProgramVisitor()));
		return f;
	}

	@Override
	public HRACForDup visitFor_duplication_head(For_duplication_headContext ctx) {
		f.setRange(ctx.offset_specify_values().accept(new HRACForRangeVisitor()));
		return f;
	}
}
