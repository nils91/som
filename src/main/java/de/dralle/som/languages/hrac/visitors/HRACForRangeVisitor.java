package de.dralle.som.languages.hrac.visitors;

import de.dralle.som.languages.hrac.generated.HRACGrammarBaseVisitor;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Offset_specify_rangeContext;
import de.dralle.som.languages.hrac.model.HRACForDupBoundingRangeProvider;
import de.dralle.som.languages.hrac.model.HRACMemoryOffset;
import de.dralle.som.languages.hrac.model.IHRACRangeProvider;

public class HRACForRangeVisitor extends HRACGrammarBaseVisitor<IHRACRangeProvider> {
	private HRACForDupBoundingRangeProvider r = new HRACForDupBoundingRangeProvider();;

	@Override
	public HRACForDupBoundingRangeProvider visitOffset_specify_range(Offset_specify_rangeContext ctx) {
		boolean rangeStartExclusive = false;
		if (ctx.children.get(0) == ctx.B_OPEN(0))// lower inclusive
		{
			rangeStartExclusive = false;
		} else {
			if (ctx.children.get(0) == ctx.B_CLOSE(0))// lower exclusive
			{
				rangeStartExclusive = true;
			}
		}
		boolean rangeEndExclusive;
		if (rangeStartExclusive) {
			if (ctx.B_CLOSE(1) != null) {
				rangeEndExclusive = false;
			} else {
				rangeEndExclusive = true;
			}
		} else {
			if (ctx.B_OPEN(1) != null) {
				rangeEndExclusive = true;
			} else {
				rangeEndExclusive = false;
			}
		}
		HRACMemoryOffset start = ctx.offset_specify_number(0).accept(new HRACOSVisitor());
		HRACMemoryOffset end = ctx.offset_specify_number(1).accept(new HRACOSVisitor());
		r.setRangeEndBoundExclusive(rangeEndExclusive);
		r.setRangeStartBoundExclusive(rangeStartExclusive);
		r.setRangeEnd(end.getOffset());
		r.setRangeEndSpecial(end.getDirectiveName());
		r.setRangeStart(start.getOffset());
		r.setRangeStartSpecial(start.getDirectiveName());
		return r;
	}

}
