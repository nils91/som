package de.dralle.som.languages.hrac.visitors;

import de.dralle.som.languages.hrac.generated.HRACGrammarBaseVisitor;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Directive_accessContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Offset_specify_numberContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Offset_specify_rangeContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Offset_specify_setContext;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser.Offset_specify_valuesContext;
import de.dralle.som.languages.hrac.model.HRACForDupBoundingRangeProvider;
import de.dralle.som.languages.hrac.model.HRACForDupFixedRangeProvider;
import de.dralle.som.languages.hrac.model.HRACMemoryOffset;
import de.dralle.som.languages.hrac.model.IHRACRangeProvider;

public class HRACForRangeVisitor extends HRACGrammarBaseVisitor<IHRACRangeProvider> {
	private IHRACRangeProvider r =null;

	@Override
	public HRACForDupBoundingRangeProvider visitOffset_specify_range(Offset_specify_rangeContext ctx) {
		HRACForDupBoundingRangeProvider r=new HRACForDupBoundingRangeProvider();
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
		boolean stepSpecified=ctx.SEMICOLON()!=null;
		int cntVal=ctx.offset_specify_number().size();
		HRACMemoryOffset step = new HRACMemoryOffset(1);
		HRACMemoryOffset start=new HRACMemoryOffset(0);
		HRACMemoryOffset end=new HRACMemoryOffset(0);
		if(cntVal==1) {
			if(stepSpecified) {
				step=ctx.offset_specify_number(0).accept(new HRACOSVisitor());
			}else {
				//step not specified, find ofs idx
				boolean rangeStartSpecified=ctx.getChild(1)==ctx.offset_specify_number(0);
				if(rangeStartSpecified) {
					start = ctx.offset_specify_number(0).accept(new HRACOSVisitor());
				}else {
					end = ctx.offset_specify_number(1).accept(new HRACOSVisitor());
				}
			}
		}
		if(cntVal==2) {
			if(!stepSpecified) {
				//start to end, no step
				start = ctx.offset_specify_number(0).accept(new HRACOSVisitor());
				end = ctx.offset_specify_number(1).accept(new HRACOSVisitor());
			}else {
				boolean rangeStartSpecified=ctx.getChild(1)==ctx.offset_specify_number(0);
				if(rangeStartSpecified) {
					start = ctx.offset_specify_number(0).accept(new HRACOSVisitor());
				}else {
					step = ctx.offset_specify_number(1).accept(new HRACOSVisitor());
				}
			}
		}
		if(cntVal==3) {
			//all specified
			start = ctx.offset_specify_number(0).accept(new HRACOSVisitor());
			end = ctx.offset_specify_number(1).accept(new HRACOSVisitor());
			step = ctx.offset_specify_number(2).accept(new HRACOSVisitor());
		}
		r.setRangeEndBoundExclusive(rangeEndExclusive);
		r.setRangeStartBoundExclusive(rangeStartExclusive);
		r.setRangeEnd(end.getOffset());
		r.setRangeEndSpecial(end.getDirectiveName());
		r.setRangeStart(start.getOffset());
		r.setRangeStartSpecial(start.getDirectiveName());
		r.setStepSize(step.getOffset());
		r.setStepSizeSpecial(step.getDirectiveName());
		return r;
	}

	@Override
	public IHRACRangeProvider visitOffset_specify_values(Offset_specify_valuesContext ctx) {
		if(ctx.offset_specify_range()!=null) {
			r=ctx.offset_specify_range().accept(this);
		}
		if(ctx.offset_specify_set()!=null) {
			r=ctx.offset_specify_set().accept(this);
		}
		if(ctx.directive_access()!=null) {
			ctx.directive_access().accept(this);
		}
		return r;
	}

	@Override
	public IHRACRangeProvider visitDirective_access(Directive_accessContext ctx) {
		r.setRunningDirectiveName(ctx.getText());
		return r;
	}

	

	@Override
	public IHRACRangeProvider visitOffset_specify_set(Offset_specify_setContext ctx) {
		HRACForDupFixedRangeProvider rl = new HRACForDupFixedRangeProvider();
		
		for (Offset_specify_numberContext iterable_element : ctx.offset_specify_number()) {
			HRACMemoryOffset ofs = iterable_element.accept(new HRACOSVisitor());
			if(ofs.getDirectiveName()!=null) {
				rl.addReplacingDirective(ofs.getDirectiveName());
			}else {
				rl.addValue(ofs.getOffset());
			}
		}
		return rl;
	}

}
