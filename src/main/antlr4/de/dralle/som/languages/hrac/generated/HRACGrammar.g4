grammar HRACGrammar;

program : (NEWLINE? line)* EOF?;
line: (directive|commadn_or_for|symbol_dec);
symbol_dec: (SYMBOL_KW|ALLOC_KW)? SYMBOL cnt_specify? symbol_os?;

directive: SEMICOLON (HEAP|D_N) EQ INT;
commadn_or_for:command|for_duplication;
program_blk:C_OPEN program C_CLOSE;
command: (SYMBOL COLON)? (NAR|NAW) symbol_os;

symbol_os: (SYMBOL|builtins) offset_specify?;

offset_specify:B_OPEN offset_specify_number B_CLOSE;

offset_specify_number:(NEG_INT|INT|AT);
offset_specify_range:B_OPEN offset_specify_number COLON offset_specify_number B_CLOSE;
for_duplication:for_duplication_head NEWLINE? program_blk;
for_duplication_head:FOR offset_specify_range DUPLICATE COLON;
cnt_specify:B_OPEN (INT|AT) B_CLOSE;

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
DUPLICATE:'dup'|'duplicate';
HEAP:'heap';
FOR:'for';
BI_N:'N';
D_N:'n';
SYMBOL:[a-zA-Z][a-zA-Z0-9_-]*;
INT:[0-9]+;
NEG_INT: DASH INT;
EQ:'=';
SEMICOLON:';';
COLON:':';
B_OPEN:'[';
B_CLOSE:']';
C_OPEN:'{';
C_CLOSE:'}';
DASH:'-';
AT:'@';

WS : [ \f\t]+ -> skip;