/**
 * Lexer definition file.
 */
package net.jpm.jsproc.lex;
import java_cup.runtime.Symbol;
import net.jpm.jsproc.cup.TokenConstants;
import net.jpm.jsproc.StringTable;


%%

%{

    
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
                        return new Symbol(TokenConstants.ERROR, "EOF in string literal");                   
                    case REGEXP:
                        yybegin(YYINITIAL); 
                        return new Symbol(TokenConstants.ERROR, "EOF in regexp literal");                   
                                        
                }
                return new Symbol(TokenConstants.EOF);
                
%eofval}
%public
%class JSLexer

%cup
%line

%state COMMENT
%state STRING
%state STRING_ERR
%state REGEXP
%state REGEXP_ERR

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
    charData = new StringBuffer();
}

<STRING>\"|\' {    
   // System.out.println("Process string quote: "+yytext());
    if(stringQuote.equals(yytext())){
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.STRING_CONST, this.stringTable.addString( charData.toString() ) );
    }
    else{
        charData.append(yytext());
    }
}

<STRING>\000 { yybegin(STRING_ERR); errStr = "String contains null character"; }
<STRING_ERR>\"|\'|\n {
     yybegin(YYINITIAL);
     return new Symbol(TokenConstants.ERROR, errStr);     
}
<STRING_ERR>.|\\\n {}


<STRING>\r|\n {
    yybegin(YYINITIAL);
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}


<STRING>"\n"|"\r"|"\b"|"\t"|"f"|"\013" {
    charData.append(yytext());
}
<STRING>[^\n\r] {
    charData.append(yytext());
}
<STRING>\\([^\n\r]) {
    //escape sequence
    charData.append(yytext().substring(1));
}





<YYINITIAL>function {  return new Symbol(TokenConstants.FUNCTION); }
<YYINITIAL>var {  return new Symbol(TokenConstants.VAR); }
<YYINITIAL>return {  return new Symbol(TokenConstants.RETURN); }
<YYINITIAL>this {  return new Symbol(TokenConstants.THIS); }

<YYINITIAL>if {  return new Symbol(TokenConstants.IF); }
<YYINITIAL>else {  return new Symbol(TokenConstants.ELSE); }

<YYINITIAL>switch {  return new Symbol(TokenConstants.SWITCH); }
<YYINITIAL>case {  return new Symbol(TokenConstants.CASE); }
<YYINITIAL>default {  return new Symbol(TokenConstants.DEFAULT); }
<YYINITIAL>new {  return new Symbol(TokenConstants.NEW); }
<YYINITIAL>delete {  return new Symbol(TokenConstants.DELETE); }

<YYINITIAL>try {  return new Symbol(TokenConstants.TRY); }
<YYINITIAL>catch {  return new Symbol(TokenConstants.CATCH); }
<YYINITIAL>finally {  return new Symbol(TokenConstants.FINALLY); }
<YYINITIAL>throw {  return new Symbol(TokenConstants.THROW); }

<YYINITIAL>for {  return new Symbol(TokenConstants.FOR); }
<YYINITIAL>in {  return new Symbol(TokenConstants.IN); }
<YYINITIAL>do {  return new Symbol(TokenConstants.DO); }
<YYINITIAL>while {  return new Symbol(TokenConstants.WHILE); }
<YYINITIAL>break {  return new Symbol(TokenConstants.BREAK); }
<YYINITIAL>continue {  return new Symbol(TokenConstants.CONTINUE); }

<YYINITIAL>null {  return new Symbol(TokenConstants.NULL); }
<YYINITIAL>undefined {  return new Symbol(TokenConstants.UNDEFINED); }
<YYINITIAL>true {  return new Symbol(TokenConstants.TRUE); }
<YYINITIAL>false {  return new Symbol(TokenConstants.FALSE); }

<YYINITIAL>instanceof {  return new Symbol(TokenConstants.INSTANCEOF); }
<YYINITIAL>typeof {  return new Symbol(TokenConstants.TYPEOF); }
<YYINITIAL>void {  return new Symbol(TokenConstants.VOID); }
<YYINITIAL>with {  return new Symbol(TokenConstants.WITH); }



<YYINITIAL>"?"  { return new Symbol(TokenConstants.CONDITIONAL); }

<YYINITIAL>"++"  { return new Symbol(TokenConstants.PLUSPLUS); }
<YYINITIAL>"--"  { return new Symbol(TokenConstants.MINUSMINUS); }

<YYINITIAL>"+="  { return new Symbol(TokenConstants.PLUS_EQ); }
<YYINITIAL>"-="  { return new Symbol(TokenConstants.MINUS_EQ); }
<YYINITIAL>"*="  { return new Symbol(TokenConstants.MULT_EQ); }
<YYINITIAL>"\="  { return new Symbol(TokenConstants.DIV_EQ); }
<YYINITIAL>"%="  { return new Symbol(TokenConstants.MINUSMINUS); }
<YYINITIAL>"&="  { return new Symbol(TokenConstants.AND_EQ); }
<YYINITIAL>"|="  { return new Symbol(TokenConstants.OR_EQ); }
<YYINITIAL>"^="  { return new Symbol(TokenConstants.XOR_EQ); }

<YYINITIAL>">>>="  { return new Symbol(TokenConstants.RSHIFT_ZERO_EQ); }
<YYINITIAL>">>="  { return new Symbol(TokenConstants.RSHIFT_EQ); }
<YYINITIAL>"<<="  { return new Symbol(TokenConstants.LSHIFT_EQ); }

<YYINITIAL>"+"  { return new Symbol(TokenConstants.PLUS); }
<YYINITIAL>"-"  { return new Symbol(TokenConstants.MINUS); } 
<YYINITIAL>"/"  { return new Symbol(TokenConstants.DIV); }  
<YYINITIAL>"*"  { return new Symbol(TokenConstants.MULT); }
<YYINITIAL>"%"  { return new Symbol(TokenConstants.MOD); }

<YYINITIAL>"&&"  { return new Symbol(TokenConstants.AND); }
<YYINITIAL>"||"  { return new Symbol(TokenConstants.OR); }
<YYINITIAL>"!"  { return new Symbol(TokenConstants.NOT); }

<YYINITIAL>"&"  { return new Symbol(TokenConstants.BIN_AND); }
<YYINITIAL>"|"  { return new Symbol(TokenConstants.BIN_OR); }
<YYINITIAL>"~"  { return new Symbol(TokenConstants.BIN_NOT); }
<YYINITIAL>"^"  { return new Symbol(TokenConstants.BIN_XOR); }


<YYINITIAL>"==="  { return new Symbol(TokenConstants.EQQ); } 
<YYINITIAL>"!=="  { return new Symbol(TokenConstants.NEQQ); }
<YYINITIAL>"=="  { return new Symbol(TokenConstants.EQ); } 
<YYINITIAL>"!="  { return new Symbol(TokenConstants.NEQ); } 
<YYINITIAL>"="  { return new Symbol(TokenConstants.ASSIGN); } 

<YYINITIAL>">>>"  { return new Symbol(TokenConstants.RSHIFT_ZERO); }
<YYINITIAL>">>"  { return new Symbol(TokenConstants.RSHIFT); }
<YYINITIAL>"<<"  { return new Symbol(TokenConstants.LSHIFT); }

<YYINITIAL>"<="  { return new Symbol(TokenConstants.LE); }
<YYINITIAL>"<"  { return new Symbol(TokenConstants.LT); } 
<YYINITIAL>">="  { return new Symbol(TokenConstants.GE); }
<YYINITIAL>">"  { return new Symbol(TokenConstants.GT); }


<YYINITIAL>"."  { return new Symbol(TokenConstants.DOT); } 
<YYINITIAL>","  { return new Symbol(TokenConstants.COMMA); } 
<YYINITIAL>";"  { return new Symbol(TokenConstants.SEMI); } 
<YYINITIAL>":"  { return new Symbol(TokenConstants.COLON); } 
<YYINITIAL>"("  { return new Symbol(TokenConstants.LPAREN); } 
<YYINITIAL>")"  { return new Symbol(TokenConstants.RPAREN); } 

<YYINITIAL>"{"  { return new Symbol(TokenConstants.LBRACE); } 
<YYINITIAL>"}"  { return new Symbol(TokenConstants.RBRACE); } 
<YYINITIAL>"["  { return new Symbol(TokenConstants.LBRACKET); } 
<YYINITIAL>"]"  { return new Symbol(TokenConstants.RBRACKET); } 

<YYINITIAL>/([^/\n\r]|(\\[^\n\r]))+/[igm]* {  return new Symbol(TokenConstants.REGEXP_CONST, this.stringTable.addString(yytext()) ); }


<YYINITIAL>(" "|\t|\r|\f|\n|\013)+ {  
  //  System.out.println("Whitespace"); 
}


<YYINITIAL>[a-zA-Z_$]([a-zA-Z0-9_$])* {
  return new Symbol(TokenConstants.ID, this.stringTable.addString(yytext()) );  
}





<YYINITIAL>0[xX][0-9A-Fa-f] {
    //hexidecimal number
    try{
        return new Symbol(TokenConstants.NUMBER_CONST, Long.parseLong(yytext().substring(2), 16)) ;
    }catch(Exception e){ return new Symbol(TokenConstants.ERROR, e.toString()); }      
}

<YYINITIAL>[1-9][0-9]*(\.[0-9]*)?([eE][+-]?[0-9]+)? {
    //splitting this into several rules, one for the format 0.00e+0, next for the format .00e+0
  try{  
    return new Symbol(TokenConstants.NUMBER_CONST, Double.parseDouble(yytext()) ); 
  }catch(Exception e){ return new Symbol(TokenConstants.ERROR, e.toString()); } 
}

<YYINITIAL>0(\.[0-9]*)?([eE][+-]?[0-9+])? {
  try{
    return new Symbol(TokenConstants.NUMBER_CONST, Double.parseDouble(yytext()) );
  }catch(Exception e){ return new Symbol(TokenConstants.ERROR, e.toString()); }  
}
<YYINITIAL>0[0-9]*(\.[0-9]*)?([eE][+-]?[0-9+])? {
    //this is an error case for to catch all numbers beginning with 0, other than "0", or "0.xxx"
    //ECMA does not specifiy literal octals, so not all browsers support them  
  return new Symbol(TokenConstants.ERROR, "Illegal numeric literal, remove leading zero. Octal literals should not be used.  input: "+yytext() );  
}



\n|[^\n] { //Catchall rule to handle unknown characters 
   return new Symbol(TokenConstants.ERROR, yytext());
}

