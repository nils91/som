package de.dralle.som.languages.hrbs.visitors;

import de.dralle.som.languages.hrac.model.HRACForDupBoundingRangeProvider;
import de.dralle.som.languages.hrac.model.HRACMemoryOffset;
import de.dralle.som.languages.hrac.visitors.HRACOSVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarBaseVisitor;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Directive_accessContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Offset_specify_numberContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Offset_specify_rangeContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Offset_specify_setContext;
import de.dralle.som.languages.hrbs.generated.HRBSGrammarParser.Offset_specify_valuesContext;
import de.dralle.som.languages.hrbs.model.HRBSMemoryAddressOffset;
import de.dralle.som.languages.hrbs.model.HRBSValueRange;
import de.dralle.som.languages.hrbs.model.AbstractHRBSRange;
import de.dralle.som.languages.hrbs.model.HRBSBoundsRange;

public class HRBSRangeVisitor extends HRBSGrammarBaseVisitor<AbstractHRBSRange> {
private AbstractHRBSRange range=null;
	@Override
	public AbstractHRBSRange visitOffset_specify_range(Offset_specify_rangeContext ctx) {
		HRBSBoundsRange r = new HRBSBoundsRange();
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
		boolean stepSpecified = ctx.SEMICOLON() != null;
		int cntVal = ctx.offset_specify_number().size();
		HRBSMemoryAddressOffset step = new HRBSMemoryAddressOffset(1);
		HRBSMemoryAddressOffset start = new HRBSMemoryAddressOffset(0);
		HRBSMemoryAddressOffset end = new HRBSMemoryAddressOffset(0);
		if (cntVal == 1) {
			if (stepSpecified) {
				step = ctx.offset_specify_number(0).accept(new HRBSMemoryAddressOffsetSpecifyVisitor());
			} else {
				// step not specified, find ofs idx
				boolean rangeStartSpecified = ctx.getChild(1) == ctx.offset_specify_number(0);
				if (rangeStartSpecified) {
					start = ctx.offset_specify_number(0).accept(new HRBSMemoryAddressOffsetSpecifyVisitor());
				} else {
					end = ctx.offset_specify_number(1).accept(new HRBSMemoryAddressOffsetSpecifyVisitor());
				}
			}
		}
		if (cntVal == 2) {
			if (!stepSpecified) {
				// start to end, no step
				start = ctx.offset_specify_number(0).accept(new HRBSMemoryAddressOffsetSpecifyVisitor());
				end = ctx.offset_specify_number(1).accept(new HRBSMemoryAddressOffsetSpecifyVisitor());
			} else {
				boolean rangeStartSpecified = ctx.getChild(1) == ctx.offset_specify_number(0);
				if (rangeStartSpecified) {
					start = ctx.offset_specify_number(0).accept(new HRBSMemoryAddressOffsetSpecifyVisitor());
				} else {
					step = ctx.offset_specify_number(1).accept(new HRBSMemoryAddressOffsetSpecifyVisitor());
				}
			}
		}
		if (cntVal == 3) {
			// all specified
			start = ctx.offset_specify_number(0).accept(new HRBSMemoryAddressOffsetSpecifyVisitor());
			end = ctx.offset_specify_number(1).accept(new HRBSMemoryAddressOffsetSpecifyVisitor());
			step = ctx.offset_specify_number(2).accept(new HRBSMemoryAddressOffsetSpecifyVisitor());
		}
		r.setEndBoundExclusive(rangeEndExclusive);
		r.setStartBoundExclusive(rangeStartExclusive);
		r.setEnd(end);
		r.setStart(start);
		r.setStep(step);
		range=r;
		return r;
	}

	@Override
	public AbstractHRBSRange visitDirective_access(Directive_accessContext ctx) {
		range.setRunningDirectiveName(ctx.directive_name().getText());
		return range;}

	@Override
	public AbstractHRBSRange visitOffset_specify_values(Offset_specify_valuesContext ctx) {
		if (ctx.offset_specify_range() != null) {
			ctx.offset_specify_range().accept(this);
		}
		if(ctx.offset_specify_set()!=null) {
			range=ctx.offset_specify_set().accept(this);
			
		}
		if(ctx.directive_access()!=null) {
			ctx.directive_access().accept(this);
		}
		return range;
	}

	@Override
	public AbstractHRBSRange visitOffset_specify_set(Offset_specify_setContext ctx) {
		HRBSValueRange r = new HRBSValueRange();
		for (Offset_specify_numberContext iterable_element : ctx.offset_specify_number()) {
			r.addValue(iterable_element.accept(new HRBSMemoryAddressOffsetSpecifyVisitor()));
		}
		return r;
	}

}
