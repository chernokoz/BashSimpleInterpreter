## Table of contents
* [General info](#general-info)
* [Supported commands](#supported-commands)
* [Examples](#examples)

## General info
This project is simple bash command line interpreter.

## Детали реализации
Главный цикл выполняется в классе Main, класс `Lexer` разбивает строку на список токенов,
класс `Parcer` отвечает за анализ списков токенов и создание команд. Токены бывают разных классов,
но все они имплементируют абстрактный класс `Token`. Команды имплементируют абстрактный класс 
`Command`. Класс `Environment` отвечает за состояние окружения - текущую директорию и набор
глобальных переменных.


#### Диаграмма классов
![rogue classes](./src/main/java/com/chernokoz/docs/cliClasses.png)

## Supported commands
* cat
* echo
* wc
* pwd
* exit
* also, interpreter support pipes and ";"
* and global variables

## Examples
```
$ echo 5 | echo 10
$ echo 5  ; echo 10
$ echo 123123 | wc | wc
$ echo 5 ; echo 10; exit; echo 15
$ a=5; echo $a
$ a=b; $a=5; echo $b
```
