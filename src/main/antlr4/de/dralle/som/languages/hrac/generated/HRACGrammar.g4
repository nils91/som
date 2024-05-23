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
		| oti
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

oti
:
	(
		OTI_SET
		| OTI_CLEAR
	) symbol_os
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
	(
		SYMBOL
		| memadr
	) offset_specify?
;

memadr
:
	AT INT
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

//following rule will only be used in for dup loop heads

offset_specify_values
:
	(
		directive_access EQ
	)?
	(offset_specify_range|offset_specify_set)
;

offset_specify_range
:
	(
		B_OPEN
		| B_CLOSE
	)
	(
		offset_specify_number? COLON offset_specify_number?
	)
	(
		SEMICOLON offset_specify_number
	)?
	(
		B_CLOSE
		| B_OPEN
	)
;

offset_specify_set
:
	C_OPEN
	(
		(
			offset_specify_number COMMA
		)* offset_specify_number
	)? C_CLOSE
;

for_duplication
:
	for_duplication_head NEWLINE? program_blk NEWLINE?
;

for_duplication_head
:
	FOR offset_specify_values DUPLICATE COLON
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

directive_name
:
	INT
	| SYMBOL
;

DOLLAR
:
	'$'
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

OTI_SET
:
	'setonce'
;

OTI_CLEAR
:
	'clearonce'
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

BINARY_NUMBER_PREFIX
:
	'0b'
;

OCTAL_NUMBER_PREFIX
:
	'0o'
;

HEX_NUMBER_PREFIX
:
	'0h'
	| '0x'
;

DECIMAL_NUMBER_PREFIX
:
	'0d'
;

BASE_NUMBER_PREFIX
:
	[0-9]+ 'b'
;

INT
:
	(
		BINARY_NUMBER_PREFIX
		| OCTAL_NUMBER_PREFIX
		| HEX_NUMBER_PREFIX
		| DECIMAL_NUMBER_PREFIX
		| BASE_NUMBER_PREFIX
	)? [0-9a-zA-Z]+
;

NEG_INT
:
	DASH INT
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

EQ
:
	'='
;

COMMA
:
	','
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