/* Trevor Bekolay - 6796723
   Family Relationships in Prolog

   There are 4 generations of people; to make it easy to see,
   the names will start with a letter from A - D, A being
   the oldest generation, D being the youngest (you can fill
   in the rest).

   Thus, rules are a bit easier to verify if they work, as
   the relationship should have how many levels of difference
   built in; for example, grandparents differ by 2 generations,
   so they should start with a two letter diffence; eg A/C or B/D.

   Relationships determined from:
   http://en.wikipedia.org/wiki/Image:Relatives_Chart.jpg
*/

%oldest generation
%grandparents (dad's side)
female(abigail).
male(abbot).
married(abigail, abbot).

%grandparents (mom's side)
female(ainslie).
male(ajax).
married(ainslie, ajax).

%grandparents (in-law, mom's side)
female(adelaide).
male(adolf).
married(adelaide, adolf).

%grandparents (in-law, dad's side)
female(alaska).
male(alastair).
married(alaska, alastair).

%grand aunt & uncle (dad's side)
female(agatha).
male(ahmed).
sibling(agatha, abigail).
married(agatha, ahmed).

%second oldest generation

%parents
female(barbie).
male(bernard).
married(barbie, bernard).
mother(abigail, bernard).
father(abbot, bernard).
mother(ainslie, barbie).
father(ajax, barbie).

%parents (in-law)
female(becky).
male(beck).
married(becky, beck).
divorced(becky, beck).
mother(alaska, beck).
father(alastair, beck).
mother(adelaide, becky).
father(adolf, becky).

%aunt & uncle (mom's side)
female(beatrix).
male(beauregard).
married(beatrix, beauregard).
mother(ainslie, beatrix).
father(ajax, beatrix).

%aunt & uncle (in-law)
female(bernice).
male(benedict).
married(bernice, benedict).
sibling(benedict, becky).

%first cousin, once removed
male(beavis).
male(butthead).
mother(agatha, beavis).
father(ahmed, beavis).
mother(agatha, butthead).
father(ahmed, butthead).
male(bob).
married(beavis, bob).

%third oldest generation

%me & the wife
female(cinderella).
male(chip).
married(cinderella, chip).
mother(becky, cinderella).
father(beck, cinderella).
mother(barbie, chip).
father(bernard, chip).

%brother & sister
female(chyna).
male(chuck).
mother(barbie, chyna).
father(bernard, chyna).
mother(barbie, chuck).
father(bernard, chuck).
female(cruella).
married(cruella, chuck).
divorced(cruella, chuck).

%brother & sister (in-law)
female(clarissa).
male(cleatus).
mother(becky, clarissa).
father(beck, clarissa).
mother(becky, cleatus).
father(beck, cleatus).
male(clay).
married(clarissa, clay).

%first cousin (in-law)
female(cloris).
female(cleopatra).
mother(bernice, cloris).
father(benedict, cloris).
mother(bernice, cleopatra).
father(benedict, cleopatra).

%first cousin
female(collice).
male(clifton).
mother(beatrix, collice).
father(beauregard, collice).
mother(beatrix, clifton).
father(beauregard, clifton).

%second cousin
female(cordelia).
father(beavis, cordelia).
father(bob, cordelia).

%fourth oldest generation - youngest

%children
female(daisy).
female(dakota).
male(dylan).
father(chip, dakota).
mother(cinderella, dakota).
father(chip, daisy).
mother(cinderella, daisy).
father(chip, dylan).
mother(cinderella, dylan).

%nieces & nephews
female(delta).
male(demetrius).
father(chuck, delta).
mother(cruella, delta).
father(chuck, demetrius).
mother(cruella, demetrius).

%nieces & nephews (in-law)
male(denver).
male(derex).
mother(clarissa, derex).
father(clay, derex).
mother(clarissa, denver).
father(clay, denver).

%---------------------
%rules

aunt-uncle(X,Y) :- sibling(X,Z), parent(Z,Y).
%aunt-uncle(X,Y) :- cousin(Z,Y), parent(X,Z).
aunt(X,Y) :- sister(X,Z), parent(Z,Y).
%aunt(X,Y) :- cousin(Z,Y), mother(X,Z).
uncle(X,Y) :- brother(X,Z), parent(Z,Y).
%uncle(X,Y) :- cousin(Z,Y), father(X,Z).

grandaunt-uncle(X,Y) :- aunt-uncle(X,Z), parent(Z,Y).
grandaunt-uncle(X,Y) :- sibling(X,Z), grandparent(Z,Y).
grandaunt(X,Y) :- sister(X,Z), grandparent(Z,Y).
grandaunt(X,Y) :- aunt(X,Z), parent(Z,Y).
granduncle(X,Y) :- brother(X,Z), grandparent(Z,Y).
granduncle(X,Y) :- uncle(X,Z), parent(Z,Y).

cousin(X,Y) :- aunt-uncle(Z,Y), parent(Z,X).
first-cousin-removed(X,Y) :- grandaunt-uncle(Z,Y), parent(Z,X).
second-cousin(X,Y) :- first-cousin-removed(Z,Y), parent(Z,X).

niece-nephew(X,Y) :- sibling(Z,Y), parent(Z,X).
niece(X,Y) :- female(X), niece-nephew(X,Y).
nephew(X,Y) :- male(X), niece-nephew(X,Y).

married_to(X,Y) :- married(X,Y), not divorced(X,Y).
sibling(X,Y) :- parent(Z,X), parent(Z,Y), X\=Y.
parent(X,Y) :- mother(X,Y).
parent(X,Y) :- father(X,Y).
brother(X,Y) :- male(X), sibling(X,Y).
sister(X,Y) :- female(X), sibling(X,Y).
grandparent(X,Y) :- parent(X,Z), parent(Z,Y).
grandfather(X,Y) :- male(X), grandparent(X,Y).
grandmother(X,Y) :- female(X), grandparent(X,Y).

ancestor(X,Y) :- parent(X,Y).
ancestor(X,Y) :- parent(X,Z), ancestor(Z,Y).

% Two people are blood related if they share a common ancestor.
blood-related(X,Y) :- ancestor(Z,X), ancestor(Z,Y), X\=Y.
% If they do not, they can be considered in-laws assuming they are from the same family.
in-law(X,Y) :- not blood-related(X,Y).

printfam(X) :- ancestor(X,Y), write(Y), nl, fail.