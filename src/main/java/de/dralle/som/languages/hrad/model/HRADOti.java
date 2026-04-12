package de.dralle.som.languages.hrad.model;

import java.util.Objects;

import de.dralle.som.languages.hrad.HRADSourceLocation;

public class HRADOti extends AbstractHRADCommand{
	private boolean set;
	public HRADOti(boolean set, int address,HRADSourceLocation sourceLocation) {
		super(sourceLocation);
		this.set = set;
		this.address = address;
	}
	@Override
	public int hashCode() {
		return Objects.hash(address, set);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HRADOti other = (HRADOti) obj;
		return address == other.address && set == other.set;
	}
	private int address;
	public boolean isSet() {
		return set;
	}
	public void setSet(boolean set) {
		this.set = set;
	}
	public int getAddress() {
		return address;
	}
	public void setAddress(int address) {
		this.address = address;
	}
}
