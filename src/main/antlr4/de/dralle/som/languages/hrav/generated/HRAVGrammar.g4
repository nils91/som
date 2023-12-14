grammar HRAVGrammar;

program : line+ EOF?;
line: (directive|command);

directive: SEMICOLON ((D_N|START|CONT) EQ INT);

command: (NAR|NAW) INT;

COMMENT:((('#'|'//') .*? [\r\n]+)|('/*' .*? '*/')) ->skip;
NAR:'NAR';
NAW:'NAW';
CONT:'continue'|'cont';
START:'start';
D_N:'n';
INT:[0-9]+;
NEG_INT: DASH INT;
EQ:'=';
SEMICOLON:';';
B_OPEN:'[';
B_CLOSE:']';
DASH:'-';

WS : [ \f\t\r\n]+ -> skip;