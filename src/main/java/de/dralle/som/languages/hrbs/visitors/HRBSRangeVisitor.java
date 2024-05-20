package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Offset_specify_rangeContext;
import de.dralle.som.languages.hrbs.model.HRBSMemoryAddressOffset;
import de.dralle.som.languages.hrbs.model.HRBSBoundsRange;

public class HRBSRangeVisitor extends HRBSGrammarBaseVisitor<HRBSBoundsRange> {

	@Override
	public HRBSBoundsRange visitOffset_specify_range(Offset_specify_rangeContext ctx) {
	HRBSMemoryAddressOffset start = ctx.offset_specify_number(0).accept(new HRBSMemoryAddressOffsetSpecifyVisitor());
	HRBSMemoryAddressOffset end = ctx.offset_specify_number(1).accept(new HRBSMemoryAddressOffsetSpecifyVisitor());
	HRBSBoundsRange range = new HRBSBoundsRange();
	range.setStart(start);
	range.setEnd(end);
	return range;
	}

}
