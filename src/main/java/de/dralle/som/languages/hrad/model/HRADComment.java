package de.dralle.som.languages.hrad.model;

import java.util.Objects;

import de.dralle.som.languages.hrad.HRADSourceLocation;

public class HRADComment extends AbstractHRADCommand {

	private String comment;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public HRADComment(HRADSourceLocation sourceLocation, String comment) {
		super(sourceLocation);
		this.comment = comment;
	}

	@Override
	public int hashCode() {
		return Objects.hash(comment);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HRADComment other = (HRADComment) obj;
		return Objects.equals(comment, other.comment);
	}

	@Override
	public String toString() {
		return "HRADComment [comment=" + comment + "]";
	}

}
