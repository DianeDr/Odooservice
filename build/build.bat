@echo off
SET _HOME=%~d0%~p0

call "%_HOME%setenv.bat"
C:\workspace\apache-ant-1.9.7\bin\ant %1




