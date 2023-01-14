grammar HRAGrammar;


/*
 * parser rules
 */

// startRules:
single_input: NEWLINE | simple_stmt ;
file_input:  (NEWLINE | stmt)* EOF;

stmt: simple_stmt;

simple_stmt: small_stmt NEWLINE;
small_stmt: d_stmt|assignment_stmt | flow_stmt;
d_stmt: ';' SYMBOL '=' NUMBER;
assignment_stmt: SYMBOL NUMBER;
flow_stmt: COMMAND SYMBOL;
COMMAND: 'NAR'|'NAW';




SYMBOL: [a-zA-Z0-9]+;

NUMBER
 : INTEGER
 ;

INTEGER
 : DECIMAL_INTEGER
 ;

NEWLINE
 : ( '\r'? '\n' | '\r' | '\f' ) WS?
 ;

DECIMAL_INTEGER
 : NON_ZERO_DIGIT DIGIT*
 | '0'
 ;
OPEN_BRACK : '[';
CLOSE_BRACK : ']';

UNKNOWN_CHAR
 : .
 ;


NON_ZERO_DIGIT
 : [1-9]
 ;

DIGIT
 : [0-9]
 ;

WS
 : [ \t]+ ->skip
 ;

COMMENT
 : '#' ~[\r\n\f]* ->skip
 ;

