package de.dralle.som.languages.hrac.visitors;

import de.dralle.som.languages.hrac.generated.HRACGrammarBaseVisitor;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Offset_specify_rangeContext;
import de.dralle.som.languages.hrac.model.HRACForDupBoundingRangeProvider;
import de.dralle.som.languages.hrac.model.HRACMemoryOffset;

public class HRACForRangeVisitor extends HRACGrammarBaseVisitor<HRACForDupBoundingRangeProvider> {
	private HRACForDupBoundingRangeProvider r = new HRACForDupBoundingRangeProvider();;

	@Override
	public HRACForDupBoundingRangeProvider visitOffset_specify_range(Offset_specify_rangeContext ctx) {
		HRACMemoryOffset start = ctx.offset_specify_number(0).accept(new HRACOSVisitor());
		HRACMemoryOffset end = ctx.offset_specify_number(1).accept(new HRACOSVisitor());
		r.setRangeEnd(end.getOffset());
		r.setRangeEndSpecial(end.getDirectiveName());
		r.setRangeStart(start.getOffset());
		r.setRangeStartSpecial(start.getDirectiveName());
		return r;
	}

}
