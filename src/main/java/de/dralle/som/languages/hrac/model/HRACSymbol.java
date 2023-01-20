/**
 * 
 */
package de.dralle.som.languages.hrac.model;

/**
 * @author Nils
 *
 */
public class HRACSymbol {
	private String name;
	private String mirrorSymbol;
	private int bitCnt;
	private boolean bitCntISN;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMirrorSymbol() {
		return mirrorSymbol;
	}
	public void setMirrorSymbol(String mirrorSymbol) {
		this.mirrorSymbol = mirrorSymbol;
	}
	public int getBitCnt() {
		return bitCnt;
	}
	public void setBitCnt(int bitCnt) {
		this.bitCnt = bitCnt;
	}
	public boolean isBitCntISN() {
		return bitCntISN;
	}
	public void setBitCntISN(boolean bitCntISN) {
		this.bitCntISN = bitCntISN;
	}
	public String asCode() {
	StringBuilder sb=new StringBuilder();
	sb.append(name);
	if(bitCntISN) {
		sb.append("[N]");
	}else {
		sb.append(String.format("[%d]", bitCnt));
	}
	if(mirrorSymbol!=null) {
		sb.append(String.format(" %s", mirrorSymbol));
	}
	return sb.toString();
	}
}
