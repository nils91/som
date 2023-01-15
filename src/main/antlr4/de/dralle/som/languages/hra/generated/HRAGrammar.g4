grammar HRAGrammar;

program : (NEWLINE? line)* EOF?;
line: (directive|symbol_dec|command);
symbol_dec: SYMBOL int_or_symbol;

directive: SEMICOLON (N|HEAP|START|CONT) EQ int_or_symbol;

command: (NAR|NAW) int_or_symbol;

int_or_symbol:INT|SYMBOL;

NEWLINE: '\r\n'|'\n';
COMMENT:'#' .*? (NEWLINE|EOF) ->skip;
NAR:'NAR';
NAW:'NAW';
N:'n';
HEAP:'heap';
CONT:'continue'|'cont';
START:'start';
SYMBOL:[a-mo-zA-Z][a-zA-Z0-9]*;
INT:[0-9]+;
EQ:'=';
SEMICOLON:';';
B_OPEN:'[';
B_CLOSE:']';

WS : [ \f\t]+ -> skip;