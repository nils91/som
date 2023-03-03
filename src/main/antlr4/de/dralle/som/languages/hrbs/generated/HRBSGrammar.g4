grammar HRBSGrammar;

program : (import_stmt NEWLINE?)* (command_def NEWLINE?)+  EOF?;
command_def: cmd_head directives? symbol_definitions? commands?;
commands: (command NEWLINE?)+;
symbol_blk:(GLOBAL|SHARED|LOCAL) C_OPEN NEWLINE  (symbol_dec NEWLINE)+ C_CLOSE NEWLINE;
symbol_definitions: ((symbol_blk|symbol_ns) NEWLINE)+;

cmd_head:NAME ((cmd_head_param COMMA)* cmd_head_param)? COLON NEWLINE;
cmd_head_param:NAME;
directives: (directive NEWLINE)+;
symbol_dec: (ALLOC|SYMBOL) NAME cnt_specify? symbol_os?;

directive: SEMICOLON (HEAP|D_N) EQ INT;

command: (NAME COLON)? NEWLINE? (NAR|NAW|NAME) ((symbol_os COMMA)* symbol_os)? SEMICOLON;

symbol_ns:(GLOBAL|SHARED|LOCAL?) symbol_dec;

symbol_os:AMP? (NAME|builtins) offset_specify*;

offset_specify:B_OPEN (NEG_INT|INT) B_CLOSE;

cnt_specify:B_OPEN (INT|BI_N) B_CLOSE;

builtins:ACC|ADR_EVAL|WH_COM|WH_DIR|WH_EN|WH_SEL|ADR|HEAP_N|BI_N;

import_stmt: IMPORT (NAME|FILEPATH) (AS NAME)? (USING NAME)? SEMICOLON;

NEWLINE: '\r\n'|'\n';
COMMENT:'#' .*? (NEWLINE|EOF) ->skip;
IMPORT:'import';
ALLOC:'alloc'|'allocate';
SYMBOL:'symbol'|'sym';
USING:'using';
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
HEAP:'heap';
GLOBAL:'global';
SHARED:'shared';
LOCAL:'local';
AS:'as';
BI_N:'N';
D_N:'n';
SINGLE_QUOTE:'\'';
DOUBLE_QUOTE:'\""';
FILEPATH:[\'"] .+? [\'"];
NAME:[a-zA-Z][a-zA-Z0-9_-]*;
INT:[0-9]+;
NEG_INT: DASH INT;
EQ:'=';
COLON:':';
SEMICOLON:';';
DOT:'.';
COMMA:',';
B_OPEN:'[';
B_CLOSE:']';
C_OPEN:'{';
C_CLOSE:'}';
P_OPEN:'(';
P_CLOSE:')';
DASH:'-';
AMP:'&';
WS : [ \f\t]+ -> skip;