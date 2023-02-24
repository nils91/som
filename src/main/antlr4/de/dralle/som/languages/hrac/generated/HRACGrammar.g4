grammar HRACGrammar;

program : (NEWLINE? line)* EOF?;
line: (directive|command|symbol_dec);
symbol_dec: SYMBOL cnt_specify? symbol_os?;

directive: SEMICOLON (HEAP|D_N) EQ INT;

command: (SYMBOL COLON)? (NAR|NAW) symbol_os;

symbol_os:(SYMBOL_KW|ALLOC_KW)? (SYMBOL|builtins) offset_specify?;

offset_specify:B_OPEN (NEG_INT|INT) B_CLOSE;

cnt_specify:B_OPEN (INT|BI_N) B_CLOSE;

builtins:ACC|ADR_EVAL|WH_COM|WH_DIR|WH_EN|WH_SEL|ADR|HEAP_N|BI_N;

NEWLINE: '\r\n'|'\n';
COMMENT:'#' .*? (NEWLINE|EOF) ->skip;
ACC:'ACC';
ADR_EVAL:'ADR_EVAL';
WH_EN:'WH_EN';
WH_COM:'WH_COM';
WH_DIR:'WH_DIR';
WH_SEL:'WH_SEL';
HEAP_N:'HEAP';
ADR:'ADR';
NAR:'NAR';
NAW:'NAW';
SYMBOL_KW:'symbol'|'sym';
ALLOC_KW:'allocate'|'alloc';
HEAP:'heap';
BI_N:'N';
D_N:'n';
SYMBOL:[a-mo-zA-Z][a-zA-Z0-9_-]*;
INT:[0-9]+;
NEG_INT: DASH INT;
EQ:'=';
SEMICOLON:';';
COLON:':';
B_OPEN:'[';
B_CLOSE:']';
DASH:'-';

WS : [ \f\t]+ -> skip;