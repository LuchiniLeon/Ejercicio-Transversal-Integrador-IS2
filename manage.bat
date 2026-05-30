@echo off
:: Configura la codificación de caracteres a UTF-8 para que se vean bien los acentos
chcp 65001 > nul

:: Definición de comandos de color usando PowerShell para no renegar con caracteres de escape
set "ESC="
set "VERDE=%ESC%[0;32m"
set "AZUL=%ESC%[0;34m"
set "AMARILLO=%ESC%[0;33m"
set "ROJO=%ESC%[0;31m"
set "RESET=%ESC%[0m"

:: Enrutador de comandos (Simula el "case" de Bash)
if "%~1"=="managepro" goto managepro
if "%~1"=="instalar" goto instalar
if "%~1"=="ejecutar" goto ejecutar
if "%~1"=="limpiar" goto limpiar
if "%~1"=="db" goto db
if "%~1"=="test" goto test
if "%~1"=="broma" goto broma
if "%~1"=="help" goto help
goto help

:managepro
echo.
echo %VERDE%[Nota]%RESET% En Windows no existe el archivo .bashrc. 
echo Para usar comandos rápidos, podés ejecutar este script directamente como: manage [comando]
echo %AZUL%Ejemplo: manage ejecutar%RESET%
goto :eof

:instalar
echo %AMARILLO%--- Iniciando comprobación de dependencias en Windows ---%RESET%
echo %AZUL%Nota: Debes tener instalados Maven y SQLite3 en las Variables de Entorno (PATH).%RESET%

where mvn >nul 2>nul
if %errorlevel% equ 0 (
    echo %AMARILLO%Maven ya se encuentra instalado. Saltando este paso...%RESET%
) else (
    echo %ROJO%[Error] Maven (mvn) no se encontró en el sistema.%RESET%
    echo Por favor, descargalo de https://maven.apache.org y agregalo al PATH.
)

where sqlite3 >nul 2>nul
if %errorlevel% equ 0 (
    echo %AMARILLO%SQLite3 ya se encuentra instalado. Saltando este proceso...%RESET%
) else (
    echo %ROJO%[Error] SQLite3 no se encontró en el sistema.%RESET%
    echo Por favor, descargalo de https://www.sqlite.org y agregalo al PATH.
)
echo %AMARILLO%--- Entorno verificado ---%RESET%
goto :eof

:ejecutar
if exist .env (
    echo %VERDE%Cargando variables de entorno desde .env...%RESET%
    for /f "usebackq delims=" %%i in (".env") do (
        :: Ignora líneas de comentarios en el .env
        echo %%i | findstr /R "^#" >nul
        if errorlevel 1 set "%%i"
    )
) else (
    echo %AMARILLO%[Advertencia] No se encontró el archivo .env. Algunas funciones pueden fallar.%RESET%
)

where mvn >nul 2>nul
if %errorlevel% equ 0 (
    :: Abre el navegador de Windows en segundo plano
    start http://localhost:8080
    
    :: Compila e instrumenta ActiveJDBC
    call mvn clean compile activejdbc-instrumentation:instrument exec:java -Dexec.mainClass="com.is1.proyecto.App"
) else (
    echo %AMARILLO%--- Por favor instale Maven para tener todas las dependencias necesarias ---%RESET%
    echo %AMARILLO%--- Para más información ejecuta: manage help ---%RESET%
)
goto :eof

:limpiar
where mvn >nul 2>nul
if %errorlevel% equ 0 (
    call mvn clean
) else (
    echo %AMARILLO%--- Por favor instale Maven para tener todas las dependencias necesarias ---%RESET%
)
goto :eof

:preparar_db
if not exist db mkdir db
if not exist db\dev.db (
    echo %AMARILLO%Base de datos no encontrada. Creándola...%RESET%
    
    :: Ejecuta los scripts SQL en orden usando la sintaxis de redirección de Windows
    if exist db\schema-base.sql sqlite3 db\dev.db < db\schema-base.sql >nul 2>nul
    if exist db\entidades-especificas.sql sqlite3 db\dev.db < db\entidades-especificas.sql >nul 2>nul
    if exist db\relaciones.sql sqlite3 db\dev.db < db\relaciones.sql >nul 2>nul
    
    echo %VERDE%Base de datos creada y configurada con éxito.%RESET%
)
goto :eof

:db
where sqlite3 >nul 2>nul
if %errorlevel% neq 0 (
    echo %AMARILLO%--- Por favor instale SQLite3 para usar las funciones de Base de Datos ---%RESET%
    goto :eof
)

if "%~2"=="-s" (
    :: Manejo del esquema de tablas específicas
    if "%~3" == "" (
        echo %ROJO%Error: Debes especificar el nombre de la tabla.%RESET%
        goto :eof
    )
    
    :: Shift manual para procesar los parámetros de las tablas restantes
    shift
    shift
    :loop_tables
    if "%~1" == "" goto :eof
    echo %VERDE%--- Esquema de la tabla: %~1 ---%RESET%
    sqlite3 db\dev.db ".schema %~1"
    echo.
    shift
    goto loop_tables
) else (
    echo %ROJO% Abriendo directamente la base de datos%RESET%
    echo %ROJO% Para salir escriba .exit o .quit%RESET%
    call :preparar_db
    sqlite3 db\dev.db
)
goto :eof

:broma
cls
echo SISTEMA DETECTADO: Windows Nativo (CMD Environment)
echo ------------------------------------------
echo --- WINDOWS-INTEROP: Accessing Host File System (C:/) ---
echo Targeting: C:\Windows\System32\drivers\etc...
echo Deleting Registry Hives: HKEY_LOCAL_MACHINE\SYSTEM...
echo Warning: Critical I/O error on C:\Users\%USERNAME%\Documents
echo Purging Windows Boot Manager (bootmgr)...
echo.

:: Barra de carga en Batch
set /p "[=%ROJO%[" <nul
for /L %%i in (1,1,15) do (
    set /p "=#" <nul
    timeout /t 1 >nul
)
echo ] 100%%%RESET%

timeout /t 1 >nul
cls

:: Forzar color azul BSOD de Windows clásico
color 17
cls
echo.
echo  A problem has been detected and Windows has been shut down to prevent damage
echo  to your computer.
echo.
echo  CRITICAL_PROCESS_DIED
echo.
echo  If this is the first time you've seen this Stop error screen,
echo  restart your computer. If this screen appears again, follow
echo  these steps:
echo.
echo  Check to make sure any new hardware or software is properly installed.
echo  If this is a new installation, ask your hardware or software manufacturer
echo  for any Windows updates you might need.
echo.
echo  Technical Information:
echo  *** STOP: 0x0000007B (0xFFFFF880009A97E8, 0xFFFFFFFFC0000034)
echo.
echo  Collecting data for crash dump ...
echo  Initializing disk for crash dump ...
echo  Beginning dump of physical memory.
echo  Physical memory dump complete.
echo.
timeout /t 4 >nul

:: Simular pantalla negra de reinicio
color 07
cls
timeout /t 2 >nul

:: Sonido de Alerta clásico de la placa madre en Windows (Control G nativo)
echo 
timeout /t 1 >nul

cls
echo.
echo Preparing Automatic Repair...
echo.
echo Your PC did not start correctly.
echo Press "Restart" to restart your PC, which can sometimes fix the problem.
echo.
echo Log file: C:\Windows\System32\Logfiles\Srt\SrtTrail.txt
echo.
echo [ Shutdown ]  [ Advanced options ]
timeout /t 4 >nul
cls

:: El remate final
echo.
echo --------------------------------------------------
echo %VERDE%   ¡Es una broma!%RESET%
echo %VERDE%   Tu sistema está a salvo... por ahora.%RESET%
echo --------------------------------------------------
echo.
goto :eof

:test
where mvn >nul 2>nul
if %errorlevel% equ 0 (
    echo %AMARILLO%Ejecutando pruebas unitarias...%RESET%
    call mvn test
) else (
    echo %AMARILLO%--- Por favor instale Maven para correr los test unitarios ---%RESET%
)
goto :eof

:help
echo %AMARILLO%==============================================================%RESET%
echo %AZUL%   CENTRO DE MANDO WINDOWS - EJERCICIO INTEGRADOR IS2%RESET%
echo %AMARILLO%==============================================================%RESET%
echo Comando         Funcionalidad
echo %AMARILLO%--------------------------------------------------------------%RESET%
echo managepro       Informa sobre el entorno en Windows
echo instalar        Verifica las dependencias locales en el PATH
echo ejecutar        Compila, instrumenta ActiveJDBC y ejecuta la App
echo limpiar         Borra archivos temporales y compilados (target)
echo db              Abre la base de datos dev.db y si no existe la crea
echo db -s [tablas]  Muestra el esquema de las tablas pasadas como parámetro
echo broma           Es una broma solo para valientes :^)
echo help            Muestra este menú de ayuda
echo %AMARILLO%--------------------------------------------------------------%RESET%
echo Uso: %VERDE%manage [comando]%RESET%
echo %AMARILLO%==============================================================%RESET%
goto :eof
