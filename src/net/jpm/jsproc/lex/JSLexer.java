/**
 * Lexer definition file.
 */
package net.jpm.jsproc.lex;
import java_cup.runtime.Symbol;
import net.jpm.jsproc.cup.TokenConstants;
import net.jpm.jsproc.StringTable;


class JSLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

    //holds string contents;
    private StringBuffer stringData = null;
    private String stringQuote;
    private String errStr;
    private StringTable stringTable = new StringTable();
    public int get_curr_lineno() {
       return yyline+1;
    }
    String filename;
    public void set_filename(String fname) {
        filename = stringTable.addString(fname);
    }
    public String curr_filename() {
        return filename;
    }
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	JSLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	JSLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private JSLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int STRING = 2;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int STRING_NULL = 3;
	private final int yy_state_dtrans[] = {
		0,
		46,
		51,
		53
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NOT_ACCEPT,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NOT_ACCEPT,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NOT_ACCEPT
	};
	private int yy_cmap[] = unpackFromString(1,130,
"6,7:8,31,3,14,31,4,7:18,31,7,5,7:4,5,24,25,2,15,21,16,20,1,33:10,23,22,19,1" +
"8,29,7,26,35:26,7,8,7:2,34,7,32,11,32:3,13,32:2,30,32:4,9,32:3,10,32,12,32:" +
"6,27,7,28,17,7,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,54,
"0,1,2,3,4,1:2,5,1:3,6,7,1:9,8,9,10,1:5,5,11,1:14,12,13,14,15,16,17,1,18")[0];

	private int yy_nxt[][] = unpackFromString(19,36,
"1,2,3,4:2,5,6:3,7:5,4,8,9,10,11,12,13,14,15,16,17,18,19,20,21,6,47,4,7,22,6" +
",23,-1:37,24,25,-1:34,26,-1:37,4:2,-1:9,4,-1:16,4,-1:13,7:5,-1:16,7,-1,7:4," +
"-1:29,27,-1:22,28,-1,29,-1:50,22,-1:11,23:5,-1:16,23,-1,23:4,-1,24:2,-1,24:" +
"32,-1:2,25,-1:33,1,31,48,52:33,-1:9,7:4,30,-1:16,7,-1,7:4,-1,32,-1:35,38:8," +
"39,40,41,42,43,38:22,-1:3,44,-1:32,1,33:2,34:2,35,36,33,49,33:5,37,33:21,1," +
"44:2,45,6,45,44:2,50,44:27");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

            /*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
             *  executed when end-of-file is reached.  If you use multiple lexical
             *  states and want to do something special if an EOF is encountered in
             *  one of those states, place your code in the switch statement.
             *  Ultimately, you should return the EOF symbol, or your lexer won't
             *  work.  */
                switch(yy_lexical_state) {
                    case YYINITIAL:
                    /* nothing special to do in the initial state */
                    break;
                    case COMMENT:
                        yybegin(YYINITIAL); 
                        return new Symbol(TokenConstants.ERROR, "EOF in comment");
                    case STRING:
                        yybegin(YYINITIAL); 
                        return new Symbol(TokenConstants.ERROR, "EOF in string constant");                   
                    /* If necessary, add code for other states here, e.g:
                       case COMMENT:
                       ...
                       break;
                    */
                }
                return new Symbol(TokenConstants.EOF);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ return new Symbol(TokenConstants.DIV); }
					case -3:
						break;
					case 3:
						{ return new Symbol(TokenConstants.MULT); }
					case -4:
						break;
					case 4:
						{  
  //  System.out.println("Whitespace"); 
}
					case -5:
						break;
					case 5:
						{
    yybegin(STRING);
    stringQuote = yytext();
   // System.out.println("Begin String: "+stringQuote);
    stringData = new StringBuffer();
}
					case -6:
						break;
					case 6:
						{ //Catchall rule to handle unknown characters 
   return new Symbol(TokenConstants.ERROR, yytext());
}
					case -7:
						break;
					case 7:
						{
  return new Symbol(TokenConstants.OBJECTID, this.stringTable.addString(yytext()) );  
}
					case -8:
						break;
					case 8:
						{ return new Symbol(TokenConstants.PLUS); }
					case -9:
						break;
					case 9:
						{ return new Symbol(TokenConstants.MINUS); }
					case -10:
						break;
					case 10:
						{ return new Symbol(TokenConstants.NEG); }
					case -11:
						break;
					case 11:
						{ return new Symbol(TokenConstants.EQ); }
					case -12:
						break;
					case 12:
						{ return new Symbol(TokenConstants.LT); }
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.DOT); }
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.COMMA); }
					case -15:
						break;
					case 15:
						{ return new Symbol(TokenConstants.SEMI); }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.COLON); }
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -19:
						break;
					case 19:
						{ return new Symbol(TokenConstants.AT); }
					case -20:
						break;
					case 20:
						{ return new Symbol(TokenConstants.LBRACE); }
					case -21:
						break;
					case 21:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -22:
						break;
					case 22:
						{
  return new Symbol(TokenConstants.INT_CONST, this.stringTable.addString(yytext()) );  
}
					case -23:
						break;
					case 23:
						{
  return new Symbol(TokenConstants.TYPEID, this.stringTable.addString(yytext()) );  
}
					case -24:
						break;
					case 24:
						{ 
    //Line Comment
}
					case -25:
						break;
					case 25:
						{
//Comments
    yybegin(COMMENT);   
}
					case -26:
						break;
					case 26:
						{
    return new Symbol(TokenConstants.ERROR, "Unmatched Comment end */" );
}
					case -27:
						break;
					case 27:
						{ return new Symbol(TokenConstants.DARROW); }
					case -28:
						break;
					case 28:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -29:
						break;
					case 29:
						{ return new Symbol(TokenConstants.LE); }
					case -30:
						break;
					case 30:
						{    return new Symbol(TokenConstants.IF);  }
					case -31:
						break;
					case 31:
						{ }
					case -32:
						break;
					case 32:
						{
    yybegin(YYINITIAL);
}
					case -33:
						break;
					case 33:
						{
    stringData.append(yytext());
}
					case -34:
						break;
					case 34:
						{
    yybegin(YYINITIAL);
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -35:
						break;
					case 35:
						{    
   // System.out.println("Process string quote: "+yytext());
    if(stringQuote.equals(yytext())){
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.STR_CONST, this.stringTable.addString( stringData.toString() ) );
    }
    else{
        stringData.append(yytext());
    }
}
					case -36:
						break;
					case 36:
						{ yybegin(STRING_NULL); errStr = "String contains null character"; }
					case -37:
						break;
					case 37:
						{
   // stringData.append("\v");
   return new Symbol(TokenConstants.ERROR, "Vertical tab in string"); 
}
					case -38:
						break;
					case 38:
						{
    //escape sequence
    stringData.append(yytext().substring(1));
}
					case -39:
						break;
					case 39:
						{
    stringData.append("\n");
}
					case -40:
						break;
					case 40:
						{
    stringData.append("\r");
}
					case -41:
						break;
					case 41:
						{
    stringData.append("\b");
}
					case -42:
						break;
					case 42:
						{
    stringData.append("\t");
}
					case -43:
						break;
					case 43:
						{
    stringData.append("\f");
}
					case -44:
						break;
					case 44:
						{}
					case -45:
						break;
					case 45:
						{
     yybegin(YYINITIAL);
     return new Symbol(TokenConstants.ERROR, errStr);     
}
					case -46:
						break;
					case 47:
						{
  return new Symbol(TokenConstants.OBJECTID, this.stringTable.addString(yytext()) );  
}
					case -47:
						break;
					case 48:
						{ }
					case -48:
						break;
					case 49:
						{
    stringData.append(yytext());
}
					case -49:
						break;
					case 50:
						{}
					case -50:
						break;
					case 52:
						{ }
					case -51:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
