/**
 * 
 */
package de.dralle.som.languages.hrac.model;

/**
 * @author Nils
 *
 */
public class HRACSymbol implements Cloneable{
	@Override
	public HRACSymbol clone() {
		HRACSymbol clone;
		try {
			clone = (HRACSymbol) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			clone=new HRACSymbol();
		}
		if(targetSymbol!=null) {
			clone.targetSymbol=targetSymbol.clone();
		}
		return clone;
	}
	private String name;
	/**
	 * Potential target symbol. Might be null.
	 */
	private HRACMemoryAddress targetSymbol;
	private int bitCnt;
	private boolean bitCntSpecial;
	/**
	 * If bitCnt is special, contains the special (directive) name.
	 */
	private String specialName;
	public void setSpecialName(String specialName) {
		this.specialName = specialName;
	}
	public HRACSymbol(String generateHRACSymbolName) {
		name=generateHRACSymbolName;
	}
	public HRACSymbol() {
		// TODO Auto-generated constructor stub
	}
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
	public boolean isBitCntSpecial() {
		return bitCntSpecial;
	}
	public void setBitCntSpecial(boolean bitCntISN) {
		this.bitCntSpecial = bitCntISN;
	}
	public String asCode() {
	StringBuilder sb=new StringBuilder();
	if(targetSymbol!=null) {
		sb.append("symbol ");
	}else {
		sb.append("alloc ");
	}
	sb.append(name);
	if(bitCntSpecial) {
		sb.append(String.format("[;%s]", specialName));
	}else {
		sb.append(String.format("[%d]", bitCnt));
	}
	if(targetSymbol!=null) {
		sb.append(String.format(" %s", targetSymbol));
	}
	return sb.toString();
	}
	public String getSpecialName() {
		return specialName;
	}
}
