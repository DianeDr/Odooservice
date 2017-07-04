@echo on
SET _HOME_EXT=%~d0%~p0XTVY
SET UINT_HOME=%_HOME_EXT:\build\XTVY=%
SET JAVA_HOME=C:\Program Files\Java\jdk1.8.0_121


SET CLASSPATH=
rem SET UCLIENT_BASE=

if exist "%UINT_HOME%\build\setopts.bat" call "%UINT_HOME%\build\setopts.bat"
set ANT_HOME=C:\workspace\apache-ant-1.9.7
if "%1" == "" goto finish
@echo JAVA_HOME=%JAVA_HOME%
:finish
@echo on

