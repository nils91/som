/**
 * 
 */
package de.dralle.som.languages.hrbs.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nils
 *
 */
public class HBRSSymbolTypeExtended implements IHBRSSymbolType{
	private Set<String> sharedWith;
	private HBRSSymbolTypeExtended(Collection<String> sharedWith) {
		if(sharedWith!=null) {
			this.sharedWith=new HashSet<>(sharedWith);
		}
		type=HRBSSymbolType.shared;
	}
	private HBRSSymbolTypeExtended(String... sharedWith) {
		if(sharedWith!=null) {
			this.sharedWith=new HashSet<>(Arrays.asList(sharedWith));
		}
		type=HRBSSymbolType.shared;
	}
	
	private HRBSSymbolType type;
	@Override
	public HRBSSymbolType getType() {
		return type;
	}
	public static HRBSSymbolType getLocalType() {
		return HRBSSymbolType.local;
	}
	public static HRBSSymbolType getGlobalType() {
		return HRBSSymbolType.global;
	}public static HRBSSymbolType getSharedType() {
		return HRBSSymbolType.shared;
	}
	@Override
	public String toString() {
		String s = type.toString();
		if(sharedWith!=null) {
			s+="(";
			for (String string : sharedWith) {
				s+=string+",";
			}
			s=s.substring(0, s.length()-1);
			s+=")";
		}
		return s;
	}
	public static HBRSSymbolTypeExtended getSharedType(String... sharedWith) {
		return new HBRSSymbolTypeExtended(sharedWith);
	}
	public static HBRSSymbolTypeExtended getSharedType(Collection<String> sharedWith) {
		return new HBRSSymbolTypeExtended(sharedWith);
	}
	public Set<String> getSharedWith() {
		return sharedWith;
	}
}
