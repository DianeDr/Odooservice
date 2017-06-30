@echo off
SET _HOME_EXT=%~d0%~p0XTVY
SET ODOO_HOME=%_HOME_EXT:\bin\XTVY=%
SET JAVA_HOME=%ODOO_HOME%\java
if not exist "%JAVA_HOME%\bin\java.exe" SET JAVA_HOME=C:\Program Files\Java\jre1.8.0_131
if not exist "%JAVA_HOME%\bin\java.exe" SET JAVA_HOME=c:\JDK 1.8.0

SET CLASSPATH=
rem SET UCLIENT_BASE=

if exist "%ODOO_HOME%\bin\setopts.bat" call "%ODOO_HOME%\bin\setopts.bat"

if "%1" == "" goto finish
@echo JAVA_HOME=%JAVA_HOME%
:finish
@echo on

