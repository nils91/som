grammar HRBSGrammar;

program : (import_stmt NEWLINE?)+ (command_def NEWLINE?)+  EOF?;
command_def: cmd_head directives? symbol_definitions? commands;
commands: (command NEWLINE?)+;

symbol_definitions: (symbol_dec NEWLINE)+;

cmd_head:NAME ((NAME COMMA)* NAME)? COLON NEWLINE;
directives: (directive NEWLINE)+;
line: (directive|command|symbol_dec);
symbol_dec: NAME cnt_specify? symbol_os?;

directive: SEMICOLON (HEAP|D_N) EQ INT;

command: NAME? (NAR|NAW|NAME) symbol_os SEMICOLON;

symbol_os:(NAME|builtins) offset_specify?;

offset_specify:B_OPEN (NEG_INT|INT) B_CLOSE;

cnt_specify:B_OPEN (INT|BI_N) B_CLOSE;

builtins:ACC|ADR_EVAL|WH_COM|WH_DIR|WH_EN|WH_SEL|ADR|HEAP_N|BI_N;

import_stmt: IMPORT (NAME|FILEPATH) SEMICOLON;

NEWLINE: '\r\n'|'\n';
COMMENT:'#' .*? (NEWLINE|EOF) ->skip;
IMPORT:'import';
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
DASH:'-';
WS : [ \f\t]+ -> skip;