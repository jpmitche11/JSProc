/*
 *  The scanner definition for COOL.
 */

package net.jpm.jslex;
import java_cup.runtime.Symbol;


%%

%{

    
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
LINE = (\r\n)|(\n)
%%


<YYINITIAL, COMMENT>"/*" {
//Comments
    yybegin(COMMENT);   
}

<COMMENT>"*/" {
    yybegin(YYINITIAL);
}
<COMMENT>\n|[^\n] { } 

<YYINITIAL>"*/" {
    return new Symbol(TokenConstants.ERROR, "Unmatched Comment end */" );
}

<YYINITIAL>//[^\n]* { 
    //Line Comment
}


<YYINITIAL>\"|\' {
    yybegin(STRING);
    stringQuote = yytext();
    
   // System.out.println("Begin String: "+stringQuote);
    stringData = new StringBuffer();
}

<STRING>\"|\' {    
   // System.out.println("Process string quote: "+yytext());
    if(stringQuote.equals(yytext())){
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.STR_CONST, this.stringTable.addString( stringData.toString() ) );
    }
    else{
        stringData.append(yytext());
    }
}

<STRING>\000 { yybegin(STRING_NULL); errStr = "String contains null character"; }
<STRING_NULL>\"|\'|\n {
     yybegin(YYINITIAL);
     return new Symbol(TokenConstants.ERROR, errStr);     
}
<STRING_NULL>.|\\\n {}


<STRING>\r|\n {
    yybegin(YYINITIAL);
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}


<STRING>"\n" {
    stringData.append("\n");
}
<STRING>"\r" {
    stringData.append("\r");
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
<STRING>\013 {
   // stringData.append("\v");
   return new Symbol(TokenConstants.ERROR, "Vertical tab in string"); 
}

<STRING>[^\n\r] {
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



<YYINITIAL>if {    return new Symbol(TokenConstants.IF);  }

<YYINITIAL>(" "|\t|\r|\f|\n|\013)+ {  
  //  System.out.println("Whitespace"); 
}

<YYINITIAL>[a-z]({ALPHANUM}|_)* {
  return new Symbol(TokenConstants.OBJECTID, this.stringTable.addString(yytext()) );  
}

<YYINITIAL>[A-Z]({ALPHANUM}|_)* {
  return new Symbol(TokenConstants.TYPEID, this.stringTable.addString(yytext()) );  
}

<YYINITIAL>{DIGIT}+ {
  return new Symbol(TokenConstants.INT_CONST, this.stringTable.addString(yytext()) );  
}



\n|[^\n] { //Catchall rule to handle unknown characters 
   return new Symbol(TokenConstants.ERROR, yytext());
}

