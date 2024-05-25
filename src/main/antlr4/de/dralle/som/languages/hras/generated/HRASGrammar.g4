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
			D_N EQ primary_expr
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
		primary_expr
		| SYMBOL
	) offset_specify?
;

signed_integer
:
	integer
	| neg_integer
;

primary_expr
:
	additive_expr
;

additive_expr
:
	multiplicative_expr
	|
		additive_expr
		(
			PLUS
			| DASH
		) multiplicative_expr
;

multiplicative_expr
:
	power_expr
	| multiplicative_expr
	(
		MUL
		| DIV
		| MOD
	) power_expr
;

power_expr
:
	factorial_expr
	| factorial_expr CARET power_expr
;

factorial_expr
:
	absolute_expr EXCL?
;

absolute_expr
:
	par_expr
	| PIPE par_expr PIPE
;

par_expr
:
	signed_integer
	| P_OPEN primary_expr P_CLOSE
;

offset_specify
:
	B_OPEN
	(
		primary_expr
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

P_OPEN
:
	'('
;

P_CLOSE
:
	')'
;

B_OPEN
:
	'['
;

B_CLOSE
:
	']'
;

PIPE
:
	'|'
;

PLUS
:
	'+'
;

DASH
:
	'-'
;

MUL
:
	'*'
;

DIV
:
	'/'
;

MOD
:
	'%'
;

CARET
:
	'^'
;

EXCL
:
	'!'
;

WS
:
	[ \f\t]+ -> skip
;