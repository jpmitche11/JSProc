package net.jpm.jslex;
/*
Copyright (c) 2000 The Regents of the University of California.
All rights reserved.

Permission to use, copy, modify, and distribute this software for any
purpose, without fee, and without written agreement is hereby granted,
provided that the above copyright notice and the following two
paragraphs appear in all copies of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 */

import java.io.PrintStream;

import java_cup.runtime.Symbol;
import net.jpm.jslex.TokenConstants;

class Utilities {
	
	/** Prints error message and exits 
	 *
	 * @param msg the error message
	 * */
	public static void fatalError(String msg) {
		(new Throwable(msg)).printStackTrace();
		System.exit(1);
	}

	/** Prints an appropritely escaped string
	 * 
	 * @param str the output stream
	 * @param s the string to print
	 * */
	public static String escapeString(String s) {
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '\\': out.append("\\\\"); break;
			case '\"': out.append("\\\""); break;
			case '\'': out.append("\\\'"); break;
			case '\n': out.append("\\n"); break;
			case '\t': out.append("\\t"); break;
			case '\b': out.append("\\b"); break;
			case '\f': out.append("\\f"); break;
			case '\r': out.append("\\r"); break;
			default:
				if (c >= 0x20 && c <= 0x7f) {
					out.append(c);
				} else {
					String octal = Integer.toOctalString(c);
					out.append('\\');
					switch (octal.length()) {
					case 1:
						out.append('0');
					case 2:
						out.append('0');
					default:
						out.append(octal);
					}
				}
			}
		}
		return out.toString();
	}

	/** Returns a string representation for a token
	 *
	 * @param s the token
	 * @return the string representation
	 * */
	public static String tokenToString(Symbol s) {
		switch (s.sym) {
		case TokenConstants.CLASS:      return("CLASS");
		case TokenConstants.ELSE:       return("ELSE");
		case TokenConstants.FI:         return("FI");
		case TokenConstants.IF:         return("IF");
		case TokenConstants.IN:         return("IN");
		case TokenConstants.INHERITS:   return("INHERITS");
		case TokenConstants.LET:        return("LET");  
		case TokenConstants.LOOP:       return("LOOP"); 
		case TokenConstants.POOL:       return("POOL"); 
		case TokenConstants.THEN:       return("THEN"); 
		case TokenConstants.WHILE:      return("WHILE"); 
		case TokenConstants.ASSIGN:     return("ASSIGN");
		case TokenConstants.CASE:       return("CASE");  
		case TokenConstants.ESAC:       return("ESAC");  
		case TokenConstants.OF:         return("OF");    
		case TokenConstants.DARROW:     return("DARROW");
		case TokenConstants.NEW:        return("NEW");   
		case TokenConstants.STR_CONST:  return("STR_CONST");
		case TokenConstants.INT_CONST:  return("INT_CONST");
		case TokenConstants.BOOL_CONST: return("BOOL_CONST");
		case TokenConstants.TYPEID:     return("TYPEID"); 
		case TokenConstants.OBJECTID:   return("OBJECTID");
		case TokenConstants.ERROR:      return("ERROR"); 
		case TokenConstants.error:      return("ERROR"); 
		case TokenConstants.LE:         return("LE");    
		case TokenConstants.NOT:        return("NOT");   
		case TokenConstants.ISVOID:     return("ISVOID");
		case TokenConstants.PLUS:       return("'+'");
		case TokenConstants.DIV:        return("'/'");
		case TokenConstants.MINUS:      return("'-'");
		case TokenConstants.MULT:       return("'*'");
		case TokenConstants.EQ:         return("'='");
		case TokenConstants.LT:         return("'<'");
		case TokenConstants.DOT:        return("'.'");
		case TokenConstants.NEG:        return("'~'");
		case TokenConstants.COMMA:      return("','");
		case TokenConstants.SEMI:       return("';'");
		case TokenConstants.COLON:      return("':'");
		case TokenConstants.LPAREN:     return("'('");
		case TokenConstants.RPAREN:     return("')'");
		case TokenConstants.AT:         return("'@'");
		case TokenConstants.LBRACE:     return("'{'");
		case TokenConstants.RBRACE:     return("'}'");
		case TokenConstants.EOF:        return("EOF");
		default:                        return("<Invalid Token: " + s.sym + ">");
		}
	}

	/** Prints a token to stderr
	 *
	 * @param s the token
	 * */
	public static void printToken(Symbol s) {    	
		System.out.print(tokenToString(s));

		if(s.value!=null){
			System.out.println(" "+escapeString((String)s.value));
		}
	}

	/** Dumps a token to the specified stream
	 *
	 * @param s the token
	 * @param str the stream
	 * */
	public static void dumpToken(PrintStream str, int lineno, Symbol s) {
		str.print("#" + lineno + " " + tokenToString(s));
		

		if(s.value!=null){
			str.println(" "+escapeString((String)s.value));
		}

	}

	/** Returns the specified amount of space padding 
	 *
	 * @param n the amount of padding
	 * */
	//    public static String pad(int n) {
	//	if (n > 80) return padding;
	//	if (n < 0) return "";
	//	return padding.substring(0, n);
	//    }
}





