grammar HRAGrammar;

main
:
	line+ EOF?
;

line
:
	(
		directive
	) EOL?
;

directive
:
	';'
	(
		directive_name EQ number
	)
;

directive_name
:
	N
	| HEAP
	| CONTINUE
;

number
:
	(
		DIGIT_NOT_ZERO number*
	)
	| DIGIT_ZERO
;

LINE_COMMENT
:
	'#' .*? EOL -> skip
;

DIGIT_NOT_ZERO
:
	(
		'1' .. '9'
	)
;

EQ
:
	'='
;

N
:
	'n'
	| 'N'
;

DIGIT_ZERO
:
	'0'
;

HEAP
:
	'heap'
;

CONTINUE
:
	'continue'
	| 'cont'
;

EOL
:
	(
		'\r\n'
		| '\r'
		| '\n' EOF
	)
;

WS
:
	(
		' '
		| '\t'
		| '\r'
		| '\n'
	)+ -> skip
;
