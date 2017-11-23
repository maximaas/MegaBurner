/**
 * EEPROM chip definitions
 * @author mingzo@gmail.com
 * @since 2017/11/9
 */
package org.arc.megaburner;

import org.arc.megaburner.chips.Chip;
import org.arc.megaburner.chips.MX29L3211;

public class Chips {

	public final static String STATUS_NO_CHIP = "No Chip";
	public final static String STATUS_MISMATCHED = "Mismatch";
	public final static String STATUS_MATCHED = "In place";
	
	public static Chip[] chips;
	
	static {
		chips = new Chip[1];
		
		Chip mx29l3211 = new MX29L3211();
		
		chips[0] = mx29l3211;
	}
	
	private static int selected = 0;
	
	private static boolean checked = false;
	
	private static boolean matched = false;

	public static String[] getNameList() {
		String[] names = new String[chips.length];
		int i = 0;
		for (Chip chip: chips) {
			names[i] = chip.getDisplayName();
			i++;
		}
		return names;
	}
	
	public static String getID() {
		return chips[selected].getId();
	}

	public static String getName() {
		return chips[selected].getName();
	}
	
	public static int getReadBlockCount() {
		return chips[selected].getReadBlockCount();
	}
	
	public static int getReadBlockSize() {
		return chips[selected].getReadBlockSize();
	}

	public static int getWriteBlockSize() {
		return chips[selected].getWriteBlockSize();
	}
	
	public static int getCapacity() {
		return chips[selected].getCapacity();
	}

	public static int getPageSize() {
		return chips[selected].getPageSize();
	}
	
	public static String getType() {
		return chips[selected].getType();
	}
	
	public static int getSelected() {
		return selected;
	}

	public static void setSelected(int selected) {
		checked = false;
		matched = false;
		
		Chips.selected = selected;
	}
	
	public static boolean check(String idToCheck) {
		if (getID().equals(idToCheck)) {
			matched = true;
		}
		checked = true;
		
		return matched;
	}
	
	public static boolean isMatched() {
		return matched;
	}

	public static boolean isChecked() {
		return checked;
	}
	
}
