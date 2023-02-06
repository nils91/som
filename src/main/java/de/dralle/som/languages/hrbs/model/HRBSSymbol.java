/**
 * 
 */
package de.dralle.som.languages.hrbs.model;

/**
 * @author Nils
 *
 */
public class HRBSSymbol {
	private String name;
	/**
	 * Potential target symbol. Might be null.
	 */
	private HRBSMemoryAddress targetSymbol;
	private HRBSSymbolType type;
	private int bitCnt;
	private boolean bitCntISN;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HRBSMemoryAddress getTargetSymbol() {
		return targetSymbol;
	}
	public void setTargetSymbol(HRBSMemoryAddress mirrorSymbol) {
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
	if(type!=null) {
		sb.append(type+" ");
	}
	sb.append(name);
	if(bitCntISN) {
		sb.append("[N]");
	}else {
		sb.append(String.format("[%d]", bitCnt));
	}
	if(targetSymbol!=null) {
		sb.append(String.format(" %s", targetSymbol.asHRBSCode()));
	}
	return sb.toString();
	}
	public HRBSSymbolType getType() {
		return type;
	}
	public void setType(HRBSSymbolType type) {
		this.type = type;
	}
}
