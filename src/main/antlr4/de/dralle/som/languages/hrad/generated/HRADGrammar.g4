grammar HRADGrammar;

program
:
	line+ EOF?
;

line
:
	(
		directive
		| oti
		| command
	)
;

oti
:
	(
		OTI_SET
		| OTI_CLEAR
	) number
;

directive: SEMICOLON (simple_directive | directive_function);

directive_function
:
	directive_name P_OPEN (directive_name COMMA)* directive_name P_CLOSE EQ
	(
		primary_expr
	)
;

simple_directive
:
	directive_name EQ
	(
		primary_expr
	)
;

directive_name
:
	INT
	| name
	| directive_access
	| (P_OPEN primary_expr P_CLOSE)
;
directive_access
:
	DOLLAR directive_name ( P_OPEN (primary_expr COMMA)* primary_expr P_CLOSE )?
;

DOLLAR
:
	'$'
;

// following rules are for the expression tree

primary_expr
:
	additive_expr
;

additive_expr
:
	multiplicative_expr
	| additive_expr
	(
		PLUS
		| DASH
	) multiplicative_expr
;

PLUS
:
	'+'
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

MOD
:
	'%'
;

DIV
:
	'/'
;

MUL
:
	'*'
;

power_expr
:
	factorial_expr
	| factorial_expr CARET power_expr
;

CARET
:
	'^'
;

factorial_expr
:
	absolute_expr EXCL?
;

EXCL
:
	'!'
;

absolute_expr
:
	par_expr
	| PIPE par_expr PIPE
;

PIPE
:
	'|'
;

par_expr
:
	signed_integer_or_directive
	| P_OPEN primary_expr P_CLOSE
;

signed_integer_or_directive
:
	DASH? integer_or_directive
;

integer_or_directive
:
	directive_access
	| number | DIRECTIVE_VALUE_STR
;


P_OPEN
:
	'('
;

P_CLOSE
:
	')'
;

DASH
:
	'-'
;

//expression tree end
command
:
	(
		NAR
		| NAW
	) number
;

number
:
	based_int
	| binary_int
	| octal_int
	| hex_int
	| decimal_int
;

based_int
:
	INT ITS_A_B
	(
		INT
		| EINT
	)
;

binary_int
:
	'0b' INT
;

octal_int
:
	'0o' INT
;

hex_int
:
	(
		'0h'
		| '0x'
	)
	(
		INT
		| EINT
	)
;

decimal_int
:
	'0d'? INT
;


name
:
	NAME_TK | ITS_A_B | EINT
;


COMMENT
:
	(
		(
			(
				'#'
				| '//'
			) .*? [\r\n]+
		)
		|
		(
			'/*' .*? '*/'
		)
	) -> skip
;

ITS_A_B : 'b';

NAR
:
	'NAR'
;

NAW
:
	'NAW'
;

OTI_SET
:
	'setonce'
;

OTI_CLEAR
:
	'clearonce'
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



INT
:
	[0-9]+
;

EINT
:
	[A-Z0-9]+
;

NAME_TK:[a-zA-Z] [a-zA-Z0-9_-]*;

EQ
:
	'='
;

SEMICOLON
:
	';'
;

COMMA:',';

B_OPEN
:
	'['
;

B_CLOSE
:
	']'
;

WS
:
	[ \f\t\r\n]+ -> skip
;
