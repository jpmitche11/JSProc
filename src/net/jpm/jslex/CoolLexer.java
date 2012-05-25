package net.jpm.jslex;
/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

    //Level of comment nesting. 0 if outside of a comment, 1 if inside a comment, 2 if inside a nested comment ....
    private int commentNesting = 0;
    //holds string contents;
    private StringBuffer stringData = null;
    private int maxStringLength = 1024;
    private String errStr;
    private StringTable stringTable = AbstractTable.stringtable;
    private IdTable idTable = AbstractTable.idtable;
    private IntTable intTable = AbstractTable.inttable;
    public int get_curr_lineno() {
       return yyline+1;
    }
    private AbstractSymbol filename;
    public void set_filename(String fname) {
        filename = AbstractTable.stringtable.addString(fname);
    }
    public AbstractSymbol curr_filename() {
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

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
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
		63,
		86,
		90
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
		/* 63 */ YY_NOT_ACCEPT,
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
		/* 86 */ YY_NOT_ACCEPT,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NOT_ACCEPT,
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
		/* 101 */ YY_NO_ANCHOR,
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
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
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
		/* 172 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"9,10:8,45,4,45:2,5,10:18,45,10,7,10:5,1,3,2,15,21,6,20,16,61:10,23,22,19,18" +
",27,10,24,40,63,39,43,32,29,63,31,28,63:2,34,63,33,37,38,63,41,35,30,44,42," +
"36,63:3,10,8,10:2,62,10,46,12,47,48,49,14,50,51,52,50:2,53,50,11,54,55,50,5" +
"6,57,13,58,59,60,50:3,25,10,26,17,10,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,173,
"0,1,2,3,1,4,5,1:2,6,1:3,7,8,1:7,9,10,1:2,11,12,1:3,13,14:2,12:2,14,12:2,14:" +
"5,12,14:4,2,1:13,15,16,17,14,18,12:2,14:2,12,14,12:9,19,20,21,22,23,24,1,25" +
",26,27,28,29,30,31,12,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49" +
",50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,12,69,70,71,72,73" +
",74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98" +
",99,14,100,101,102,103,104")[0];

	private int yy_nxt[][] = unpackFromString(105,64,
"1,2,3,4,5:2,6,7,8:3,9,135,141,64,10,11,12,13,14,15,16,17,18,19,20,21,8,22,6" +
"5,88,167,168,136,142,167,169,92,170,171,167:5,5,135,145,135,148,135:2,87,15" +
"0,91,152,135:4,154,23,8,167,-1:66,24,-1:64,25,-1:64,5:2,-1:39,5,-1:24,26,-1" +
":68,135:4,-1:13,135:4,93,135:4,95,135:7,-1,135:3,93,135:4,95,135:7,97,135,-" +
"1:27,28,-1:42,29,-1:11,30,-1:56,31,167:2,32,-1:13,167,32,167:3,31,167,172,1" +
"67:9,-1,167:11,172,167:6,-1:61,23,-1:3,26:3,-1,26:59,-1:11,135:4,-1:13,135:" +
"17,-1,135:16,97,135,-1:11,167:4,-1:13,167:3,159,167:13,-1,167:5,159,167:12," +
"-1:11,167:4,-1:13,167:17,-1,167:18,1,49,83,89:61,-1:11,135:4,-1:13,27,135:1" +
"1,103,135:4,-1,103,135:5,27,135:9,97,135,-1:11,167:4,-1:13,66,167:16,-1,167" +
":6,66,167:11,-1:11,135:4,-1:13,135:3,123,135:13,-1,135:5,123,135:10,97,135," +
"-1:3,50,-1:61,55:8,56,55,57,58,59,60,55:49,-1:4,61,-1:59,1,51:3,52,51:2,53," +
"84,54,51:54,-1:11,67,135:2,68,-1:13,135,68,135:3,67,135,109,135:9,-1,135:11" +
",109,135:4,97,135,-1:11,167:4,-1:13,167:3,146,167:13,-1,167:5,146,167:12,1," +
"61:3,62,8,61,62,85,61:55,-1:11,135:3,69,-1:13,135,69,135:15,-1,135:16,97,13" +
"5,-1:11,167:3,33,-1:13,167,33,167:15,-1,167:18,-1:11,135:4,-1:13,135:8,34,1" +
"35:8,-1,135:14,34,135,97,135,-1:11,167:4,-1:13,167:8,70,167:8,-1,167:14,70," +
"167:3,-1:11,135:2,35,135,-1:13,135:2,35,135:14,-1,135:16,97,135,-1:11,167:2" +
",71,167,-1:13,167:2,71,167:14,-1,167:18,-1:11,167:2,36,167,-1:13,167:2,36,1" +
"67:14,-1,167:18,-1:11,135:4,-1:13,135:4,117,135:12,-1,135:3,117,135:12,97,1" +
"35,-1:11,73,167:3,-1:13,167:5,73,167:11,-1,167:18,-1:11,135:4,-1:13,135:16," +
"119,-1,135:12,119,135:3,97,135,-1:11,167:4,-1:13,167:4,39,167:12,-1,167:3,3" +
"9,167:14,-1:11,135:4,-1:13,135:6,143,135:10,-1,135:7,143,135:8,97,135,-1:11" +
",167:4,-1:13,167:11,40,167:5,-1,167,40,167:16,-1:11,135:4,-1:13,135:12,147," +
"135:4,-1,147,135:15,97,135,-1:11,167:4,-1:13,167:10,41,167:6,-1,167:9,41,16" +
"7:8,-1:11,135:4,-1:13,135:7,120,135:9,-1,135:11,120,135:4,97,135,-1:11,167:" +
"4,-1:13,167:6,42,167:10,-1,167:7,42,167:10,-1:11,135:4,-1:13,135:14,144,135" +
":2,-1,135:13,144,135:2,97,135,-1:11,167:4,-1:13,167:4,43,167:12,-1,167:3,43" +
",167:14,-1:11,135:2,72,135,-1:13,135:2,72,135:14,-1,135:16,97,135,-1:11,167" +
":4,-1:13,167:4,45,167:12,-1,167:3,45,167:14,-1:11,135:4,-1:13,135:9,124,135" +
":7,-1,135:8,124,135:7,97,135,-1:11,167:4,-1:13,167:7,46,167:9,-1,167:11,46," +
"167:6,-1:11,135:4,-1:13,126,135:16,-1,135:6,126,135:9,97,135,-1:11,167:4,-1" +
":13,167:15,47,167,-1,167:2,47,167:15,-1:11,37,135:3,-1:13,135:5,37,135:11,-" +
"1,135:16,97,135,-1:11,167:4,-1:13,167:7,48,167:9,-1,167:11,48,167:6,-1:11,1" +
"35:4,-1:13,135:4,38,135:12,-1,135:3,38,135:12,97,135,-1:11,135:4,-1:13,135:" +
"4,78,135:12,-1,135:3,78,135:12,97,135,-1:11,135:4,-1:13,135:4,74,135:12,-1," +
"135:3,74,135:12,97,135,-1:11,135:4,-1:13,135:11,75,135:5,-1,135,75,135:14,9" +
"7,135,-1:11,135:4,-1:13,135:4,129,135:12,-1,135:3,129,135:12,97,135,-1:11,1" +
"35:4,-1:13,135:10,76,135:6,-1,135:9,76,135:6,97,135,-1:11,135:4,-1:13,135:6" +
",77,135:10,-1,135:7,77,135:8,97,135,-1:11,135:4,-1:13,135:6,131,135:10,-1,1" +
"35:7,131,135:8,97,135,-1:11,135:4,-1:13,135:4,44,135:12,-1,135:3,44,135:12," +
"97,135,-1:11,135:4,-1:13,135:7,80,135:9,-1,135:11,80,135:4,97,135,-1:11,135" +
":4,-1:13,135:13,140,135:3,-1,135:10,140,135:5,97,135,-1:11,135:4,-1:13,132," +
"135:16,-1,135:6,132,135:9,97,135,-1:11,135:4,-1:13,135:4,79,135:12,-1,135:3" +
",79,135:12,97,135,-1:11,135:4,-1:13,135:15,81,135,-1,135:2,81,135:13,97,135" +
",-1:11,135:2,134,135,-1:13,135:2,134,135:14,-1,135:16,97,135,-1:11,135:4,-1" +
":13,135:7,82,135:9,-1,135:11,82,135:4,97,135,-1:11,167:4,-1:13,167:4,94,167" +
":4,96,167:7,-1,167:3,94,167:4,96,167:9,-1:11,135:4,-1:13,135:12,122,135:4,-" +
"1,122,135:15,97,135,-1:11,135:4,-1:13,135:7,121,135:9,-1,135:11,121,135:4,9" +
"7,135,-1:11,135:4,-1:13,135:9,125,135:7,-1,135:8,125,135:7,97,135,-1:11,135" +
":4,-1:13,133,135:16,-1,135:6,133,135:9,97,135,-1:11,135:4,-1:13,135:3,99,13" +
"5:9,101,135:3,-1,135:5,99,135:4,101,135:5,97,135,-1:11,167:4,-1:13,167:4,98" +
",167:4,153,167:7,-1,167:3,98,167:4,153,167:9,-1:11,135:4,-1:13,135:7,127,13" +
"5:9,-1,135:11,127,135:4,97,135,-1:11,135:4,-1:13,135:9,130,135:7,-1,135:8,1" +
"30,135:7,97,135,-1:11,135:4,-1:13,135:6,105,135:5,107,135:4,-1,107,135:6,10" +
"5,135:8,97,135,-1:11,167:4,-1:13,167:4,100,167:12,-1,167:3,100,167:14,-1:11" +
",135:4,-1:13,135:7,128,135:9,-1,135:11,128,135:4,97,135,-1:11,135:4,-1:13,1" +
"35:6,138,137,135:9,-1,135:7,138,135:3,137,135:4,97,135,-1:11,167:4,-1:13,16" +
"7:7,102,167:9,-1,167:11,102,167:6,-1:11,135:4,-1:13,135:4,111,135:4,113,135" +
":7,-1,135:3,111,135:4,113,135:7,97,135,-1:11,167:4,-1:13,167:12,104,167:4,-" +
"1,104,167:17,-1:11,135:4,-1:13,135:9,139,135:7,-1,135:8,139,135:7,97,135,-1" +
":11,167:4,-1:13,167:9,106,167:7,-1,167:8,106,167:9,-1:11,135:4,-1:13,135:3," +
"115,135:13,-1,135:5,115,135:10,97,135,-1:11,167:4,-1:13,161,167:16,-1,167:6" +
",161,167:11,-1:11,167:4,-1:13,167:9,108,167:7,-1,167:8,108,167:9,-1:11,167:" +
"4,-1:13,167:12,162,167:4,-1,162,167:17,-1:11,167:4,-1:13,167:7,110,167:9,-1" +
",167:11,110,167:6,-1:11,167:4,-1:13,167:4,163,167:12,-1,167:3,163,167:14,-1" +
":11,167:4,-1:13,167:9,164,167:7,-1,167:8,164,167:9,-1:11,167:4,-1:13,167:6," +
"112,167:10,-1,167:7,112,167:10,-1:11,167:4,-1:13,167:7,114,167:9,-1,167:11," +
"114,167:6,-1:11,167:4,-1:13,167:13,165,167:3,-1,167:10,165,167:7,-1:11,167:" +
"4,-1:13,116,167:16,-1,167:6,116,167:11,-1:11,167:4,-1:13,166,167:16,-1,167:" +
"6,166,167:11,-1:11,167:2,118,167,-1:13,167:2,118,167:14,-1,167:18,-1:11,167" +
":4,-1:13,167:6,149,151,167:9,-1,167:7,149,167:3,151,167:6,-1:11,167:4,-1:13" +
",167:3,155,167:13,-1,167:5,155,167:12,-1:11,167:4,-1:13,167:9,156,167:7,-1," +
"167:8,156,167:9,-1:11,167:4,-1:13,167:6,157,167:5,158,167:4,-1,158,167:6,15" +
"7,167:10,-1:11,167:4,-1:13,167:14,160,167:2,-1,167:13,160,167:4");

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
						{ return new Symbol(TokenConstants.LPAREN); }
					case -3:
						break;
					case 3:
						{ return new Symbol(TokenConstants.MULT); }
					case -4:
						break;
					case 4:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -5:
						break;
					case 5:
						{  
  //  System.out.println("Whitespace"); 
}
					case -6:
						break;
					case 6:
						{ return new Symbol(TokenConstants.MINUS); }
					case -7:
						break;
					case 7:
						{
    yybegin(STRING);
    stringData = new StringBuffer();
}
					case -8:
						break;
					case 8:
						{ //Catchall rule to handle unknown characters 
   return new Symbol(TokenConstants.ERROR, yytext());
}
					case -9:
						break;
					case 9:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -10:
						break;
					case 10:
						{ return new Symbol(TokenConstants.PLUS); }
					case -11:
						break;
					case 11:
						{ return new Symbol(TokenConstants.DIV); }
					case -12:
						break;
					case 12:
						{ return new Symbol(TokenConstants.NEG); }
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.EQ); }
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.LT); }
					case -15:
						break;
					case 15:
						{ return new Symbol(TokenConstants.DOT); }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.COMMA); }
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.SEMI); }
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.COLON); }
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
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -23:
						break;
					case 23:
						{
  return new Symbol(TokenConstants.INT_CONST, this.intTable.addString(yytext()) );  
}
					case -24:
						break;
					case 24:
						{
//Comments
    this.commentNesting++;
    yybegin(COMMENT);   
}
					case -25:
						break;
					case 25:
						{
    return new Symbol(TokenConstants.ERROR, "Unmatched *)" );
}
					case -26:
						break;
					case 26:
						{ 
    //Line Comment
}
					case -27:
						break;
					case 27:
						{    return new Symbol(TokenConstants.FI);  }
					case -28:
						break;
					case 28:
						{ return new Symbol(TokenConstants.DARROW); }
					case -29:
						break;
					case 29:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -30:
						break;
					case 30:
						{ return new Symbol(TokenConstants.LE); }
					case -31:
						break;
					case 31:
						{    return new Symbol(TokenConstants.IN);  }
					case -32:
						break;
					case 32:
						{    return new Symbol(TokenConstants.IF);  }
					case -33:
						break;
					case 33:
						{    return new Symbol(TokenConstants.OF);  }
					case -34:
						break;
					case 34:
						{    return new Symbol(TokenConstants.NEW);  }
					case -35:
						break;
					case 35:
						{    return new Symbol(TokenConstants.NOT);  }
					case -36:
						break;
					case 36:
						{    return new Symbol(TokenConstants.LET);  }
					case -37:
						break;
					case 37:
						{    return new Symbol(TokenConstants.THEN);  }
					case -38:
						break;
					case 38:
						{    return new Symbol(TokenConstants.BOOL_CONST, true);  }
					case -39:
						break;
					case 39:
						{    return new Symbol(TokenConstants.ELSE);  }
					case -40:
						break;
					case 40:
						{    return new Symbol(TokenConstants.ESAC);  }
					case -41:
						break;
					case 41:
						{    return new Symbol(TokenConstants.LOOP);  }
					case -42:
						break;
					case 42:
						{    return new Symbol(TokenConstants.POOL);  }
					case -43:
						break;
					case 43:
						{    return new Symbol(TokenConstants.CASE);  }
					case -44:
						break;
					case 44:
						{    return new Symbol(TokenConstants.BOOL_CONST, false);  }
					case -45:
						break;
					case 45:
						{    return new Symbol(TokenConstants.WHILE);  }
					case -46:
						break;
					case 46:
						{    return new Symbol(TokenConstants.CLASS);  }
					case -47:
						break;
					case 47:
						{    return new Symbol(TokenConstants.ISVOID);  }
					case -48:
						break;
					case 48:
						{    return new Symbol(TokenConstants.INHERITS);  }
					case -49:
						break;
					case 49:
						{ }
					case -50:
						break;
					case 50:
						{
    this.commentNesting--;
    if(this.commentNesting == 0){
        yybegin(YYINITIAL);      
    }
}
					case -51:
						break;
					case 51:
						{
    stringData.append(yytext());
}
					case -52:
						break;
					case 52:
						{
    yybegin(YYINITIAL);
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -53:
						break;
					case 53:
						{
    yybegin(YYINITIAL);
    if(stringData.length() > maxStringLength){
        return new Symbol(TokenConstants.ERROR, "String constant too long");
    }
    return new Symbol(TokenConstants.STR_CONST, this.stringTable.addString( stringData.toString() ) );  
}
					case -54:
						break;
					case 54:
						{ yybegin(STRING_NULL); errStr = "String contains null character"; }
					case -55:
						break;
					case 55:
						{
    //escape sequence
    stringData.append(yytext().substring(1));
}
					case -56:
						break;
					case 56:
						{ yybegin(STRING_NULL); errStr = "String contains escaped null character"; }
					case -57:
						break;
					case 57:
						{
    stringData.append("\n");
}
					case -58:
						break;
					case 58:
						{
    stringData.append("\b");
}
					case -59:
						break;
					case 59:
						{
    stringData.append("\t");
}
					case -60:
						break;
					case 60:
						{
    stringData.append("\f");
}
					case -61:
						break;
					case 61:
						{}
					case -62:
						break;
					case 62:
						{
     yybegin(YYINITIAL);
     return new Symbol(TokenConstants.ERROR, errStr);     
}
					case -63:
						break;
					case 64:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -64:
						break;
					case 65:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -65:
						break;
					case 66:
						{    return new Symbol(TokenConstants.FI);  }
					case -66:
						break;
					case 67:
						{    return new Symbol(TokenConstants.IN);  }
					case -67:
						break;
					case 68:
						{    return new Symbol(TokenConstants.IF);  }
					case -68:
						break;
					case 69:
						{    return new Symbol(TokenConstants.OF);  }
					case -69:
						break;
					case 70:
						{    return new Symbol(TokenConstants.NEW);  }
					case -70:
						break;
					case 71:
						{    return new Symbol(TokenConstants.NOT);  }
					case -71:
						break;
					case 72:
						{    return new Symbol(TokenConstants.LET);  }
					case -72:
						break;
					case 73:
						{    return new Symbol(TokenConstants.THEN);  }
					case -73:
						break;
					case 74:
						{    return new Symbol(TokenConstants.ELSE);  }
					case -74:
						break;
					case 75:
						{    return new Symbol(TokenConstants.ESAC);  }
					case -75:
						break;
					case 76:
						{    return new Symbol(TokenConstants.LOOP);  }
					case -76:
						break;
					case 77:
						{    return new Symbol(TokenConstants.POOL);  }
					case -77:
						break;
					case 78:
						{    return new Symbol(TokenConstants.CASE);  }
					case -78:
						break;
					case 79:
						{    return new Symbol(TokenConstants.WHILE);  }
					case -79:
						break;
					case 80:
						{    return new Symbol(TokenConstants.CLASS);  }
					case -80:
						break;
					case 81:
						{    return new Symbol(TokenConstants.ISVOID);  }
					case -81:
						break;
					case 82:
						{    return new Symbol(TokenConstants.INHERITS);  }
					case -82:
						break;
					case 83:
						{ }
					case -83:
						break;
					case 84:
						{
    stringData.append(yytext());
}
					case -84:
						break;
					case 85:
						{}
					case -85:
						break;
					case 87:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -86:
						break;
					case 88:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -87:
						break;
					case 89:
						{ }
					case -88:
						break;
					case 91:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -89:
						break;
					case 92:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -90:
						break;
					case 93:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -91:
						break;
					case 94:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -92:
						break;
					case 95:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -93:
						break;
					case 96:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -94:
						break;
					case 97:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -95:
						break;
					case 98:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -96:
						break;
					case 99:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -97:
						break;
					case 100:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -98:
						break;
					case 101:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -99:
						break;
					case 102:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -100:
						break;
					case 103:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -101:
						break;
					case 104:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -102:
						break;
					case 105:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -103:
						break;
					case 106:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -104:
						break;
					case 107:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -105:
						break;
					case 108:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -106:
						break;
					case 109:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -107:
						break;
					case 110:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -108:
						break;
					case 111:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -109:
						break;
					case 112:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -110:
						break;
					case 113:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -111:
						break;
					case 114:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -112:
						break;
					case 115:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -113:
						break;
					case 116:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -114:
						break;
					case 117:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -115:
						break;
					case 118:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -116:
						break;
					case 119:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -117:
						break;
					case 120:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -118:
						break;
					case 121:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -119:
						break;
					case 122:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -120:
						break;
					case 123:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -121:
						break;
					case 124:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -122:
						break;
					case 125:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -123:
						break;
					case 126:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -124:
						break;
					case 127:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -125:
						break;
					case 128:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -126:
						break;
					case 129:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -127:
						break;
					case 130:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -128:
						break;
					case 131:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -129:
						break;
					case 132:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -130:
						break;
					case 133:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -131:
						break;
					case 134:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -132:
						break;
					case 135:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -133:
						break;
					case 136:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -134:
						break;
					case 137:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -135:
						break;
					case 138:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -136:
						break;
					case 139:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -137:
						break;
					case 140:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -138:
						break;
					case 141:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -139:
						break;
					case 142:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -140:
						break;
					case 143:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -141:
						break;
					case 144:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -142:
						break;
					case 145:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -143:
						break;
					case 146:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -144:
						break;
					case 147:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -145:
						break;
					case 148:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -146:
						break;
					case 149:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -147:
						break;
					case 150:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -148:
						break;
					case 151:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -149:
						break;
					case 152:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -150:
						break;
					case 153:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -151:
						break;
					case 154:
						{
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}
					case -152:
						break;
					case 155:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -153:
						break;
					case 156:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -154:
						break;
					case 157:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -155:
						break;
					case 158:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -156:
						break;
					case 159:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -157:
						break;
					case 160:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -158:
						break;
					case 161:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -159:
						break;
					case 162:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -160:
						break;
					case 163:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -161:
						break;
					case 164:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -162:
						break;
					case 165:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -163:
						break;
					case 166:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -164:
						break;
					case 167:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -165:
						break;
					case 168:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -166:
						break;
					case 169:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -167:
						break;
					case 170:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -168:
						break;
					case 171:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -169:
						break;
					case 172:
						{
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}
					case -170:
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
