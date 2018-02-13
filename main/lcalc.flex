/* --------------------------Usercode Section------------------------ */

import java_cup.runtime.*;

%%

/* -----------------Options and Declarations Section----------------- */

%class Lexer

%line
%column

%cup

/*
  Declarations
*/
%{

    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }

    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}


/*
  Macro Declarations
*/


LineTerminator = \r|\n|\r\n


WhiteSpace     = {LineTerminator} | [ \t\f]


intnum = 0 | [0-9][0-9]*
select = [Ss][Ee][Ll][Ee][Cc][Tt]
distinct = [Dd][Ii][Ss][Tt][Ii][Nn][Cc][Tt]
from = [Ff][Rr][Oo][Mm]
where = [Ww][Hh][Ee][Rr][Ee]
in = [Ii][Nn]
and = [Aa][Nn][Dd]
not = [Nn][Oo][Tt]
exists = [Ee][Xx][Ii][Ss][Tt][Ss]
name = [A-Za-z_][A-Za-z_0-9]*
//str = \'.*\'


%%
/* ------------------------Lexical Rules Section---------------------- */



<YYINITIAL> {

    ";"                { return symbol(sym.SEMI); }
    "="                { return symbol(sym.EQUALS, new String(yytext())); }
    ">"                { return symbol(sym.GREATER, new String(yytext())); }
    "<"                { return symbol(sym.LESSER, new String(yytext())); }
    "<="               { return symbol(sym.EQGREATER, new String(yytext())); }
    ">="               { return symbol(sym.EQLESSER, new String(yytext())); }
    ","                { return symbol(sym.COMMA); }
    "."                { return symbol(sym.PERIOD); }
    "("                { return symbol(sym.LPAREN); }
    ")"                { return symbol(sym.RPAREN); }
    "'"                { return symbol(sym.APOST); }

    {select}       { return symbol(sym.SELECT); }
    {distinct}       { return symbol(sym.DISTINCT); }
    {from}       { return symbol(sym.FROM); }
    {where}       { return symbol(sym.WHERE); }
    {in}       { return symbol(sym.IN); }
    {and}       { return symbol(sym.AND); }
    {not}       { return symbol(sym.NOT); }
    {exists}       { return symbol(sym.EXISTS); }

    {intnum}      { return symbol(sym.INTNUM, new String(yytext()));}


    //{str}       { return symbol(sym.STR); }

    {name}       { return symbol(sym.NAME, new String(yytext())); }
    {WhiteSpace}       { /* just skip what was found, do nothing */ }
}


[^]                    { throw new Error("Illegal character <"+yytext()+">"); }
