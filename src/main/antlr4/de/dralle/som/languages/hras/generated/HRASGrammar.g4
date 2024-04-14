grammar HRASGrammar;

program : (NEWLINE? line)* EOF?;
line: (directive|symbol_dec|command);
symbol_dec: SYMBOL_KW? SYMBOL int_or_symbol;

directive: SEMICOLON ((D_N EQ integer)|(START|CONT) EQ int_or_symbol);

command: (NAR|NAW) int_or_symbol;

int_or_symbol:(integer|SYMBOL) offset_specify?;

offset_specify:B_OPEN (neg_integer|integer) B_CLOSE;
neg_integer: DASH integer;
integer: binary_int|based_int|octal_int|hex_int|decimal_int;

based_int:INT 'b' (INT|EINT);
binary_int:'0b' INT;
octal_int:'0o' INT;
hex_int:('0h'|'0x') (INT|EINT);
decimal_int:'0d'? INT;

NEWLINE: '\r\n'|'\n';
COMMENT:'#' .*? (NEWLINE|EOF) ->skip;
NAR:'NAR';
NAW:'NAW';
CONT:'continue'|'cont';
START:'start';
SYMBOL_KW:'symbol'|'sym';
D_N:'n';
SYMBOL:[a-mo-zA-Z][a-zA-Z0-9_-]*;
INT:[0-9]+;
EINT:[A-Z0-9]+;
EQ:'=';
SEMICOLON:';';
B_OPEN:'[';
B_CLOSE:']';
DASH:'-';

WS : [ \f\t]+ -> skip;