/**
 * Lexer definition file.
 */
package net.jpm.jsproc.lex;
import java_cup.runtime.Symbol;
import net.jpm.jsproc.cup.TokenConstants;
import net.jpm.jsproc.StringTable;


public class JSLexer implements java_cup.runtime.Scanner {
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
    private StringBuffer charData = null;
    private String stringQuote;
    private String errStr;
    private StringTable stringTable = new StringTable();
    public int getCurrentLineNum() {
       return yyline+1;
    }
    String fileName;
    public void setFileName(String fileName) {
        this.fileName = stringTable.addString(fileName);
    }
    public String getFileName() {
        return fileName;
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

	public JSLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	public JSLexer (java.io.InputStream instream) {
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
	private final int STRING_ERR = 3;
	private final int REGEXP_ERR = 5;
	private final int STRING = 2;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int REGEXP = 4;
	private final int yy_state_dtrans[] = {
		0,
		135,
		137,
		141,
		143,
		143
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
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NOT_ACCEPT,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NOT_ACCEPT,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NOT_ACCEPT,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NOT_ACCEPT,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NOT_ACCEPT,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NOT_ACCEPT,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NOT_ACCEPT,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NOT_ACCEPT,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NOT_ACCEPT,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NOT_ACCEPT,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NOT_ACCEPT,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NOT_ACCEPT,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NOT_ACCEPT,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NOT_ACCEPT,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NO_ANCHOR,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NO_ANCHOR,
		/* 174 */ YY_NO_ANCHOR,
		/* 175 */ YY_NO_ANCHOR,
		/* 176 */ YY_NO_ANCHOR,
		/* 177 */ YY_NO_ANCHOR,
		/* 178 */ YY_NO_ANCHOR,
		/* 179 */ YY_NO_ANCHOR,
		/* 180 */ YY_NO_ANCHOR,
		/* 181 */ YY_NO_ANCHOR,
		/* 182 */ YY_NO_ANCHOR,
		/* 183 */ YY_NO_ANCHOR,
		/* 184 */ YY_NO_ANCHOR,
		/* 185 */ YY_NO_ANCHOR,
		/* 186 */ YY_NO_ANCHOR,
		/* 187 */ YY_NO_ANCHOR,
		/* 188 */ YY_NO_ANCHOR,
		/* 189 */ YY_NO_ANCHOR,
		/* 190 */ YY_NO_ANCHOR,
		/* 191 */ YY_NO_ANCHOR,
		/* 192 */ YY_NO_ANCHOR,
		/* 193 */ YY_NO_ANCHOR,
		/* 194 */ YY_NO_ANCHOR,
		/* 195 */ YY_NO_ANCHOR,
		/* 196 */ YY_NO_ANCHOR,
		/* 197 */ YY_NO_ANCHOR,
		/* 198 */ YY_NO_ANCHOR,
		/* 199 */ YY_NO_ANCHOR,
		/* 200 */ YY_NO_ANCHOR,
		/* 201 */ YY_NO_ANCHOR,
		/* 202 */ YY_NO_ANCHOR,
		/* 203 */ YY_NO_ANCHOR,
		/* 204 */ YY_NO_ANCHOR,
		/* 205 */ YY_NO_ANCHOR,
		/* 206 */ YY_NO_ANCHOR,
		/* 207 */ YY_NO_ANCHOR,
		/* 208 */ YY_NO_ANCHOR,
		/* 209 */ YY_NO_ANCHOR,
		/* 210 */ YY_NO_ANCHOR,
		/* 211 */ YY_NO_ANCHOR,
		/* 212 */ YY_NO_ANCHOR,
		/* 213 */ YY_NO_ANCHOR,
		/* 214 */ YY_NO_ANCHOR,
		/* 215 */ YY_NO_ANCHOR,
		/* 216 */ YY_NO_ANCHOR,
		/* 217 */ YY_NO_ANCHOR,
		/* 218 */ YY_NO_ANCHOR,
		/* 219 */ YY_NO_ANCHOR,
		/* 220 */ YY_NO_ANCHOR,
		/* 221 */ YY_NO_ANCHOR,
		/* 222 */ YY_NO_ANCHOR,
		/* 223 */ YY_NO_ANCHOR,
		/* 224 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"6,7:8,55,3,55:2,4,7:18,55,42,5,7,56,36,37,5,48,49,2,33,45,34,44,1,14,15,57," +
"16,57:6,47,46,41,35,40,32,7,59:4,60,59,56:17,58,56:2,52,8,53,39,56,7,22,11," +
"18,28,23,13,54,24,19,56,30,26,54,9,20,31,56,10,25,12,17,21,27,58,29,56,50,3" +
"8,51,43,7,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,225,
"0,1,2,3,4,1:2,5,6,7,1,8,9,10,11,12,13,14,15,16,17,1:11,18,19,1:3,20,21,22:2" +
",1:4,23,1:7,24,1,25,26,27,22:3,1,22,1:2,28,1:2,22:7,1,22:15,29,1:9,19,30,31" +
",32,33,1,34,35,36,37,38,39,40,41,1,42,1:2,43,44,45,46,47,48,49,50,51,52,45," +
"53,42,54,46,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75," +
"76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100" +
",101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,11" +
"9,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,2" +
"2,138,139,140,141,142,143,144")[0];

	private int yy_nxt[][] = unpackFromString(145,61,
"1,2,3,4:2,5,6:2,102,7,192,206,213,214,8,9:2,215,216,103,217,218,217,219,217" +
",220,217,221,114,217:3,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,2" +
"7,28,29,30,31,217,4,217,9,217:3,-1:62,32,33,-1:2,101:3,113,101:52,-1,34,-1:" +
"33,35,-1:28,4:2,-1:50,4,-1:14,217:8,222,217:5,120,217:8,-1:22,217,-1,217:5," +
"-1:14,37:3,-1:6,119,-1:20,104,-1:12,37,123,-1,119,-1:14,9:3,-1:6,125,-1:20," +
"105,-1:12,9,-1:2,125,-1:33,41,-1,42,-1:59,43,44,-1:60,45,-1:60,46,-1:60,47," +
"-1,48,-1:58,49,-1:2,50,-1:57,51,-1:60,52,-1:4,53,-1:55,54,-1:5,55,-1:54,56," +
"-1:26,32:2,-1,32:57,-1,57,101,-1:2,101:3,113,101:52,-1:14,37:3,-1:6,127,-1:" +
"20,107,-1:12,37,-1:2,127,-1:9,217:16,200,217:6,-1:22,217,-1,217:5,-1:9,217:" +
"23,-1:22,217,-1,217:5,-1:35,63,-1:60,64,-1:4,65,-1:55,66,-1:60,67,-1:44,57," +
"-1:34,57,-1:41,75,-1:27,106,-1:93,36,-1:34,38,217:3,39,217:18,-1:22,217,-1," +
"217:5,-1:14,104:3,-1:6,119,-1:33,104,-1:2,119,-1:14,105:3,-1:6,125,-1:33,10" +
"5,-1:2,125,-1:14,107:3,-1:6,127,-1:33,107,-1:2,127,-1,57,101,-1:2,101:3,113" +
",101:10,108,101:34,108,101:6,-1,92,-1:60,98:2,-1:2,98:4,97:4,98,111,98:46,-" +
"1:15,139,-1:48,99,-1:58,108,101,-1:2,101:3,113,101:52,-1:9,217:11,40,217:2," +
"147,217:8,-1:22,217,-1,217:5,-1:14,116:3,-1:40,116,-1:17,115:3,-1:16,121,12" +
"9,-1:22,115,-1:12,217:18,58,217:4,-1:22,217,-1,217:5,-1:14,115:3,-1:16,115," +
"-1:23,115,-1:17,117:3,-1:16,117,-1:23,117,-1:14,61,-1,61:4,-1,61,-1:3,61:2," +
"-1:4,61,-1:28,61,-1,61:2,-1:9,217:3,149,217:19,-1:22,217,-1,217:5,-1:14,116" +
":3,-1:16,131:2,-1:22,116,-1:12,217:14,150,217:8,-1:22,217,-1,217:5,-1:14,11" +
"7:3,-1:16,122,133,-1:22,117,-1:12,217:8,151,217:11,59,217:2,-1:22,217,-1,21" +
"7:5,-1:9,217,152,217:8,153,217:12,-1:22,217,-1,217:5,-1:9,217:22,154,-1:22," +
"217,-1,217:5,-1:9,155,217:22,-1:22,217,-1,217:5,1,91,109,118:58,-1:9,217,60" +
",217:21,-1:22,217,-1,217:5,1,93:2,94:2,95,96,93,110,93:4,97,93:47,-1:9,217:" +
"17,196,217:5,-1:22,217,-1,217:5,-1:16,97,-1:53,217:19,198,217:3,-1:22,217,-" +
"1,217:5,1,99:2,100,6,100,99:2,112,99:52,-1:9,217:3,199,217:12,157,217:6,-1:" +
"22,217,-1,217:5,1,6:60,-1:9,217:10,158,217:12,-1:22,217,-1,217:5,-1:9,217,6" +
"2,217:21,-1:22,217,-1,217:5,-1:9,217:16,159,217:6,-1:22,217,-1,217:5,-1:9,2" +
"17:4,209,217:12,210,217:5,-1:22,217,-1,217:5,-1:9,217:17,68,217:5,-1:22,217" +
",-1,217:5,-1:9,217:8,162,217:14,-1:22,217,-1,217:5,-1:9,217:13,163,217:9,-1" +
":22,217,-1,217:5,-1:9,217:14,69,217:8,-1:22,217,-1,217:5,-1:9,217:11,164,21" +
"7:11,-1:22,217,-1,217:5,-1:9,217:16,70,217:6,-1:22,217,-1,217:5,-1:9,217:14" +
",165,217:8,-1:22,217,-1,217:5,-1:9,217:9,223,217:13,-1:22,217,-1,217:5,-1:9" +
",217:3,168,217:19,-1:22,217,-1,217:5,-1:9,217:14,71,217:8,-1:22,217,-1,217:" +
"5,-1:9,217:19,72,217:3,-1:22,217,-1,217:5,-1:9,217:14,73,217:8,-1:22,217,-1" +
",217:5,-1:9,217:15,74,217:7,-1:22,217,-1,217:5,-1:9,217:17,172,217:5,-1:22," +
"217,-1,217:5,-1:9,217,175,217:21,-1:22,217,-1,217:5,-1:9,217:21,76,217,-1:2" +
"2,217,-1,217:5,-1:9,217:18,77,217:4,-1:22,217,-1,217:5,-1:9,217:11,176,217:" +
"11,-1:22,217,-1,217:5,-1:9,217:14,78,217:8,-1:22,217,-1,217:5,-1:9,217:4,22" +
"4,217:18,-1:22,217,-1,217:5,-1:9,217:10,178,217:12,-1:22,217,-1,217:5,-1:9," +
"217:15,79,217:7,-1:22,217,-1,217:5,-1:9,217:13,204,217:9,-1:22,217,-1,217:5" +
",-1:9,217:9,179,217:13,-1:22,217,-1,217:5,-1:9,217:14,80,217:8,-1:22,217,-1" +
",217:5,-1:9,217:8,203,217:14,-1:22,217,-1,217:5,-1:9,217:3,180,217:19,-1:22" +
",217,-1,217:5,-1:9,81,217:22,-1:22,217,-1,217:5,-1:9,217:4,82,217:18,-1:22," +
"217,-1,217:5,-1:9,217:17,182,217:5,-1:22,217,-1,217:5,-1:9,183,217:22,-1:22" +
",217,-1,217:5,-1:9,217:15,83,217:7,-1:22,217,-1,217:5,-1:9,217:14,84,217:8," +
"-1:22,217,-1,217:5,-1:9,217:11,186,217:11,-1:22,217,-1,217:5,-1:9,217:20,85" +
",217:2,-1:22,217,-1,217:5,-1:9,217:8,188,217:14,-1:22,217,-1,217:5,-1:9,217" +
":9,205,217:13,-1:22,217,-1,217:5,-1:9,217:3,86,217:19,-1:22,217,-1,217:5,-1" +
":9,87,217:22,-1:22,217,-1,217:5,-1:9,217:14,189,217:8,-1:22,217,-1,217:5,-1" +
":9,217:14,88,217:8,-1:22,217,-1,217:5,-1:9,217:19,89,217:3,-1:22,217,-1,217" +
":5,-1:9,217:11,191,217:11,-1:22,217,-1,217:5,-1:9,217:4,90,217:18,-1:22,217" +
",-1,217:5,-1:9,217:14,124,217:8,-1:22,217,-1,217:5,-1:9,217:3,160,217:19,-1" +
":22,217,-1,217:5,-1:9,197,217:22,-1:22,217,-1,217:5,-1:9,217:10,211,217:12," +
"-1:22,217,-1,217:5,-1:9,217:16,166,217:6,-1:22,217,-1,217:5,-1:9,217:13,201" +
",217:9,-1:22,217,-1,217:5,-1:9,217:14,167,217:8,-1:22,217,-1,217:5,-1:9,217" +
":9,169,217:13,-1:22,217,-1,217:5,-1:9,217:3,170,217:19,-1:22,217,-1,217:5,-" +
"1:9,217:17,177,217:5,-1:22,217,-1,217:5,-1:9,217:10,181,217:12,-1:22,217,-1" +
",217:5,-1:9,217:17,185,217:5,-1:22,217,-1,217:5,-1:9,184,217:22,-1:22,217,-" +
"1,217:5,-1:9,217:14,190,217:8,-1:22,217,-1,217:5,-1:9,217,126,217:21,-1:22," +
"217,-1,217:5,-1:9,156,217:22,-1:22,217,-1,217:5,-1:9,217:10,161,217:12,-1:2" +
"2,217,-1,217:5,-1:9,217:13,173,217:9,-1:22,217,-1,217:5,-1:9,217:14,174,217" +
":8,-1:22,217,-1,217:5,-1:9,217:3,171,217:19,-1:22,217,-1,217:5,-1:9,187,217" +
":22,-1:22,217,-1,217:5,-1:9,217,128,217:13,130,217:4,132,217:2,-1:22,217,-1" +
",217:5,-1:9,217:8,134,217,194,136,217,138,217:9,-1:22,217,-1,217:5,-1:9,140" +
",217:22,-1:22,217,-1,217:5,-1:9,217:11,207,217,142,217:9,-1:22,217,-1,217:5" +
",-1:9,217:11,144,217,145,217:9,-1:22,217,-1,217:5,-1:9,217:17,146,217:5,-1:" +
"22,217,-1,217:5,-1:9,217:18,195,217:4,-1:22,217,-1,217:5,-1:9,217:10,193,21" +
"7:4,208,217:7,-1:22,217,-1,217:5,-1:9,217:17,148,217:5,-1:22,217,-1,217:5,-" +
"1:9,217:3,202,217:19,-1:22,217,-1,217:5,-1:9,217:10,212,217:12,-1:22,217,-1" +
",217:5");

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
                        return new Symbol(TokenConstants.ERROR, "EOF in string literal");                   
                    case REGEXP:
                        yybegin(YYINITIAL); 
                        return new Symbol(TokenConstants.ERROR, "EOF in regexp literal");                   
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
    charData = new StringBuffer();
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
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -8:
						break;
					case 8:
						{
  try{
    return new Symbol(TokenConstants.NUMBER_CONST, Double.parseDouble(yytext()) );
  }catch(Exception e){ return new Symbol(TokenConstants.ERROR, e.toString()); }  
}
					case -9:
						break;
					case 9:
						{
    //splitting this into several rules, one for the format 0.00e+0, next for the format .00e+0
  try{  
    return new Symbol(TokenConstants.NUMBER_CONST, Double.parseDouble(yytext()) ); 
  }catch(Exception e){ return new Symbol(TokenConstants.ERROR, e.toString()); } 
}
					case -10:
						break;
					case 10:
						{ return new Symbol(TokenConstants.CONDITIONAL); }
					case -11:
						break;
					case 11:
						{ return new Symbol(TokenConstants.PLUS); }
					case -12:
						break;
					case 12:
						{ return new Symbol(TokenConstants.MINUS); }
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.MOD); }
					case -15:
						break;
					case 15:
						{ return new Symbol(TokenConstants.BIN_AND); }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.BIN_OR); }
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.BIN_XOR); }
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.GT); }
					case -19:
						break;
					case 19:
						{ return new Symbol(TokenConstants.LT); }
					case -20:
						break;
					case 20:
						{ return new Symbol(TokenConstants.NOT); }
					case -21:
						break;
					case 21:
						{ return new Symbol(TokenConstants.BIN_NOT); }
					case -22:
						break;
					case 22:
						{ return new Symbol(TokenConstants.DOT); }
					case -23:
						break;
					case 23:
						{ return new Symbol(TokenConstants.COMMA); }
					case -24:
						break;
					case 24:
						{ return new Symbol(TokenConstants.SEMI); }
					case -25:
						break;
					case 25:
						{ return new Symbol(TokenConstants.COLON); }
					case -26:
						break;
					case 26:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -27:
						break;
					case 27:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -28:
						break;
					case 28:
						{ return new Symbol(TokenConstants.LBRACE); }
					case -29:
						break;
					case 29:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -30:
						break;
					case 30:
						{ return new Symbol(TokenConstants.LBRACKET); }
					case -31:
						break;
					case 31:
						{ return new Symbol(TokenConstants.RBRACKET); }
					case -32:
						break;
					case 32:
						{ 
    //Line Comment
}
					case -33:
						break;
					case 33:
						{
//Comments
    yybegin(COMMENT);   
}
					case -34:
						break;
					case 34:
						{
    return new Symbol(TokenConstants.ERROR, "Unmatched Comment end */" );
}
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.MULT_EQ); }
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.DIV_EQ); }
					case -37:
						break;
					case 37:
						{
    //this is an error case for to catch all numbers beginning with 0, other than "0", or "0.xxx"
    //ECMA does not specifiy literal octals, so not all browsers support them  
  return new Symbol(TokenConstants.ERROR, "Illegal numeric literal, remove leading zero. Octal literals should not be used.  input: "+yytext() );  
}
					case -38:
						break;
					case 38:
						{  return new Symbol(TokenConstants.IN); }
					case -39:
						break;
					case 39:
						{  return new Symbol(TokenConstants.IF); }
					case -40:
						break;
					case 40:
						{  return new Symbol(TokenConstants.DO); }
					case -41:
						break;
					case 41:
						{ return new Symbol(TokenConstants.PLUSPLUS); }
					case -42:
						break;
					case 42:
						{ return new Symbol(TokenConstants.PLUS_EQ); }
					case -43:
						break;
					case 43:
						{ return new Symbol(TokenConstants.MINUSMINUS); }
					case -44:
						break;
					case 44:
						{ return new Symbol(TokenConstants.MINUS_EQ); }
					case -45:
						break;
					case 45:
						{ return new Symbol(TokenConstants.EQ); }
					case -46:
						break;
					case 46:
						{ return new Symbol(TokenConstants.MINUSMINUS); }
					case -47:
						break;
					case 47:
						{ return new Symbol(TokenConstants.AND_EQ); }
					case -48:
						break;
					case 48:
						{ return new Symbol(TokenConstants.AND); }
					case -49:
						break;
					case 49:
						{ return new Symbol(TokenConstants.OR_EQ); }
					case -50:
						break;
					case 50:
						{ return new Symbol(TokenConstants.OR); }
					case -51:
						break;
					case 51:
						{ return new Symbol(TokenConstants.XOR_EQ); }
					case -52:
						break;
					case 52:
						{ return new Symbol(TokenConstants.GE); }
					case -53:
						break;
					case 53:
						{ return new Symbol(TokenConstants.RSHIFT); }
					case -54:
						break;
					case 54:
						{ return new Symbol(TokenConstants.LE); }
					case -55:
						break;
					case 55:
						{ return new Symbol(TokenConstants.LSHIFT); }
					case -56:
						break;
					case 56:
						{ return new Symbol(TokenConstants.NEQ); }
					case -57:
						break;
					case 57:
						{  return new Symbol(TokenConstants.REGEXP_CONST, this.stringTable.addString(yytext()) ); }
					case -58:
						break;
					case 58:
						{  return new Symbol(TokenConstants.NEW); }
					case -59:
						break;
					case 59:
						{  return new Symbol(TokenConstants.TRY); }
					case -60:
						break;
					case 60:
						{  return new Symbol(TokenConstants.FOR); }
					case -61:
						break;
					case 61:
						{
    //hexidecimal number
    try{
        return new Symbol(TokenConstants.NUMBER_CONST, Long.parseLong(yytext().substring(2), 16)) ;
    }catch(Exception e){ return new Symbol(TokenConstants.ERROR, e.toString()); }      
}
					case -62:
						break;
					case 62:
						{  return new Symbol(TokenConstants.VAR); }
					case -63:
						break;
					case 63:
						{ return new Symbol(TokenConstants.EQQ); }
					case -64:
						break;
					case 64:
						{ return new Symbol(TokenConstants.RSHIFT_EQ); }
					case -65:
						break;
					case 65:
						{ return new Symbol(TokenConstants.RSHIFT_ZERO); }
					case -66:
						break;
					case 66:
						{ return new Symbol(TokenConstants.LSHIFT_EQ); }
					case -67:
						break;
					case 67:
						{ return new Symbol(TokenConstants.NEQQ); }
					case -68:
						break;
					case 68:
						{  return new Symbol(TokenConstants.NULL); }
					case -69:
						break;
					case 69:
						{  return new Symbol(TokenConstants.TRUE); }
					case -70:
						break;
					case 70:
						{  return new Symbol(TokenConstants.THIS); }
					case -71:
						break;
					case 71:
						{  return new Symbol(TokenConstants.CASE); }
					case -72:
						break;
					case 72:
						{  return new Symbol(TokenConstants.VOID); }
					case -73:
						break;
					case 73:
						{  return new Symbol(TokenConstants.ELSE); }
					case -74:
						break;
					case 74:
						{  return new Symbol(TokenConstants.WITH); }
					case -75:
						break;
					case 75:
						{ return new Symbol(TokenConstants.RSHIFT_ZERO_EQ); }
					case -76:
						break;
					case 76:
						{  return new Symbol(TokenConstants.BREAK); }
					case -77:
						break;
					case 77:
						{  return new Symbol(TokenConstants.THROW); }
					case -78:
						break;
					case 78:
						{  return new Symbol(TokenConstants.FALSE); }
					case -79:
						break;
					case 79:
						{  return new Symbol(TokenConstants.CATCH); }
					case -80:
						break;
					case 80:
						{  return new Symbol(TokenConstants.WHILE); }
					case -81:
						break;
					case 81:
						{  return new Symbol(TokenConstants.RETURN); }
					case -82:
						break;
					case 82:
						{  return new Symbol(TokenConstants.TYPEOF); }
					case -83:
						break;
					case 83:
						{  return new Symbol(TokenConstants.SWITCH); }
					case -84:
						break;
					case 84:
						{  return new Symbol(TokenConstants.DELETE); }
					case -85:
						break;
					case 85:
						{  return new Symbol(TokenConstants.FINALLY); }
					case -86:
						break;
					case 86:
						{  return new Symbol(TokenConstants.DEFAULT); }
					case -87:
						break;
					case 87:
						{  return new Symbol(TokenConstants.FUNCTION); }
					case -88:
						break;
					case 88:
						{  return new Symbol(TokenConstants.CONTINUE); }
					case -89:
						break;
					case 89:
						{  return new Symbol(TokenConstants.UNDEFINED); }
					case -90:
						break;
					case 90:
						{  return new Symbol(TokenConstants.INSTANCEOF); }
					case -91:
						break;
					case 91:
						{ }
					case -92:
						break;
					case 92:
						{
    yybegin(YYINITIAL);
}
					case -93:
						break;
					case 93:
						{
    charData.append(yytext());
}
					case -94:
						break;
					case 94:
						{
    yybegin(YYINITIAL);
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -95:
						break;
					case 95:
						{    
   // System.out.println("Process string quote: "+yytext());
    if(stringQuote.equals(yytext())){
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.STRING_CONST, this.stringTable.addString( charData.toString() ) );
    }
    else{
        charData.append(yytext());
    }
}
					case -96:
						break;
					case 96:
						{ yybegin(STRING_ERR); errStr = "String contains null character"; }
					case -97:
						break;
					case 97:
						{
    charData.append(yytext());
}
					case -98:
						break;
					case 98:
						{
    //escape sequence
    charData.append(yytext().substring(1));
}
					case -99:
						break;
					case 99:
						{}
					case -100:
						break;
					case 100:
						{
     yybegin(YYINITIAL);
     return new Symbol(TokenConstants.ERROR, errStr);     
}
					case -101:
						break;
					case 102:
						{ //Catchall rule to handle unknown characters 
   return new Symbol(TokenConstants.ERROR, yytext());
}
					case -102:
						break;
					case 103:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -103:
						break;
					case 104:
						{
  try{
    return new Symbol(TokenConstants.NUMBER_CONST, Double.parseDouble(yytext()) );
  }catch(Exception e){ return new Symbol(TokenConstants.ERROR, e.toString()); }  
}
					case -104:
						break;
					case 105:
						{
    //splitting this into several rules, one for the format 0.00e+0, next for the format .00e+0
  try{  
    return new Symbol(TokenConstants.NUMBER_CONST, Double.parseDouble(yytext()) ); 
  }catch(Exception e){ return new Symbol(TokenConstants.ERROR, e.toString()); } 
}
					case -105:
						break;
					case 106:
						{
//Comments
    yybegin(COMMENT);   
}
					case -106:
						break;
					case 107:
						{
    //this is an error case for to catch all numbers beginning with 0, other than "0", or "0.xxx"
    //ECMA does not specifiy literal octals, so not all browsers support them  
  return new Symbol(TokenConstants.ERROR, "Illegal numeric literal, remove leading zero. Octal literals should not be used.  input: "+yytext() );  
}
					case -107:
						break;
					case 108:
						{  return new Symbol(TokenConstants.REGEXP_CONST, this.stringTable.addString(yytext()) ); }
					case -108:
						break;
					case 109:
						{ }
					case -109:
						break;
					case 110:
						{
    charData.append(yytext());
}
					case -110:
						break;
					case 111:
						{
    //escape sequence
    charData.append(yytext().substring(1));
}
					case -111:
						break;
					case 112:
						{}
					case -112:
						break;
					case 114:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -113:
						break;
					case 115:
						{
  try{
    return new Symbol(TokenConstants.NUMBER_CONST, Double.parseDouble(yytext()) );
  }catch(Exception e){ return new Symbol(TokenConstants.ERROR, e.toString()); }  
}
					case -114:
						break;
					case 116:
						{
    //splitting this into several rules, one for the format 0.00e+0, next for the format .00e+0
  try{  
    return new Symbol(TokenConstants.NUMBER_CONST, Double.parseDouble(yytext()) ); 
  }catch(Exception e){ return new Symbol(TokenConstants.ERROR, e.toString()); } 
}
					case -115:
						break;
					case 117:
						{
    //this is an error case for to catch all numbers beginning with 0, other than "0", or "0.xxx"
    //ECMA does not specifiy literal octals, so not all browsers support them  
  return new Symbol(TokenConstants.ERROR, "Illegal numeric literal, remove leading zero. Octal literals should not be used.  input: "+yytext() );  
}
					case -116:
						break;
					case 118:
						{ }
					case -117:
						break;
					case 120:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -118:
						break;
					case 121:
						{
  try{
    return new Symbol(TokenConstants.NUMBER_CONST, Double.parseDouble(yytext()) );
  }catch(Exception e){ return new Symbol(TokenConstants.ERROR, e.toString()); }  
}
					case -119:
						break;
					case 122:
						{
    //this is an error case for to catch all numbers beginning with 0, other than "0", or "0.xxx"
    //ECMA does not specifiy literal octals, so not all browsers support them  
  return new Symbol(TokenConstants.ERROR, "Illegal numeric literal, remove leading zero. Octal literals should not be used.  input: "+yytext() );  
}
					case -120:
						break;
					case 124:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -121:
						break;
					case 126:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -122:
						break;
					case 128:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -123:
						break;
					case 130:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -124:
						break;
					case 132:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -125:
						break;
					case 134:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -126:
						break;
					case 136:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -127:
						break;
					case 138:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -128:
						break;
					case 140:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -129:
						break;
					case 142:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -130:
						break;
					case 144:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -131:
						break;
					case 145:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -132:
						break;
					case 146:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -133:
						break;
					case 147:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -134:
						break;
					case 148:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -135:
						break;
					case 149:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -136:
						break;
					case 150:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -137:
						break;
					case 151:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -138:
						break;
					case 152:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -139:
						break;
					case 153:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -140:
						break;
					case 154:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -141:
						break;
					case 155:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -142:
						break;
					case 156:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -143:
						break;
					case 157:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -144:
						break;
					case 158:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -145:
						break;
					case 159:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -146:
						break;
					case 160:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -147:
						break;
					case 161:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -148:
						break;
					case 162:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -149:
						break;
					case 163:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -150:
						break;
					case 164:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -151:
						break;
					case 165:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -152:
						break;
					case 166:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -153:
						break;
					case 167:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -154:
						break;
					case 168:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -155:
						break;
					case 169:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -156:
						break;
					case 170:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -157:
						break;
					case 171:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -158:
						break;
					case 172:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -159:
						break;
					case 173:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -160:
						break;
					case 174:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -161:
						break;
					case 175:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -162:
						break;
					case 176:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -163:
						break;
					case 177:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -164:
						break;
					case 178:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -165:
						break;
					case 179:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -166:
						break;
					case 180:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -167:
						break;
					case 181:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -168:
						break;
					case 182:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -169:
						break;
					case 183:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -170:
						break;
					case 184:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -171:
						break;
					case 185:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -172:
						break;
					case 186:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -173:
						break;
					case 187:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -174:
						break;
					case 188:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -175:
						break;
					case 189:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -176:
						break;
					case 190:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -177:
						break;
					case 191:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -178:
						break;
					case 192:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -179:
						break;
					case 193:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -180:
						break;
					case 194:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -181:
						break;
					case 195:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -182:
						break;
					case 196:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -183:
						break;
					case 197:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -184:
						break;
					case 198:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -185:
						break;
					case 199:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -186:
						break;
					case 200:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -187:
						break;
					case 201:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -188:
						break;
					case 202:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -189:
						break;
					case 203:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -190:
						break;
					case 204:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -191:
						break;
					case 205:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -192:
						break;
					case 206:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -193:
						break;
					case 207:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -194:
						break;
					case 208:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -195:
						break;
					case 209:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -196:
						break;
					case 210:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -197:
						break;
					case 211:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -198:
						break;
					case 212:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -199:
						break;
					case 213:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -200:
						break;
					case 214:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -201:
						break;
					case 215:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -202:
						break;
					case 216:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -203:
						break;
					case 217:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -204:
						break;
					case 218:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -205:
						break;
					case 219:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -206:
						break;
					case 220:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -207:
						break;
					case 221:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -208:
						break;
					case 222:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -209:
						break;
					case 223:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -210:
						break;
					case 224:
						{
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}
					case -211:
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
