@echo off
rem ---------------------------------------------------------------------------
rem Append to CLASSPATH
rem
rem $Id: cpappend.bat,v 1.5 2002/02/13 05:57:08 patrickl Exp $
rem ---------------------------------------------------------------------------
rem usage: call cpappend "arg1" "arg2" ...
rem Note: recommend double quote the arguments in case there spaces inside the args
rem Process the first argument
if ""%1"" == """" goto end
SET _ARG_TEMP=%1
SET _ARG_TEMP=%_ARG_TEMP:"=%
set CLASSPATH=%CLASSPATH%;%_ARG_TEMP%
shift

rem Process the remaining arguments
:setArgs
if ""%1"" == """" goto doneSetArgs
SET _ARG_TEMP=%1
SET _ARG_TEMP=%_ARG_TEMP:"=%
set CLASSPATH=%CLASSPATH%;%_ARG_TEMP%
shift
goto setArgs
:doneSetArgs
:end
