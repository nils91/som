grammar HRAGrammar;

program : statement+;statement : let | show ;let : VAR '=' INT ;
show : 'show' (INT | VAR) ;VAR : [a-z]+ ;

WS : [ \n\r\f\t]+ -> skip;
COMMENT: '#' .* -> skip;
INT : '0' .. '9' + ;