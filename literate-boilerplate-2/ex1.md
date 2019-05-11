<div id="table-of-contents">
<h2>Table of Contents</h2>
<div id="text-table-of-contents">
<ul>
<li><a href="#sec-1">1. Introduction</a></li>
<li><a href="#sec-2">2. Can org-mode Tangle Produce Directories?</a></li>
<li><a href="#sec-3">3. Files in the Project Directory</a>
<ul>
<li><a href="#sec-3-1">3.1. .Gitignore</a></li>
<li><a href="#sec-3-2">3.2. README.md</a></li>
<li><a href="#sec-3-3">3.3. project.clj</a></li>
</ul>
</li>
<li><a href="#sec-4">4. The Documentation Subdirectory</a></li>
<li><a href="#sec-5">5. Core Source File</a></li>
<li><a href="#sec-6">6. Core Unit-Test File</a></li>
<li><a href="#sec-7">7. Conclusion</a></li>
</ul>
</div>
</div>

# Introduction

Did you know that \(x^2=-1\) has no solution over the real numbers,
\(\mathbb{R}\)? That's just a demonstration of doing math in \(\LaTeX{}\)
in org-mode.

Figure \ref{fig:fufortune} is an example of including an image
picture. Figures \ref{fig:equalizer} and \ref{fig:pullback} are
examples of doing \verb|TikZ| graphics.

\begin{figure}
  \centering
  \includegraphics[width=0.5\textwidth]{FuFortune2.png}
  \caption{\label{fig:fufortune}This means ``Fortune'' and is pronounced ``Foo''.}
\end{figure}

\begin{figure}
  \centering
\begin{tikzpicture}
  \node (A) {$B$};
  \node (B) [right of=A] {$C$};
  \draw[->] (A.20) to node {$g$} (B.160);
  \draw[->] (A.340) to node [swap] {$h$} (B.200);
\end{tikzpicture}
  \caption{\label{fig:equalizer}This is the kernel of an \emph{equalizer}.}
\end{figure}

\begin{figure}
  \centering
\begin{tikzpicture}
  \node (P) {$P$};
  \node (B) [right of=P] {$B$};
  \node (A) [below of=P] {$A$};
  \node (C) [below of=B] {$C$};
  \node (P1) [node distance=1.4cm, left of=P, above of=P] {$\hat{P}$};
  \draw[->] (P) to node {$\bar{f}$} (B);
  \draw[->] (P) to node [swap] {$\bar{g}$} (A);
  \draw[->] (A) to node [swap] {$f$} (C);
  \draw[->] (B) to node {$g$} (C);
  \draw[->, bend right] (P1) to node [swap] {$\hat{g}$} (A);
  \draw[->, bend left] (P1) to node {$\hat{f}$} (B);
  \draw[->, dashed] (P1) to node {$k$} (P);
\end{tikzpicture}
  \caption{\label{fig:pullback}This is a pullback diagram.}
\end{figure}

\begin{figure}
  \centering
  \tikzset{%
    commutative diagrams/.cd,
    arrow style=tikz,
    diagrams={>=open triangle 45, line width=tikzcdrule}}
  \begin{tikzcd}
    A \arrow{r} \arrow{d} & B \arrow{d}\\
    C \arrow{r} & D
  \end{tikzcd}
  \caption{\label{fig:tikz-cd}This is a tikz-cd diagram.}
\end{figure}

We want to automate the production of a Leiningen project tree
entirely from an org-mode babel file. We want to do this so that we
can also create beautiful, typeset documentation via
\verb+org-latex-export-to-pdf+. We want no less than full literate
programming in Clojure from org-mode.

# Can org-mode Tangle Produce Directories?

The following command:

    $ lein new ex1

produces a tree that looks like this:

    ex1
    ex1/.gitignore
    ex1/doc
    ex1/doc/intro.md
    ex1/project.clj
    ex1/README.md
    ex1/resources
    ex1/src
    ex1/src/ex1
    ex1/src/ex1/core.clj
    ex1/test
    ex1/test/ex1
    ex1/test/ex1/core_test.clj

We want to do the identical thing just by running
\verb+org-babel-tangle+, and no more, in our org-mode buffer in
emacs.

A difficulty arises: whereas \verb+tangle+ is happy to produce
files, it seems reluctant to produce directories. That means we must
create the directory structure by some other means &#x2013; unless we can
get \verb+tangle+ to do it for us, and that's the subject of this
*StackOverflow* question.

Let's write the org-mode file we want, and then see exactly where
\verb+org-babel-tangle+ falls over.

# Files in the Project Directory

In our example, the top-level directory doesn't have a name &#x2013; put
our org file in that directory. The Leiningen project directory
should have the same name as our org file. Our org file is named
\verb+ex1.org+ and we want a directory tree rooted at \verb+ex1+
exactly as above.

Start with the contents of the project directory, \verb+ex1+. Each
org-mode babel source-code block will name a file path &#x2013; including
sub-directories &#x2013; after a \verb+:tangle+ keyword on the
\texttt{\#+BEGIN\_SRC} command of org-mode.

## .Gitignore

First, we must create the \verb+.gitignore+ file that tells
\verb+git+ not to check in and push the ephemeral *ejecta* of build
processes like \verb+maven+ and \verb+javac+.

    /target
    /lib
    /classes
    /checkouts
    pom.xml
    pom.xml.asc
    *.jar
    *.class
    .lein-deps-sum
    .lein-failures
    .lein-plugins
    .lein-repl-history

## README.md

Next, we produce the default \verb+README.md+ in \verb+markdown+
syntax for the entire project:

    # ex1
    
    A Clojure library designed to ... well, that part is up to you.
    
    ## Usage
    
    FIXME
    
    ## License
    
    Copyright © 2013 FIXME
    
    Distributed under the Eclipse Public License, the same as Clojure.

## project.clj

Next is the \verb+project.clj+ file required by Leiningen for fetching
dependencies and doing various, valuable housekeeping and
configuration tasks.

    (defproject ex1 "0.1.0-SNAPSHOT"
      :description "FIXME: write description"
      :url "http://example.com/FIXME"
      :license {:name "Eclipse Public License"
                :url "http://www.eclipse.org/legal/epl-v10.html"}
      :dependencies [[org.clojure/clojure "1.5.1"]])

# The Documentation Subdirectory

Mimicking Leiningen's documentation subdirectory, it contains the
single file \verb+intro.md+, again in \verb+markdown+ syntax. The
following contains an intentionally long line:

    # Introduction to ex1
    
    TODO: write [great documentation](http://jacobian.org/writing/great-documentation/what-to-write/)

# Core Source File

By convention, the core source files go in a subdirectory named
\verb+./ex1/src/ex1+. This convention allows the clojure namespaces
to map to java packages.

Here is our core source file

    (ns ex1.core)
    
    (defn foo
      "I don't do a whole lot."
      [x]
      (println x "Hello, World!"))

# Core Unit-Test File

Finally, the unit-testing files go in a subdirectory named
\verb+./ex1/test/ex1+. Again, the directory-naming convention
enables valuable shortcuts from Leiningen.

    (ns ex1.core-test
      (:require [clojure.test :refer :all]
                [ex1.core :refer :all]))
    
    (deftest a-test
      (testing "FIXME, I fail."
        (is (= 0 1))))

# Conclusion

All is well if our directory structure already exists. org-mode's
\`tangle\` will create or update all six files described above and
create new files in any existing directory. We don't know how to get
\`tangle\` to produce the directories; it complains that there is no
such directory.
