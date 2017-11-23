/**
 * MX29L3211 chip informations
 * 
 * @author mingzo@gmail.com
 * @since 2017/11/11
 */
package org.arc.megaburner.chips;

public class MX29L3211 extends Chip {

	private String id = "C2F9";
	
	private String name = "MX29L3211";
	
	private String displayName = "   MX29L2311 (C2F9)";
	
	private String type = "3.3v/32MBit/4MiB";
	
	private int capacity = 4194304;
	
	private int pageSize = 128;
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public String getType() {
		return type;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getPageSize() {
		return pageSize;
	}
	
	public int getReadBlockCount() {
		int blockCount = capacity / readBlock;
		return blockCount;
	}
}
