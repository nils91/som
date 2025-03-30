grammar HRBSGrammar;

program
:
	(
		import_stmt NEWLINE?
	)*
	(
		command_def NEWLINE?
	)+ EOF?
;

command_def
:
	cmd_head
	(
		(
			directive
			| symbol_definition
			| oti
			| command
		) SEMICOLON? NEWLINE?
	)*
;

oti
:
	(
		OTI_SET
		| OTI_CLEAR
	) target_argument SEMICOLON
;

symbol_blk
:
	def_scope C_OPEN NEWLINE
	(
		symbol_dec NEWLINE
	)+ C_CLOSE NEWLINE
;

symbol_definition
:
	symbol_blk
	| symbol_ns
;

cmd_head
:
	NAME
	(
		(
			cmd_head_param COMMA
		)* cmd_head_param
	)? COLON NEWLINE
;

cmd_head_param
:
	NAME
;

directives
:
	(
		directive NEWLINE
	)+
;

symbol_dec
:
	(
		ALLOC
		| SYMBOL
	) NAME cnt_specify? target_argument?
;

def_scope
:
	(
		GLOBAL
		| SHARED
		| LOCAL
	)
;

directive
:
	SEMICOLON GLOBAL?  directive_name EQ
	(
		primary_expr
		| DIRECTIVE_VALUE_STR
	)
;

directive_name
:
	INT
	| NAME
;

command
:
	commad_label? NEWLINE? offset_specify_values? custom_command_call_no_param
	(
		(
			target_argument COMMA
		)* target_argument
	)? SEMICOLON
;

directive_access
:
	DOLLAR directive_name
;

DOLLAR
:
	'$'
;

offset_specify_number
:
	(
		primary_expr
		| directive_access
	)
;

offset_specify_values
:
	(
		directive_access EQ
	)?
	(
		offset_specify_range
		| offset_specify_set
	)
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

custom_command_call_no_param
:
	NAME instance_id?
;

instance_id
:
	B_OPEN
	(
		NAME
		| directive_access
	) B_CLOSE
;

commad_label
:
	def_scope? NAME COLON
;

symbol_ns
:
	def_scope? symbol_dec
;

target_argument
:
	AMP?
	(
		symbol_target
		| fixed_address
	) offset_specify*
;

symbol_target
:
	symbol_target_name
;

fixed_address
:
	AT par_expr
;

AT
:
	'@'
;

symbol_target_name
:
	(
		custom_command_call_no_param DOT
	)?
	(
		NAME
	)
;

offset_specify
:
	B_OPEN
	(
		offset_specify_number
	) B_CLOSE
;

cnt_specify
:
	B_OPEN
	(
		primary_expr
		| directive_access
	) B_CLOSE
;

import_stmt
:
	IMPORT
	(
		NAME
		| DIRECTIVE_VALUE_STR
	)
	(
		AS NAME
	)?
	(
		USING NAME
	)? SEMICOLON
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
	| INT
;
//expression tree end

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

IMPORT
:
	'import'
;

ALLOC
:
	'alloc'
	| 'allocate'
;

SYMBOL
:
	'symbol'
	| 'sym'
;

USING
:
	'using'
;

GLOBAL
:
	'global'
;

SHARED
:
	'shared'
;

LOCAL
:
	'local'
;

AS
:
	'as'
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

EQ
:
	'='
;

COLON
:
	':'
;

SEMICOLON
:
	';'
;

DOT
:
	'.'
;

COMMA
:
	','
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

AMP
:
	'&'
;

WS
:
	[ \f\t]+ -> skip
;