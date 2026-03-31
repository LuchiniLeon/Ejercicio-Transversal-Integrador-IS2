#!/bin/bash

# Colores que se usarán a lo largo del script (Corregidos)
VERDE='\033[0;32m'
AZUL='\033[0;34m'
AMARILLO='\033[0;33m'
RESET='\033[0m'

# Instala todas las dependencias necesarias de una vez
# Si el usuario ya tiene alguna dependencia no la descarga
instalar() {
        echo -e "${AMARILLO}--- Iniciando comprobación de dependencias ---${RESET}"
	echo -e "${AZUL}Nota: Se solicitará tu contraseña de Linux para instalar paquetes faltantes.${RESET}"

        if command -v mvn &> /dev/null; then
                echo -e "${AMARILLO}Maven ya se encuentra instalado. Saltando este paso...${RESET}"
        else
                echo -e "${AMARILLO}Iniciando instalación de Maven...${RESET}"
                sudo apt update && sudo apt install -y maven
        fi

        if command -v sqlite3 &> /dev/null; then
                echo -e "${AMARILLO}SQL Lite ya se encuentra instalado. Saltando este proceso...${RESET}"
        else
                echo -e "${AMARILLO}Iniciando instalación de SQL Lite...${RESET}"
                sudo apt update && sudo apt install -y sqlite3
        fi

        echo -e "${AMARILLO}--- Entorno listo para trabajar ---${RESET}"
}

# Compila el proyecto y lo ejecuta
# Si el usuario no posee las dependencias necesarias pide que el usuario las instale
ejecutar() {
        if command -v mvn &> /dev/null; then
                if grep -q Microsoft /proc/version || grep -q "microsoft" /proc/version; then
                        cmd.exe /c start "http://localhost:8080" 2>/dev/null
                else
                        xdg-open http://localhost:8080 || echo -e "${AMARILLO}Por favor abre en el navegador http://localhost:8080${RESET}"
                fi

                mvn clean compile exec:java
        else
                echo -e "${AMARILLO}--- Por favor utilice el método instalar() para tener todas las dependencias necesaria ---${RESET}"
                echo -e "${AMARILLO}--- Para más información ejecuta: ./manage.sh help ---${RESET}"
        fi
}

# Bora archivos temporales y compilados
# Si el usuario no posee las dependencias necesarias pide que el usuario las instale
limpiar() {
        if command -v mvn &> /dev/null; then
                mvn clean
        else
                echo -e "${AMARILLO}--- Por favor utilice el método instalar() para tener todas las dependencias necesaria ---${RESET}"
                echo -e "${AMARILLO}--- Para más información ejecuta: ./manage.sh help ---${RESET}"
        fi
}

# Abre la base de datos
# Por defecto abre el archivo dev.db
# Si no abre el archivo prod.db si se pasa el comando de la siguiemte forma:
# ./manage.sh db prod
# Si el usuario no posee las dependencias necesarias pide que el usuario las instale
db() {
        if ! command -v sqlite3 &> /dev/null; then
                echo -e "${AMARILLO}--- Por favor utilice el método instalar() para tener todas las dependencias necesarias ---${RESET}"
                echo -e "${AMARILLO}--- Para más información ejecuta: ./manage.sh help ---${RESET}"
                return 1
        fi

        local ARCHIVO="db/dev.db"

        if [ "$1" == "prod" ]; then
                ARCHIVO="db/prod.db"
                echo -e "${AMARILLO}--- ENTRANDO A LA BASE DE DATOS DE prod ---${RESET}"
        else
                echo -e "${AMARILLO}--- ENTRANDO A LA BASE DE DATOS DE dev ---${RESET}"
        fi

        if [ -f "$ARCHIVO" ]; then
                sqlite3 "$ARCHIVO"
        else
                echo -e "${AZUL}[ERROR] No se pudo encontrar el archivo $ARCHIVO${RESET}"
        fi
}

# Ejecuta los test unitarios
# Si el usuario no posee las dependencias necesarias pide que el usuario las instale
test() {
        if command -v mvn &> /dev/null; then
		echo -e "${AMARILLO}Ejecutando pruebas unitarias...${RESET}"
		mvn test
        else
                echo -e "${AMARILLO}--- Por favor utilice el método instalar() para tener todas las dependencias necesarias ---${RESET}"
                echo -e "${AMARILLO}--- Para más información ejecuta: ./manage.sh help ---${RESET}"
        fi
}

help() {
        echo -e "${AMARILLO}==============================================================${RESET}"
        echo -e "${AZUL}   CENTRO DE MANDO - EJERCICIO INTEGRADOR IS2${RESET}"
        echo -e "${AMARILLO}==============================================================${RESET}"

        printf "${VERDE}%-15s %-40s${RESET}\n" "Comando" "Funcionalidad"
        echo -e "${AMARILLO}--------------------------------------------------------------${RESET}"
        printf "%-15s %-40s\n" "instalar" "Instala todas las dependencias que falten"
        printf "%-15s %-40s\n" "ejecutar" "Compila y Ejecuta el proyecto"
        printf "%-15s %-40s\n" "limpiar"  "Borra archivos temporales y compilados"
        printf "%-15s %-40s\n" "db [prod]" "Abre la base de datos prod.db"
        printf "%-15s %-40s\n" "db" "Abre por defecto la base de datos dev.db"
        printf "%-15s %-40s\n" "help" "Muestra este menú de ayuda"

        echo -e "${AMARILLO}--------------------------------------------------------------${RESET}"
        echo -e "Uso: ${VERDE}./manage.sh [comando]${RESET}"
        echo -e "${AMARILLO}==============================================================${RESET}"
}

case "$1" in
        "instalar") instalar ;;
        "ejecutar") ejecutar ;;
	"limpiar") limpiar ;;
	"db") db "$2" ;;
	"test") test ;;
        "help") help ;;
        *)      help ;;
esac
