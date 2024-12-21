package de.dralle.som.languages.hrac.model;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACIntegerNode;

public class HRACMemoryOffset {
private HRACAbstractExpressionNode offset;
public void setOffset(HRACAbstractExpressionNode offset) {
	this.offset = offset;
}
/**
 * This functionality is now handled by offset - can be directive node
 */
@Deprecated
private String directiveName;
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
@Deprecated
public String getDirectiveName() {
	return directiveName;
}
@Deprecated
public void setDirectiveName(String directiveName) {
	this.directiveName = directiveName;
}
}
