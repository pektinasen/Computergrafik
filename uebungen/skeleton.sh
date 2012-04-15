#!/bin/bash

if ! ([[ $# -eq 1 ]] & [[ "$1" =~ ^[0-9]+$ ]])
then
echo "Usage: skeleton.sh <number of assignment (e.g. \"01\")>"
exit
fi

# Some variables.
NUMBER=$1
DIRECTORY=uebung$NUMBER
ASSIGNMENT=CG$NUMBER.pdf
ASSIGNMENT_URL=http://www.inf.fu-berlin.de/lehre/SS12/Computergrafik/$ASSIGNMENT
SAVE_AS=$DIRECTORY/$ASSIGNMENT

MAKEFILE=$DIRECTORY/Makefile
LATEX_BASE=$DIRECTORY
LATEX_FILE=$DIRECTORY/$LATEX_BASE.tex

# Create directory
mkdir -p $DIRECTORY

# Download assignment pdf.
wget $ASSIGNMENT_URL -O $SAVE_AS &>/dev/null
if ! [[ $? -eq 0 ]]
then
echo "WARNING: Could not download assignment pdf."
fi

# Create makefile.
if [[ -e $MAKEFILE ]]
then
echo "WARNING: Makefile already exists."
else

cat <<EOF > $MAKEFILE
TEMPORARIES=*aux *toc *log *blg *bbl *out *lol *lot *lof
ARTIFACTS=*pdf *dvi *ps
LATEX_OPTS=-interaction=nonstopmode

all: clean compile removetemporaries

compile:
	latex \$(LATEX_OPTS) $LATEX_BASE.tex
#	bibtex8 report
#	latex \$(LATEX_OPTS) $LATEX_BASE.tex
#	latex \$(LATEX_OPTS) $LATEX_BASE.tex

pdf: compile removetemporaries
	dvips $LATEX_BASE.dvi
	ps2pdf $LATEX_BASE.ps
	rm -f *dvi *ps

clean:
	rm -f \$(TEMPORARIES) \$(ARTIFACTS)
	
removetemporaries:
	rm -f \$(TEMPORARIES)
EOF

fi

# Create latex template.
if [[ -e $LATEX_FILE ]]
then
echo "WARNING: Latex file already exists."
else

cat <<EOF > $LATEX_FILE
\documentclass[a4paper]{scrartcl}
\usepackage[utf8]{inputenc}  
\usepackage[T1]{fontenc}         
\usepackage[ngerman]{babel}

\usepackage{graphicx} % For includegraphics.
\usepackage{listings} % For code listings.

\usepackage[
pdftitle={Computergrafik: Übung $NUMBER},
pdfsubject={Abgabe zu Übung $NUMBER},
pdfauthor={Sascha Gennrich (4301150), Malte Rohde (4287463)}
]{hyperref}

\title{Computergrafik: Übung $NUMBER}
\author{Sascha Gennrich (4301150), Malte Rohde (4287463)}
\date{\today{}}

\begin{document}
\maketitle

\section*{Aufgabe 1}

\end{document}
EOF

fi

