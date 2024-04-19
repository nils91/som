grammar HRASGrammar;

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
		| symbol_dec
		| oti
		| command
	)
;

symbol_dec
:
	SYMBOL_KW? SYMBOL int_or_symbol
;

directive
:
	SEMICOLON
	(
		(
			D_N EQ integer
		)
		|
		(
			START
			| CONT
		) EQ int_or_symbol
	)
;

oti
:
	(
		OTI_SET
		| OTI_CLEAR
	) int_or_symbol
;

command
:
	(
		NAR
		| NAW
	) int_or_symbol
;

int_or_symbol
:
	(
		integer
		| SYMBOL
	) offset_specify?
;

offset_specify
:
	B_OPEN
	(
		neg_integer
		| integer
	) B_CLOSE
;

neg_integer
:
	DASH integer
;

integer
:
	binary_int
	| based_int
	| octal_int
	| hex_int
	| decimal_int
;

based_int
:
	BASE_NUMBER_PREFIX
	(
		INT
		| EINT
	)
;

binary_int
:
	BINARY_NUMBER_PREFIX INT
;

octal_int
:
	OCTAL_NUMBER_PREFIX INT
;

hex_int
:
	HEX_NUMBER_PREFIX
	(
		INT
		| EINT
	)
;

decimal_int
:
	DECIMAL_NUMBER_PREFIX? INT
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

CONT
:
	'continue'
	| 'cont'
;

START
:
	'start'
;

SYMBOL_KW
:
	'symbol'
	| 'sym'
;

D_N
:
	'n'
;

SYMBOL
:
	[a-mo-zA-Z] [a-zA-Z0-9_-]*
;

INT
:
	[0-9]+
;

EINT
:
	[A-Z0-9]+
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
	INT 'b'
;

EQ
:
	'='
;

SEMICOLON
:
	';'
;

B_OPEN
:
	'['
;

B_CLOSE
:
	']'
;

DASH
:
	'-'
;

WS
:
	[ \f\t]+ -> skip
;