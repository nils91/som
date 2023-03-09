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
	cmd_head directives? symbol_definitions? commands?
;

commands
:
	(
		command NEWLINE?
	)+
;

symbol_blk
:
	def_scope C_OPEN NEWLINE
	(
		symbol_dec NEWLINE
	)+ C_CLOSE NEWLINE
;

symbol_definitions
:
	(
		(
			symbol_blk
			| symbol_ns
		) NEWLINE
	)+
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
	) NAME cnt_specify? symbol_os?
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
	SEMICOLON directive_name EQ
	(
		INT
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
	commad_label? NEWLINE? custom_command_call_no_param
	(
		(
			symbol_os COMMA
		)* symbol_os
	)? SEMICOLON
;

custom_command_call_no_param
:
	NAME instance_id?
;

instance_id
:
	B_OPEN NAME B_CLOSE
;

commad_label
:
	def_scope? NAME COLON
;

symbol_ns
:
	def_scope? symbol_dec
;

symbol_os
:
	AMP? symbol_target_nname offset_specify*
;

symbol_target_nname
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
		NEG_INT
		| INT
	) B_CLOSE
;

cnt_specify
:
	B_OPEN
	(
		INT
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

INT
:
	[0-9]+
;

NEG_INT
:
	DASH INT
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