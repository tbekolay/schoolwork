#ifndef TOPOST_H
#define TOPOST_H

typedef enum OPERATORS {
	PLUS,
	MINUS,
	TIMES,
	DIVIDE,
	EXPONENT,
	MOD
}operators;

char *toPostfix(char *infix);
int evalPostfix(char *expr);

#endif
