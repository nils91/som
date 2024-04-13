grammar HRAVGrammar;

program : line+ EOF?;
line: (directive|command);
directive: SEMICOLON (((D_N|START|CONT) EQ number));

command: (NAR|NAW) number;

number: based_int|binary_int|octal_int|hex_int|decimal_int;

based_int:INT 'b' (INT|EINT);
binary_int:'0b' INT;
octal_int:'0o' INT;
hex_int:'0h' (INT|EINT);
decimal_int:'0d'? INT;

COMMENT:((('#'|'//') .*? [\r\n]+)|('/*' .*? '*/')) ->skip;
NAR:'NAR';
NAW:'NAW';
CONT:'continue'|'cont';
START:'start';
D_N:'n';
INT:[0-9]+;
EINT:[A-Z0-9]+;
EQ:'=';
SEMICOLON:';';
B_OPEN:'[';
B_CLOSE:']';

WS : [ \f\t\r\n]+ -> skip;