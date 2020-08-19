## Table of contents
* [General info](#general-info)
* [Supported commands](#supported-commands)
* [Examples](#examples)

## General info
This project is simple bash command line interpreter.

## Supported commands
* cat
* echo
* wc
* pwd
* exit
Also, interpreter support pipes and ";"

## Examples
```
$ echo 5 | echo 10
$ echo 5  ; echo 10
$ echo 123123 | wc | wc
$ echo 5 ; echo 10; exit; echo 15
$ a=5; echo $a
$ a=b; $a=5; echo $b
```
