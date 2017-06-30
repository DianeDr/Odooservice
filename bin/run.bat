@echo off
SET _HOME=%~d0%~p0
call "%_HOME%setenv.bat"

for %%i in ("%ODOO_HOME%\lib\*.jar") do call "%ODOO_HOME%\bin\cpappend.bat" "%%i"
set CLASSPATH=%CLASSPATH%;

set CLASSPATH=%CLASSPATH%;%JAVA_HOME%\lib\tools.jar;%ODOO_HOME%/lib;
echo on
 "%JAVA_HOME%\bin\java" -DODOO_HOME="%ODOO_HOME%" -Xmx1024m com.cf.tkconnect.Odoo





