package de.dralle.som.languages.hrac.model;

public class HRACMemoryOffset {
private int offset;
private String directiveName;
public HRACMemoryOffset() {
	
}
public HRACMemoryOffset(int ofs) {
	offset=ofs;
}
public int getOffset() {
	return offset;
}
public void setOffset(int offset) {
	this.offset = offset;
}
public String getDirectiveName() {
	return directiveName;
}
public void setDirectiveName(String directiveName) {
	this.directiveName = directiveName;
}
}
