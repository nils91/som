package de.dralle.som.languages.hrad.model;

import de.dralle.som.languages.hrad.HRADSourceLocation;

public class AbstractHRADCommand implements Cloneable {
	private HRADSourceLocation sourceLocation;
	public AbstractHRADCommand(HRADSourceLocation sourceLocation) {
		super();
		this.sourceLocation = sourceLocation;
	}
	@Override
	public AbstractHRADCommand clone(){
		// TODO Auto-generated method stub
		try {
			return (AbstractHRADCommand) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public HRADSourceLocation getSourceLocation() {
		return sourceLocation;
	}
	public void setSourceLocation(HRADSourceLocation sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

}
