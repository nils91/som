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
	| NAME
	| directive_access
	| (P_OPEN primary_expr P_CLOSE)
;
directive_access
:
	DOLLAR directive_name ( P_OPEN ((primary_expr COMMA)* primary_expr)? P_CLOSE )?
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
	negation_expr
	| PIPE negation_expr PIPE
;

negation_expr: DASH? par_expr;

PIPE
:
	'|'
;

par_expr
:
	integer_or_directive
	| P_OPEN primary_expr P_CLOSE
;


integer_or_directive
:
	number | DIRECTIVE_VALUE_STR | directive_access
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
	PREFIXED_INT | INT
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

NAME
:
	[a-zA-Z] [a-zA-Z0-9_]*
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

PREFIXED_INT
:
	(
		BINARY_NUMBER_PREFIX
		| OCTAL_NUMBER_PREFIX
		| HEX_NUMBER_PREFIX
		| DECIMAL_NUMBER_PREFIX
		| BASE_NUMBER_PREFIX
	) [0-9a-zA-Z]+
;
INT: ([1-9][0-9]*) | '0';

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
