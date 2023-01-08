/**
 * 
 */
package de.dralle.som;

/**
 * @author Nils Dralle
 *
 */
public class ByteArrayMemspace extends AbstractSomMemspace {

	private byte[] memory;
	
	/**
	 * Creates a new memory space. Note that will not initialize the memspace and for example set the bits for N in the memspace.
	 * @param size Size of the new memory space in bits.
	 */
	public ByteArrayMemspace(int size) {
		int sizeBytes = size/8;
		if(size%8!=0) {
			sizeBytes++;
		}
		memory=new byte[sizeBytes];
	}
	
	/**
	 * Creates a new memory space from a given byte array.vNote that this does not check if N and the size of the byte array match up.
	 */
	public ByteArrayMemspace(byte[] memory) {
		this.memory=memory;
	}
	/**
	 * Creates a new memory space. Note that will not initialize the memspace and for example set the bits for N in the memspace.
	 * @param size Size of the new memory space in bits.
	 */
	public ByteArrayMemspace() {
		memory=new byte[0];
	}
	
	@Override
	public void setBit(int address, boolean bitValue) {
		int byteAddress = address / 8;
		int offset = 7 - address % 8;
		byte bite = memory[byteAddress];
		byte bitmask = (byte) (1 << offset);
		if (!bitValue) {
			bitmask = (byte) ~bitmask;
			bite = (byte) (bite & bitmask);
		} else {
			bite = (byte) (bite | bitmask);
		}
		memory[byteAddress] = bite;
	}

	@Override
	public boolean getBit(int address) {
		int byteAddress = address / 8;
		int offset = 7 - address % 8;
		byte bite = memory[byteAddress];
		byte bitmask = (byte) (1 << offset);
		boolean bitValue = (bite & bitmask) != 0;
		return bitValue;
	}

	@Override
	public int getSize() {
		return memory.length*8;
	}

	@Override
	public void copy(IMemspace from) {
		if(from instanceof ByteArrayMemspace) {
			ByteArrayMemspace fromByteArrayMemspace = (ByteArrayMemspace)from;
			for (int i = 0; i < memory.length; i++) {
				if(i*8<fromByteArrayMemspace.getSize()) {
					memory[i]=fromByteArrayMemspace.memory[i];
				}
			}
		}else {
			super.copy(from);
		}		
	}

	@Override
	public void resize(int newSize, boolean copyContent) {
		ByteArrayMemspace newmemSpace = new ByteArrayMemspace(newSize);
		if(copyContent) {
			newmemSpace.copy(this);
		}
		memory=newmemSpace.memory;
	}

	@Override
	public ByteArrayMemspace clone() {
		ByteArrayMemspace newmemSpace = new ByteArrayMemspace(getSize());
		newmemSpace.copy(this);
		return newmemSpace;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ByteArrayMemspace) {
			ByteArrayMemspace cTo = (ByteArrayMemspace)obj;
			return equalContent(cTo);
		}
		return false;
	}
	
	public byte[] getUnderlyingByteArray() {
		return memory;
	}
}
