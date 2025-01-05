package de.dralle.som.languages.hrac.model;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACIntegerNode;

public class HRACMemoryOffset {
private HRACAbstractExpressionNode offset;
public void setOffset(HRACAbstractExpressionNode offset) {
	this.offset = offset;
}
public HRACMemoryOffset() {
	
}
public HRACMemoryOffset(int ofs) {
	offset=new HRACIntegerNode(ofs);
}
public HRACAbstractExpressionNode getOffset() {
	return offset;
}
public void setOffset(int offset) {
	this.offset = new HRACIntegerNode(offset);
}
}
