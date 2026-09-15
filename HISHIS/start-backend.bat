@echo off
REM ============================================
REM  Start HIS outpatient backend (JDK 17)
REM  Double-click to run. Press Ctrl+C to stop.
REM ============================================

set "JAVA_HOME=C:\Users\23720\.jdks\ms-17.0.20.1"
set "PATH=%JAVA_HOME%\bin;%PATH%"

REM cd to this script's own folder (HISHIS)
cd /d "%~dp0"

"C:\Users\23720\.m2\wrapper\dists\apache-maven-3.9.16\56ba1f9f\bin\mvn.cmd" -pl his-outpatient spring-boot:run

pause
