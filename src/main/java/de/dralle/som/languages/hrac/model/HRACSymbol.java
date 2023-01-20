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
	/**
	 * Potential target symbol. Might be null.
	 */
	private HRACMemoryAddress targetSymbol;
	private int bitCnt;
	private boolean bitCntISN;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HRACMemoryAddress getTargetSymbol() {
		return targetSymbol;
	}
	public void setTargetSymbol(HRACMemoryAddress mirrorSymbol) {
		this.targetSymbol = mirrorSymbol;
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
	if(targetSymbol!=null) {
		sb.append(String.format(" %s", targetSymbol));
	}
	return sb.toString();
	}
}
