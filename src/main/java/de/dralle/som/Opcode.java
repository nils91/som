/**
 * 
 */
package de.dralle.som;

/**
 * @author Nils Dralle
 *
 */
public enum Opcode {
	NAR("0"), NAW("1");
	private boolean bit;
	private Opcode (String bitcode){
		this.bit=!("0".equals(bitcode));
	}
	private Opcode (char bitcode){
		this.bit=!('0'==bitcode);
	}
	private Opcode (boolean bit){
		this.bit=bit;
	}
	public char getBitChar() {
		if(bit) {
			return '1';
		}else {
			return '0';
		}
	}
}
