grammar HRAVGrammar;

program : line+ EOF?;
line: (directive|command);

directive: SEMICOLON ((D_N EQ DECIMAL_INT)|((START|CONT) EQ number));

command: (NAR|NAW) number;

number: BASED_INT|BINARY_INT|OCTAL_INT|HEX_INT|DECIMAL_INT;

COMMENT:((('#'|'//') .*? [\r\n]+)|('/*' .*? '*/')) ->skip;
NAR:'NAR';
NAW:'NAW';
CONT:'continue'|'cont';
START:'start';
D_N:'n';
BASED_INT:[0-9A-Z]+ 'b' DECIMAL_INT;
BINARY_INT:[0-1]+ 'b';
OCTAL_INT:[0-7]+ 'o';
HEX_INT:[0-9A-F]+ 'h';
DECIMAL_INT:[0-9]+ 'd'?;
EQ:'=';
SEMICOLON:';';
B_OPEN:'[';
B_CLOSE:']';

WS : [ \f\t\r\n]+ -> skip;