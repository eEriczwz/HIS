@echo off
chcp 65001 >nul
REM ============================================================
REM  HIS 一键启动全部后端服务 (JDK 17 + Maven)
REM  双击运行即可；每个服务独立窗口，关闭对应窗口即停止该服务。
REM  启动前请先关闭之前已运行的旧服务窗口，避免端口被占用。
REM ============================================================

set "JAVA_HOME=C:\Users\23720\.jdks\ms-17.0.20.1"
set "PATH=%JAVA_HOME%\bin;%PATH%"
set "MVN_BIN=C:\Users\23720\.m2\wrapper\dists\apache-maven-3.9.16\56ba1f9f\bin"
set "PATH=%MVN_BIN%;%PATH%"

cd /d "%~dp0"

echo [1/6] 编译并安装全部模块 (his-common + 6 个服务) ...
call mvn -DskipTests install -q
if errorlevel 1 (
    echo.
    echo [错误] 构建失败，请查看上方输出后重试。
    pause
    exit /b 1
)

echo [2/6] 启动 挂号服务 his-registration :8093
start "his-registration-8093" cmd /k "mvn -pl his-registration spring-boot:run"

echo [3/6] 启动 药房服务 his-drugstore :8091
start "his-drugstore-8091" cmd /k "mvn -pl his-drugstore spring-boot:run"

echo [4/6] 启动 员工服务 his-employee :8094
start "his-employee-8094" cmd /k "mvn -pl his-employee spring-boot:run"

echo [5/6] 启动 门诊服务 his-outpatient :8092
start "his-outpatient-8092" cmd /k "mvn -pl his-outpatient spring-boot:run"

echo [6/6] 启动 门诊医生工作站 his-menzhen :8095
start "his-menzhen-8095" cmd /k "mvn -pl his-menzhen spring-boot:run"

echo [7/7] 启动 收费管理 his-charge :8096
start "his-charge-8096" cmd /k "mvn -pl his-charge spring-boot:run"

echo.
echo 全部服务已在新窗口启动。等待各窗口出现 "Started ...Application" 后即可使用前端。
echo 关闭某个窗口即停止对应服务。
pause
