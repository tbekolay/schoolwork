%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%DEFAULT VALUES                             %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
has(patience, 0).
has(money, 0).
prefer-game(wii, 0).
prefer-game(xbox360, 0).
prefer-game(ps3, 0).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%RULES                                      %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%System preference rules
%These are CF calculation based on these unwritten rules:
%  likes(wii, CF) :- prefer-game(wii, CF).
%  likes(wii, CF) :- has(patience, CF_1), CF is -CF_1 * 0.5.
%  likes(wii, CF) :- has(money, CF_1), CF is -CF_1.
%  likes(xbox360, CF) :- prefer-game(xbox360, CF).
%  likes(xbox360, CF) :- has(patience, CF_1), CF is -CF_1.
%  likes(xbox360, CF) :- has(money, CF_1), CF is -CF_1 * 0.5.
%  likes(ps3, CF) :- prefer-game(ps3, CF).
%  likes(ps3, CF) :- has(patience, CF).
%  likes(ps3, CF) :- has(money, CF).

likes(wii, CF) :- prefer-game(wii, Game), has(patience, InPatience), has(money, InMoney),
	Patience is (InPatience * -0.5), Money is (InMoney * -1), combine(Patience, Money, PatMoney), combine(PatMoney, Game, CF).

likes(xbox360, CF) :- prefer-game(xbox360, Game), has(patience, InPatience), has(money, InMoney),
	Patience is (InPatience * -1), Money is (InMoney * -0.5), combine(Patience, Money, PatMoney), combine(PatMoney, Game, CF).
	 
likes(ps3, CF) :- prefer-game(ps3, Game), has(patience, Patience), has(money, Money),
	combine(Patience, Money, PatMoney), combine(PatMoney, Game, CF).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%CALCULATIONS                               %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%MATH

%Calculates average of non-empty list
average(List, Avg) :- sum(List, Sum), count(List, Count), Avg is Sum/Count.	

%Calculates count of elements in a list
count([],0).
count([H|T], Count) :- count(T, TCount), Count is TCount + 1.

%Calculates sum of elements in a list
sum([],0).
sum([H|T], Sum) :- sum(T, TSum), Sum is TSum + H.

%Calculates absolute value of a number
abs(In, Out) :- In < 0, Out is -In.
abs(In, Out) :- In >= 0, Out is In.

%Calculates the minimum of two numbers
min(A, B, Min) :- A < B, Min is A.
min(A, B, Min) :- B =< A, Min is B.

%CERTAINTY FACTOR

%Combines two independent CF's using the formula described in the notes
% (if X > 0 and Y > 0, X+Y - (X*Y)
%  if X < 0 and Y < 0, X+Y + (X*Y)
%  else (X+Y) / (1 - min(abs(X), abs(Y))
combine(X, Y, CF) :- X >= 0, Y >= 0, !, CF is ( (X+Y) - (X*Y) ).
combine(X, Y, CF) :- X =< 0, Y =< 0, !, CF is ( (X+Y) + (X*Y) ).
combine(X, Y, CF) :- X >= 0, Y =< 0, !, abs(X, Xa), abs(Y, Ya), min(Xa, Ya, Min), CF is ( (X+Y) / (1 - Min) ).
combine(X, Y, CF) :- X =< 0, Y >= 0, !, abs(X, Xa), abs(Y, Ya), min(Xa, Ya, Min), CF is ( (X+Y) / (1 - Min) ).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%DATA COLLECTION                            %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%Main Loop
collect_data :-	repeat,
	write('Please choose a line of questioning by typing in the number of the selection, followed by a period:'), nl,
	write('1. Game preference'), nl,
	write('2. Time factors'), nl,
	write('3. Financial factors'), nl,
	read(Select), nl,
	collect_data(Select),
	write('Continue inputting data? y/n'), nl,
	read(Continue), nl,
	Continue == n,
	!.

%1: Game preference
collect_data(1) :- retract(prefer-game(wii,_)), retract(prefer-game(xbox360,_)), retract(prefer-game(ps3,_)),
	write('Answer the following questions honestly.  You won\'t hurt my feelings!'), nl,
	ask-game('any of the Legend of Zelda games', CF_1),
	ask-game('Wario Ware', CF_2),
	ask-game('Halo 1 and/or 2', CF_3),
	ask-game('Fable', CF_4),
	ask-game('any of the Metal Gear Solid games', CF_5),
	ask-game('a Grand Theft Auto game', CF_6),
	ask-game('one or more of Final Fantasy 7 through 12', CF_7),
	determine_cf(y, CF_8, 'How likely are you to play multiplayer games off-line?\n0 = Not likely at all, 5 = Neither likely nor unlikely, 10 = Certainly.', 0, 10),
	determine_cf(y, CF_9, 'How likely are you to play multiplayer games on-line?\n0 = Not likely at all, 5 = Neither likely nor unlikely, 10 = Certainly.', 0, 10),
	average([CF_1, CF_2, CF_7, CF_8], WiiCF), assert(prefer-game(wii, WiiCF)),
	average([CF_3, CF_4, CF_6, CF_9], Xbox360CF), assert(prefer-game(xbox360, Xbox360CF)),
	average([CF_5, CF_6, CF_7], PS3CF), assert(prefer-game(ps3, PS3CF)), !.

ask-game(Game, CF) :- write('Have you played '), write(Game), write('?\nAnswer with a y or n, followed by a period.'), nl,
	read(Select), nl,
	determine_cf(Select, CF, 'How would you rate the game, on a scale of 0 to 10?\n0 = Absolutely hated it, 5 = Neither hated nor liked it, 10 = Absolutely loved it.', 0, 10).

%2: Time factors
collect_data(2) :- retract(has(patience,_)),
	determine_cf(y, CF, 'How willing are you to wait until after Christmas to obtain a next-gen console?\n0 = Must have it on or before Christams, 5 = Doesn\'t have to be on Christmas, but not much later, 10 = Doesn\'t matter when I get it.', 0, 10),
	assert(has(patience, CF)), !.

%3: Financial factors
collect_data(3) :- retract(has(money,_)),
	determine_cf(y, CF_1, 'How much would you ideally spend on a console?\nPlease enter an amount (in Canadian dollars) from $200 - $1000 (don\'t include the $).', 200, 1000),
	write('Do you plan to use online services?  This includes playing multiplayer games online, or downloading extra content for offline games.'), nl,
	read(Select), nl,
	determine_cf(Select, CF_2, 'How much would you be willing to spend per year to access online content?\nPlease enter a dollar amount between $0 (free) and $100 (don\'t include the $).', 0, 100),
	average([CF_1, CF_2], CF),
	assert(has(money, CF)), !.

%Support Methods
collect_data(Else) :- false.

determine_cf(Select, CF, Prompt, Min, Max):- Select \== y, CF is 0.
determine_cf(Select, CF, Prompt, Min, Max):- Select == y, !, repeat,
	write(Prompt), nl,
	write('Enter a number (decimals are fine), then a period.'), nl,
	read(Rating), nl,
	Rating @=< Max, Rating @>= Min,
	!, CF is ( (Rating - ((Max - Min) / 2)) * (1 / ((Max - Min) / 2)) ).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%RESULT OUTPUT                              %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%Main
display_results :- likes(wii, CFWii), likes(xbox360, CFXbox), likes(ps3, CFPS3),
	write('Based on the answers you have given, we have deduced that:'), nl,
	display_best(CFWii, CFXbox, CFPS3), nl,
	write('Go into more detail? y/n'), nl,
	read(Cont_1), nl, Cont_1 == y, !,

%More detailed results
	like-dislike(CFWii, CFWiiLike), like-dislike(CFXbox, CFXboxLike), like-dislike(CFPS3, CFPS3Like),
	abs(CFWii*100, CFWiiP), abs(CFXbox*100, CFXboxP), abs(CFPS3*100, CFPS3P),

	write('Based on the answers you have given, we have deduced that:'), nl,
	write('\tYou will '), write(CFWiiLike), write(' the Wii, with '), write(CFWiiP), write('% confidence.'), nl,
	write('\tYou will '), write(CFXboxLike), write(' the Xbox360, with '), write(CFXboxP), write('% confidence.'), nl,
	write('\tYou will '), write(CFPS3Like), write(' the PS3, with '), write(CFPS3P), write('% confidence.'), nl, nl,

%Why (explanation)
	write('Want to know why? y/n'), nl,
	read(Cont_2), nl, Cont_2 == y, !,

	prefer-game(wii, Wii), prefer-game(xbox360, Xbox360), prefer-game(ps3, PS3),
	has(patience, Patience), has(money, Money),
	like-dislike(Wii, WiiLike), like-dislike(Xbox360, XboxLike), like-dislike(PS3, PS3Like),
	do-dont(Patience, PatienceDo), do-dont(Money, MoneyDo),
	abs(Wii*100, WiiP), abs(Xbox360*100, XboxP), abs(PS3*100, PS3P), abs(Patience*100, PatienceP), abs(Money*100, MoneyP),

	write('Based on the answers you have given, we can say that:'), nl,
	write('\tYou will '), write(WiiLike), write(' Wii\'s selection of games, with '), write(WiiP), write('% confidence.'), nl,
	write('\tYou will '), write(XboxLike), write(' XBox 360\'s selection of games, with '), write(XboxP), write('% confidence.'), nl,
	write('\tYou will '), write(PS3Like), write(' PS3\'s selection of games, with '), write(PS3P), write('% confidence.'), nl,
	write('\tYou '), write(PatienceDo), write(' have the patience to wait until after Christmas, with '), write(PatienceP), write('% confidence.'), nl,
	write('\tYou '), write(MoneyDo), write(' have the urge to spend a lot of money on a console purchase, with '), write(MoneyP), write('% confidence.'), nl.

%Displays system with highest Certainty Factor
display_best(Wii, Xbox, PS3) :- Wii >= Xbox, Wii >= PS3, !, write('\tYou will like the Wii the best!'), nl.
display_best(Wii, Xbox, PS3) :- Xbox >= Wii, Xbox >= PS3, !, write('\tYou will like the Xbox360 the best!'), nl.
display_best(Wii, Xbox, PS3) :- PS3 >= Wii, PS3 >= Xbox, !, write('\tYou will like the PS3 the best!'), nl.

%Methods for changing strings to be grammatically correct
like-dislike(CF, Result) :- CF @< 0, !, Result = 'dislike'.
like-dislike(CF, Result) :- CF @>= 0, !, Result = 'like'.
do-dont(CF, Result) :- CF @< 0, !, Result = 'do not'.
do-dont(CF, Result) :- CF @>= 0, !, Result = 'do'.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%MAIN PROGRAM                               %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

run :- write('Welcome to the Christmas 2006 Next-gen Console Assistant!'), nl,
	write('To determine which console you should be bugging your parents for, please answer all questions!  Or at least some.  But you will get better results the more questions you answer.'), nl, nl,
	collect_data, display_results, write('Thanks for using the Next-gen Console Assistant!').