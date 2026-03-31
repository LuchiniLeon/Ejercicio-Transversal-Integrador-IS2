@echo off
setlocal enabledelayedexpansion
:: Forma infalible para colores en .bat (Windows es muy tedioso)
for /F "tokens=1,2 delims=#" %%a in ('"prompt #$H#$E# & echo on & for %%b in (1) do rem"') do set "ESC=%%b"
set "VERDE=%ESC%[0;32m"
set "AZUL=%ESC%[0;34m"
set "AMARILLO=%ESC%[0;33m"
set "RESET=%ESC%[0m

:: Logica de ruteo (el "case" de manage.sh)
	if "%1"=="instalar" goto instalar
	if "%1"=="ejecutar" goto ejecutar
	if "%1"=="limpiar"  goto limpiar
	if "%1"=="db"       goto db
	if "%1"=="test"     goto test
	if "%1"=="help"     goto help
	goto help


:: Instala todas las dependencias necesarias de una vez
:: Si el usuario ya tiene alguna dependencia no la descarga
:instalar

	echo %AMARILLO%--- Iniciando comprobacion de dependencias ---%RESET%
	echo %AZUL%Nota: En Windows debes tener Maven y SQLite en el PATH manualmente.%RESET%

	where mvn >nul 2>nul
		if %errorlevel% equ 0 (
    			echo %AMARILLO%Maven ya se encuentra instalado. Saltando este paso...%RESET%
		) else (
    			echo %AMARILLO%Maven no encontrado. Por favor, instalalo desde https://maven.apache.org/%RESET%
		)

	where sqlite3 >nul 2>nul
		if %errorlevel% equ 0 (
    			echo %AMARILLO%SQL Lite ya se encuentra instalado. Saltando este proceso...%RESET%
		) else (
    			echo %AMARILLO%SQL Lite no encontrado. Por favor, instalalo desde https://www.sqlite.org/%RESET%
		)

	echo %AMARILLO%--- Entorno listo para trabajar ---%RESET%
goto :eofo



:: Compila el proyecto y lo ejecuta
:: Si el usuario no posee las dependencias necesarias pide que el usuario las instale
:ejecutar
	where mvn >nul 2>nul
		if %errorlevel% equ 0 (
    			start http://localhost:8080
    			mvn clean compile exec:java
		) else (
    			echo %AMARILLO%--- Por favor utilice el metodo instalar^^(^) para tener todas las dependencias necesaria ---%RESET%
    			echo %AMARILLO%--- Para más informacion ejecuta: manage.bat help ---%RESET%
		)
goto :eof


:: Bora archivos temporales y compilados
:: Si el usuario no posee las dependencias necesarias pide que el usuario las instale
:limpiar
	where mvn >nul 2>nul
		if %errorlevel% equ 0 (
    			mvn clean
		) else (
    			echo %AMARILLO%--- Por favor utilice el metodo instalar^^(^) para tener todas las dependencias necesaria ---%RESET%
    			echo %AMARILLO%--- Para más informacion ejecuta: manage.bat help ---%RESET%
		)
goto :eof


:: Abre la base de datos
:: Por defecto abre el archivo dev.db
:: Si no abre el archivo prod.db si se pasa el comando de la siguiemte forma:
:: .\manage.bat db prod
:: Si el usuario no posee las dependencias necesarias pide que el usuario las instale
:db
	where sqlite3 >nul 2>nul
		if %errorlevel% neq 0 (
    			echo %AMARILLO%--- Por favor utilice el metodo instalar^^(^) para tener todas las dependencias necesarias ---%RESET%
    			echo %AMARILLO%--- Para más informacion ejecuta: manage.bat help ---%RESET%
    		goto :eof
	)

	set "ARCHIVO=db\dev.db"

	if "%2"=="prod" (
    		set "ARCHIVO=db\prod.db"
    		echo %AMARILLO%--- ENTRANDO A LA BASE DE DATOS DE prod ---%RESET%
	) else (
    		echo %AMARILLO%--- ENTRANDO A LA BASE DE DATOS DE dev ---%RESET%
	)

	if exist %ARCHIVO% (
    		sqlite3 %ARCHIVO%
	) else (
    		echo %AZUL%[ERROR] No se pudo encontrar el archivo %ARCHIVO%%RESET%
	)
goto :eof


:: Ejecuta los test unitarios
:: Si el usuario no posee las dependencias necesarias pide que el usuario las instale
:test
	where mvn >nul 2>nul
		if %errorlevel% equ 0 (
    			echo %AMARILLO%Ejecutando pruebas unitarias...%RESET%
    			mvn test
		) else (
    			echo %AMARILLO%--- Por favor utilice el metodo instalar^^(^) para tener todas las dependencias necesarias ---%RESET%
    			echo %AMARILLO%--- Para más informacion ejecuta: manage.bat help ---%RESET%
		)
goto :eof



:help
	echo %AMARILLO%==============================================================%RESET%
	echo %AZUL%   CENTRO DE MANDO - EJERCICIO INTEGRADOR IS2 ^(WINDOWS^)%RESET%
	echo %AMARILLO%==============================================================%RESET%
	echo Comando         Funcionalidad
	echo %AMARILLO%--------------------------------------------------------------%RESET%
	echo instalar        Instrucciones de instalacion
	echo ejecutar        Compila y Ejecuta el proyecto
	echo limpiar         Borra archivos temporales y compilados
	echo db [prod]       Abre la base de datos prod.db
	echo db              Abre por defecto la base de datos dev.db
	echo help            Muestra este menu de ayuda
	echo %AMARILLO%--------------------------------------------------------------%RESET%
	echo Uso: %VERDE%manage.bat [comando]%RESET%
	echo %AMARILLO%==============================================================%RESET%
goto :eof
