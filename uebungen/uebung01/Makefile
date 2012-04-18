TEMPORARIES=*aux *toc *log *blg *bbl *out *lol *lot *lof
ARTIFACTS=*pdf *dvi *ps
LATEX_OPTS=-interaction=nonstopmode

all: clean compile removetemporaries

compile:
	latex $(LATEX_OPTS) uebung01.tex
#	bibtex8 report
#	latex $(LATEX_OPTS) uebung01.tex
#	latex $(LATEX_OPTS) uebung01.tex

pdf: compile removetemporaries
	dvips uebung01.dvi
	ps2pdf uebung01.ps
	rm -f *dvi *ps

clean:
	rm -f $(TEMPORARIES) $(ARTIFACTS)
	
removetemporaries:
	rm -f $(TEMPORARIES)
