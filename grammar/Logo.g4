grammar Logo; 

@header {
  package logoparsing;
}

BOOL 	: 'true' | 'false';
INT 	: '0' | [1-9][0-9]* ;
WS 		: [ \t\r\n]+ -> skip ;
ID		: [a-zA-Z][a-zA-Z0-9_\-]*;

programme 	: liste_instructions
;

liste_instructions :
  (instruction)+
;

instruction :
	'lc'							# lc
  | 'bc'							# bc
  | 've'							# ve
  | 'av'  arithmeticExpression 		# av
  | 're'  arithmeticExpression		# re
  | 'td'  arithmeticExpression 		# td
  | 'tg'  arithmeticExpression		# tg
  | 'fcc' arithmeticExpression		# fcc
  | 'fpos' arithmeticExpression arithmeticExpression 	# fpos
  | 'si' booleanExpression block elseBlock				# ifExpression
  | 'repete' arithmeticExpression block					# repeatExpression
  | 'tantque' booleanExpression block					# whileExpression
  | 'donne' '"' ID arithmeticExpression					# affectationExpression
  | procedureDeclaration								# procedureDeclarationInstruction
  | procedureCall										# procedureCallInstruction
  | 'ret' arithmeticExpression                          # returnInstruction
; 

arithmeticExpression :
    'hasard' arithmeticExpression					# rand
  | '(' procedureCall ')'                           # arithmeticExpressionFunctionCall
  | arithmeticExpression '*' arithmeticExpression	# mul
  | arithmeticExpression '/' arithmeticExpression 	# div
  | arithmeticExpression '+' arithmeticExpression	# sum
  |	arithmeticExpression '-' arithmeticExpression 	# sub
  | '(' arithmeticExpression ')'					# parenthesis
  | ':' ID											# arithmeticExpressionVar
  | 'loop'											# arithmeticExpressionLoop
  | INT												# arithmeticExpressionInt
;

// Fin Seance 2

booleanExpression :
    booleanExpression '&&' booleanExpression		# and
  | booleanExpression '||' booleanExpression  		# or
  | arithmeticExpression '=' arithmeticExpression	# eq
  | arithmeticExpression '<' arithmeticExpression	# inf
  | arithmeticExpression '<=' arithmeticExpression	# infEq
  | arithmeticExpression '>' arithmeticExpression	# sup
  | arithmeticExpression '>=' arithmeticExpression	# supEq
  |	BOOL 											# bool
;

block : '[' liste_instructions ']';

elseBlock : block 	
			| 
			; 
			
// TODO ? LOCALE			
			
// Fin Seance 3

procedureDeclaration 	: 'pour' ID procedureListeArgs liste_instructions 'fin';
procedureListeArgs 		: ':' ID procedureListeArgs 
						|
						;

procedureCall : ID procedureCallArgs;
procedureCallArgs : arithmeticExpression procedureCallArgs 
					| 
					;

					