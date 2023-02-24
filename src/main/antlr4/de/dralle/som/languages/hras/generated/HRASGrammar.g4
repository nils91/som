grammar HRASGrammar;

program : (NEWLINE? line)* EOF?;
line: (directive|symbol_dec|command);
symbol_dec: SYMBOL_KW SYMBOL int_or_symbol;

directive: SEMICOLON ((D_N EQ INT)|(START|CONT) EQ int_or_symbol);

command: (NAR|NAW) int_or_symbol;

int_or_symbol:(INT|SYMBOL) offset_specify?;

offset_specify:B_OPEN (NEG_INT|INT) B_CLOSE;

NEWLINE: '\r\n'|'\n';
COMMENT:'#' .*? (NEWLINE|EOF) ->skip;
NAR:'NAR';
NAW:'NAW';
CONT:'continue'|'cont';
START:'start';
SYMBOL_KW:'symbol';
D_N:'n';
SYMBOL:[a-mo-zA-Z][a-zA-Z0-9_-]*;
INT:[0-9]+;
NEG_INT: DASH INT;
EQ:'=';
SEMICOLON:';';
B_OPEN:'[';
B_CLOSE:']';
DASH:'-';

WS : [ \f\t]+ -> skip;