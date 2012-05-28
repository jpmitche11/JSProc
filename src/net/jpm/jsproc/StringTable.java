package net.jpm.jsproc;

import java.util.HashSet;

/**
 * Table containing all Strings found by the scanner
 * @author pmitchel
 *
 */
public class StringTable {
   
	HashSet<String> table = new HashSet<String>();
    
	public String addString(String str){
		table.add(str);
		return str;
	}
	
}
