grammar HRAGrammar;

program : (NEWLINE? line)* EOF?;
line: (directive|symbol_dec|command);
symbol_dec: SYMBOL int_or_symbol;

directive: SEMICOLON (D_N|HEAP|START|CONT) EQ int_or_symbol;

command: (NAR|NAW) int_or_symbol;

int_or_symbol:(INT|SYMBOL|builtins) offset_specify?;

offset_specify:B_OPEN (NEG_INT|INT) B_CLOSE;

builtins:ACC|ADR_EVAL|WH_COM|WH_DIR|WH_EN|WH_SEL|ADR|BI_N|EXIT;

NEWLINE: '\r\n'|'\n';
COMMENT:'#' .*? (NEWLINE|EOF) ->skip;
ACC:'ACC';
ADR_EVAL:'ADR_EVAL';
WH_EN:'WH_EN';
WH_COM:'WH_COM';
WH_DIR:'WH_DIR';
WH_SEL:'WH_SEL';
ADR:'ADR';
EXIT:'EXIT';
NAR:'NAR';
NAW:'NAW';
HEAP:'heap';
CONT:'continue'|'cont';
START:'start';
BI_N:'N';
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