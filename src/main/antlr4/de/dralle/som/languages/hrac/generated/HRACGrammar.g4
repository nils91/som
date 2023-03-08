grammar HRACGrammar;

program
:
	(
		NEWLINE? line
	)* EOF?
;

line
:
	(
		directive
		| commadn_or_for
		| symbol_dec
	)
;

symbol_dec
:
	(
		SYMBOL_KW
		| ALLOC_KW
	)? SYMBOL cnt_specify? symbol_os?
;

directive
:
	SEMICOLON directive_name EQ
	(
		INT
		| DIRECTIVE_VALUE_STR
	)
;

commadn_or_for
:
	command
	| for_duplication
;

program_blk
:
	C_OPEN program C_CLOSE
;

command
:
	(
		SYMBOL COLON
	)?
	(
		NAR
		| NAW
	) symbol_os
;

symbol_os
:
	SYMBOL offset_specify?
;

offset_specify
:
	B_OPEN offset_specify_number B_CLOSE
;

offset_specify_number
:
	(
		NEG_INT
		| INT
		| directive_access
	)
;

offset_specify_range
:
	B_OPEN? offset_specify_number COLON offset_specify_number B_CLOSE?
;

for_duplication
:
	for_duplication_head NEWLINE? program_blk
;

for_duplication_head
:
	FOR offset_specify_range DUPLICATE COLON
;

cnt_specify
:
	B_OPEN
	(
		INT
		| directive_access
	) B_CLOSE
;

directive_access
:
	DOLLAR directive_name
;
DOLLAR: '$';


directive_name
:
	INT
	| SYMBOL
;

NEWLINE
:
	'\r\n'
	| '\n'
;

COMMENT
:
	'#' .*?
	(
		NEWLINE
		| EOF
	) -> skip
;

NAR
:
	'NAR'
;

NAW
:
	'NAW'
;

SYMBOL_KW
:
	'symbol'
	| 'sym'
;

ALLOC_KW
:
	'allocate'
	| 'alloc'
;

DUPLICATE
:
	'dup'
	| 'duplicate'
;

FOR
:
	'for'
;

SYMBOL
:
	[a-zA-Z] [a-zA-Z0-9_-]*
;

INT
:
	[0-9]+
;

DIRECTIVE_VALUE_STR
:
	(
		'"'
		| '\''
	) .*?
	(
		'"'
		| '\''
	)
;

NEG_INT
:
	DASH INT
;

EQ
:
	'='
;

SEMICOLON
:
	';'
;

COLON
:
	':'
;

B_OPEN
:
	'['
;

B_CLOSE
:
	']'
;

C_OPEN
:
	'{'
;

C_CLOSE
:
	'}'
;

DASH
:
	'-'
;

AT
:
	'@'
;

WS
:
	[ \f\t]+ -> skip
;