CC=gcc
EMACS=emacs
INIT_EL=$(HOME)/.emacs.d/init.el
BATCH_EMACS=$(EMACS) --batch -Q -l $(INIT_EL) ex1.org
REQUIREMENTS=curl dot perl python R sh sqlite3

all: ex1.pdf

ex1.tex: ex1.org $(INIT_EL)
	$(BATCH_EMACS) -f org-export-as-latex

ex1.pdf: ex1.tex
