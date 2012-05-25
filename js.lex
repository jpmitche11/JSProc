/*
 *  The scanner definition for COOL.
 */

package net.jpm.jslex;
import java_cup.runtime.Symbol;

%%

%{

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
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

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
                
%eofval}

%class JSLexer

%cup
%line

%state COMMENT
%state STRING
%state STRING_NULL

LETTER = [a-zA-Z]
DIGIT = [0-9]
ALPHANUM = [a-zA-Z0-9]
%%


<YYINITIAL, COMMENT>"(*" {
//Comments
    this.commentNesting++;
    yybegin(COMMENT);   
}

<COMMENT>"*)" {
    this.commentNesting--;
    if(this.commentNesting == 0){
        yybegin(YYINITIAL);      
    }
}
<COMMENT>\n|[^\n] { } 

<YYINITIAL>"*)" {
    return new Symbol(TokenConstants.ERROR, "Unmatched *)" );
}


<YYINITIAL>--[^\n]* { 
    //Line Comment
}


<YYINITIAL>\" {
    yybegin(STRING);
    stringData = new StringBuffer();
}

<STRING>\" {
    yybegin(YYINITIAL);
    
    if(stringData.length() > maxStringLength){
        return new Symbol(TokenConstants.ERROR, "String constant too long");
    }
    
    return new Symbol(TokenConstants.STR_CONST, this.stringTable.addString( stringData.toString() ) );  
}


<STRING>\\\000 { yybegin(STRING_NULL); errStr = "String contains escaped null character"; }
<STRING>\000 { yybegin(STRING_NULL); errStr = "String contains null character"; }
<STRING_NULL>\"|\n {
     yybegin(YYINITIAL);
     return new Symbol(TokenConstants.ERROR, errStr);     
}
<STRING_NULL>.|\\\n {}


<STRING>\n {
    yybegin(YYINITIAL);
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}


<STRING>"\n" {
    stringData.append("\n");
}
<STRING>"\b" {
    stringData.append("\b");
}
<STRING>"\t" {
    stringData.append("\t");
}
<STRING>"\f" {
    stringData.append("\f");
}

<STRING>[^\n] {
    stringData.append(yytext());
}

<STRING>\\([^\n]|\n) {
    //escape sequence
    stringData.append(yytext().substring(1));
}









<YYINITIAL>"+"  { return new Symbol(TokenConstants.PLUS); }
<YYINITIAL>"-"  { return new Symbol(TokenConstants.MINUS); } 
<YYINITIAL>"/"  { return new Symbol(TokenConstants.DIV); }  
<YYINITIAL>"*"  { return new Symbol(TokenConstants.MULT); }
<YYINITIAL>"~"  { return new Symbol(TokenConstants.NEG); }  
<YYINITIAL>"="  { return new Symbol(TokenConstants.EQ); } 
<YYINITIAL>"<"  { return new Symbol(TokenConstants.LT); } 
<YYINITIAL>"<="  { return new Symbol(TokenConstants.LE); }
<YYINITIAL>"."  { return new Symbol(TokenConstants.DOT); } 
<YYINITIAL>","  { return new Symbol(TokenConstants.COMMA); } 
<YYINITIAL>";"  { return new Symbol(TokenConstants.SEMI); } 
<YYINITIAL>":"  { return new Symbol(TokenConstants.COLON); } 
<YYINITIAL>"("  { return new Symbol(TokenConstants.LPAREN); } 
<YYINITIAL>")"  { return new Symbol(TokenConstants.RPAREN); } 
<YYINITIAL>"@"  { return new Symbol(TokenConstants.AT); } 
<YYINITIAL>"{"  { return new Symbol(TokenConstants.LBRACE); } 
<YYINITIAL>"}"  { return new Symbol(TokenConstants.RBRACE); } 
<YYINITIAL>"}"  { return new Symbol(TokenConstants.RBRACE); }

<YYINITIAL>"<-"  { return new Symbol(TokenConstants.ASSIGN); }
<YYINITIAL>"=>"  { return new Symbol(TokenConstants.DARROW); }



<YYINITIAL>[iI][fF] {    return new Symbol(TokenConstants.IF);  }
<YYINITIAL>[tT][hH][eE][nN] {    return new Symbol(TokenConstants.THEN);  }
<YYINITIAL>[eE][lL][sS][eE] {    return new Symbol(TokenConstants.ELSE);  }
<YYINITIAL>[fF][iI] {    return new Symbol(TokenConstants.FI);  }

<YYINITIAL>[wW][hH][iI][lL][eE] {    return new Symbol(TokenConstants.WHILE);  }
<YYINITIAL>[lL][oO][oO][pP] {    return new Symbol(TokenConstants.LOOP);  }
<YYINITIAL>[pP][oO][oO][lL] {    return new Symbol(TokenConstants.POOL);  }

<YYINITIAL>[lL][eE][tT] {    return new Symbol(TokenConstants.LET);  }
<YYINITIAL>[iI][nN] {    return new Symbol(TokenConstants.IN);  }

<YYINITIAL>[cC][lL][aA][sS][sS] {    return new Symbol(TokenConstants.CLASS);  }
<YYINITIAL>[iI][nN][hH][eE][rR][iI][Tt][sS] {    return new Symbol(TokenConstants.INHERITS);  }


<YYINITIAL>[cC][aA][sS][eE] {    return new Symbol(TokenConstants.CASE);  }
<YYINITIAL>[oO][fF] {    return new Symbol(TokenConstants.OF);  }
<YYINITIAL>[eE][sS][aA][cC] {    return new Symbol(TokenConstants.ESAC);  }

<YYINITIAL>[iI][sS][vV][oO][iI][dD] {    return new Symbol(TokenConstants.ISVOID);  }
<YYINITIAL>[nN][oO][tT] {    return new Symbol(TokenConstants.NOT);  }
<YYINITIAL>[nN][eE][wW] {    return new Symbol(TokenConstants.NEW);  }

<YYINITIAL>t[rR][uU][eE] {    return new Symbol(TokenConstants.BOOL_CONST, true);  }
<YYINITIAL>f[aA][lL][sS][eE] {    return new Symbol(TokenConstants.BOOL_CONST, false);  }


<YYINITIAL>(" "|\t|\r|\f|\n|\013)+ {  
  //  System.out.println("Whitespace"); 
}

<YYINITIAL>[a-z]({ALPHANUM}|_)* {
  return new Symbol(TokenConstants.OBJECTID, this.idTable.addString(yytext()) );  
}

<YYINITIAL>[A-Z]({ALPHANUM}|_)* {
  return new Symbol(TokenConstants.TYPEID, this.idTable.addString(yytext()) );  
}

<YYINITIAL>{DIGIT}+ {
  return new Symbol(TokenConstants.INT_CONST, this.intTable.addString(yytext()) );  
}



<YYINITIAL>{LETTER}({ALPHANUM}|_)* {
    System.out.println("Identifier: "+yytext());
   // return new Symbol(TokenConstants.CLASS);
}







\n|[^\n] { //Catchall rule to handle unknown characters 
   return new Symbol(TokenConstants.ERROR, yytext());
}

