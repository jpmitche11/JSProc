package net.jpm.jsproc;
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
import net.jpm.jsproc.cup.TokenConstants;

public class Utilities {
	
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
	public static String escapeString(Object o) {
		String s;
		if(o instanceof String){
			s = (String)o;
		}
		else{
			s = "" + o;
		}
		
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
		
		case TokenConstants.LT:    return("<");
		case TokenConstants.LE:    return("<=");
		case TokenConstants.GT:    return(">");
		case TokenConstants.GE:    return(">=");
		
		case TokenConstants.INSTANCEOF:    return("instanceof");
		case TokenConstants.TYPEOF:    return("typeof");
		
		case TokenConstants.TRY:    return("try");
		case TokenConstants.CATCH:    return("catch");
		case TokenConstants.THROW:    return("throw");
		
		
		
		
		
		case TokenConstants.SEMI:    return(";");
		case TokenConstants.COMMA:    return(",");
		
		case TokenConstants.LPAREN:    return("(");
		case TokenConstants.RPAREN:    return(")");
		
		case TokenConstants.LBRACE:    return("{");
		case TokenConstants.RBRACE:    return("}");
		
		case TokenConstants.ASSIGN:    return("=");
		case TokenConstants.EQ:    return("==");
		case TokenConstants.EQQ:    return("===");
		
		case TokenConstants.PLUS_EQ:    return("+=");
		case TokenConstants.MINUS_EQ:    return("-=");
		case TokenConstants.DIV_EQ:    return("/=");
		case TokenConstants.MULT_EQ:    return("*=");
		
		case TokenConstants.OR_EQ:    return("|=");
		case TokenConstants.XOR_EQ:    return("^=");
		case TokenConstants.MOD_EQ:    return("%=");
		
		
		case TokenConstants.RSHIFT_ZERO_EQ:    return(">>>=");
		case TokenConstants.RSHIFT_EQ:    return(">>=");
		case TokenConstants.LSHIFT_EQ:    return("<<=");
		
		case TokenConstants.RSHIFT_ZERO:    return(">>>");
		case TokenConstants.FALSE:    return("false");
		
		case TokenConstants.NOT:    return("!");
		
		
		case TokenConstants.NEQQ:    return("!==");
		
		case TokenConstants.VAR:    return("var");
		case TokenConstants.BIN_AND:    return("&");
		
		case TokenConstants.BIN_XOR:    return("^");
		
		case TokenConstants.MOD:    return("%");
		case TokenConstants.BIN_OR:    return("|");
		
		case TokenConstants.TRUE:    return("true");
		case TokenConstants.PLUS:    return("+");
		case TokenConstants.WHILE:    return("while");
		case TokenConstants.DELETE:    return("delete");
		case TokenConstants.CONDITIONAL:    return("?");
		case TokenConstants.SWITCH:    return("switch");
		case TokenConstants.DO:    return("do");
		case TokenConstants.FOR:    return("for");
		case TokenConstants.VOID:    return("void");
		case TokenConstants.DIV:    return("/");
		
		case TokenConstants.RETURN:    return("return");
		case TokenConstants.MULT:    return("*");
		case TokenConstants.ELSE:    return("else");
		
		case TokenConstants.BREAK:    return("break");
		case TokenConstants.DOT:    return(".");
		case TokenConstants.UNDEFINED:    return("undefined");
		case TokenConstants.NULL:    return("null");
		
		
		case TokenConstants.THIS:    return("this");
		case TokenConstants.DEFAULT:    return("default");
		
		case TokenConstants.BIN_NOT:    return("~");
		case TokenConstants.FUNCTION:    return("function");
		case TokenConstants.AND_EQ:    return("&=");
		case TokenConstants.MINUS:    return("-");
		case TokenConstants.IN:    return("in");
		case TokenConstants.OR:    return("or");
		
		case TokenConstants.FINALLY:    return("finally");
		case TokenConstants.CONTINUE:    return("continue");
		case TokenConstants.IF:    return("if");
		
		
		case TokenConstants.MINUSMINUS:    return("--");
		case TokenConstants.COLON:    return(":");
		
		case TokenConstants.RBRACKET:    return("]");
		case TokenConstants.CASE:    return("case");
		case TokenConstants.PLUSPLUS:    return("++");
		case TokenConstants.NEW:    return("new");
		case TokenConstants.RSHIFT:    return(">>");
		case TokenConstants.NEQ:    return("!=");
		case TokenConstants.AND:    return("&&");
		case TokenConstants.LBRACKET:    return("[");
		case TokenConstants.LSHIFT:    return("<<");
		case TokenConstants.WITH:    return("WITH");

		
		case TokenConstants.ID: return("ID");
		case TokenConstants.STRING_CONST: return("STRING");
		case TokenConstants.NUMBER_CONST: return("NUMBER");
		case TokenConstants.REGEXP_CONST: return("RegExp Literal");
		case TokenConstants.EOF:        return("EOF");
		case TokenConstants.ERROR:    return("<ERROR>: " + s.value);
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
			System.out.print(" "+escapeString(s.value));
		}
		System.out.println("");
	}

	/** Dumps a token to the specified stream
	 *
	 * @param s the token
	 * @param str the stream
	 * */
	public static void dumpToken(PrintStream str, int lineno, Symbol s) {
		str.print("#" + lineno + " " + tokenToString(s));
		

		if(s.value!=null){
			str.print(" "+escapeString(s.value));
			
		}
		System.out.println("");

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





