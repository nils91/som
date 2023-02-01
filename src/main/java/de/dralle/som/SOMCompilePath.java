/**
 * 
 */
package de.dralle.som;

/**
 * @author Nils
 *
 */
public abstract class SOMCompilePath<SOURCE,TARGET> {
	
	private SOMFormats sourceFormat;
	private SOMFormats targetFormat;
	public SOMCompilePath(SOMFormats sourceFormat, SOMFormats targetFormat) {
		super();
		this.sourceFormat = sourceFormat;
		this.targetFormat = targetFormat;
	}
	public abstract TARGET compile(SOURCE sourceModel);}
