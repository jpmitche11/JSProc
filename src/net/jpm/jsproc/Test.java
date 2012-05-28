package net.jpm.jsproc;

public class Test {
	
	public static void main(String[] args){
		System.out.println(Double.parseDouble("0"));
		System.out.println(Double.parseDouble("0.1"));
		System.out.println(Double.parseDouble(".1"));
		System.out.println(Double.parseDouble("+1"));
		System.out.println(Double.parseDouble("-1"));
		System.out.println(Double.parseDouble("1.1e+1"));
		System.out.println(Double.parseDouble("1.1e-1"));
		System.out.println(Double.parseDouble("1.1e0"));
		System.out.println(Long.parseLong("fF", 16));
	}

}
