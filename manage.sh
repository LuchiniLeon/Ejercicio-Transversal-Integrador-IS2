#!/usr/bin/env bash

# Colores que se usaran a lo largo del script (Corregidos)
VERDE='\033[0;32m'
AZUL='\033[0;34m'
AMARILLO='\033[0;33m'
RESET='\033[0m'


managepro(){

        CONFIG_FILE="$HOME/.bashrc"
        if [ -f "$HOME/.zshrc" ]; then
                CONFIG_FILE="$HOME/.zshrc"
        fi

        # Guardamos la ruta en RUTA_ABSOLUTA
        RUTA_ABSOLUTA="$(realpath "$0")"

        echo "" >> "$CONFIG_FILE"
        echo "# Comandos rápidos de manage.sh" >> "$CONFIG_FILE"
        
        
        echo "alias manage='$RUTA_ABSOLUTA'" >> "$CONFIG_FILE"
        echo "alias instalar='$RUTA_ABSOLUTA instalar'" >> "$CONFIG_FILE"
        echo "alias ejecutar='$RUTA_ABSOLUTA ejecutar'" >> "$CONFIG_FILE"
        echo "alias limpiar='$RUTA_ABSOLUTA limpiar'" >> "$CONFIG_FILE"
        echo "alias db='$RUTA_ABSOLUTA db'" >> "$CONFIG_FILE"
        echo "alias test='$RUTA_ABSOLUTA test'" >> "$CONFIG_FILE"

        echo -e "${VERDE}¡Instalación exitosa!${RESET}"
        echo -e "${AZUL}Ahora debes reiniciar la terminal o ejecutar: source $CONFIG_FILE${RESET}"
}

# Instala todas las dependencias necesarias de una vez
# Si el usuario ya tiene alguna dependencia no la descarga
instalar() {
        echo -e "${AMARILLO}--- Iniciando comprobacion de dependencias ---${RESET}"
	echo -e "${AZUL}Nota: Se solicitara tu contrasena de Linux para instalar paquetes faltantes.${RESET}"

        if command -v mvn &> /dev/null; then
                echo -e "${AMARILLO}Maven ya se encuentra instalado. Saltando este paso...${RESET}"
        else
                echo -e "${AMARILLO}Iniciando instalacion de Maven...${RESET}"
                sudo apt update && sudo apt install -y maven
        fi

        if command -v sqlite3 &> /dev/null; then
                echo -e "${AMARILLO}SQL Lite ya se encuentra instalado. Saltando este proceso...${RESET}"
        else
                echo -e "${AMARILLO}Iniciando instalacion de SQL Lite...${RESET}"
                sudo apt update && sudo apt install -y sqlite3
        fi

        echo -e "${AMARILLO}--- Entorno listo para trabajar ---${RESET}"
}

# Compila el proyecto y lo ejecuta
# Si el usuario no posee las dependencias necesarias pide que el usuario las instale
ejecutar() {
        
        if [ -f .env ]; then
                echo -e "${VERDE}Cargando variables de entorno desde .env...${RESET}"
                set -a # Exporta automáticamente todas las variables definidas a continuación
                source .env
                set +a # Desactiva la exportación automática
        else
                echo -e "${AMARILLO}[Advertencia] No se encontró el archivo .env. Algunas funciones pueden fallar.${RESET}"
        fi

        if command -v mvn &> /dev/null; then
                if grep -q Microsoft /proc/version || grep -q "microsoft" /proc/version; then
                        cmd.exe /c start "http://localhost:8080" 2>/dev/null
                else
                        xdg-open http://localhost:8080 || echo -e "${AMARILLO}Por favor abre en el navegador http://localhost:8080${RESET}"
                fi
                #rm -f ./db/dev.db
                mvn clean compile activejdbc-instrumentation:instrument exec:java -Dexec.mainClass="com.is1.proyecto.App"
        else
                echo -e "${AMARILLO}--- Por favor utilice el metodo instalar() para tener todas las dependencias necesaria ---${RESET}"
                echo -e "${AMARILLO}--- Para mas informacion ejecuta: ./manage.sh help ---${RESET}"
        fi
}

# Bora archivos temporales y compilados
# Si el usuario no posee las dependencias necesarias pide que el usuario las instale
limpiar() {
        if command -v mvn &> /dev/null; then
                mvn clean
        else
                echo -e "${AMARILLO}--- Por favor utilice el metodo instalar() para tener todas las dependencias necesaria ---${RESET}"
                echo -e "${AMARILLO}--- Para mas informacion ejecuta: ./manage.sh help ---${RESET}"
        fi
}

preparar_db() {
    local ARCHIVO="db/dev.db"
    
    # Si no existe la carpeta db, la creamos
    mkdir -p db

    if [ ! -f "$ARCHIVO" ]; then
        echo -e "${AMARILLO}Base de datos no encontrada. Creándola...${RESET}"
        
        # Cargamos los esquemas en orden. 
        # Usamos -f para que si un archivo falta, no se rompa todo el proceso
        sqlite3 "$ARCHIVO" < db/schema-base.sql 2>/dev/null
        sqlite3 "$ARCHIVO" < db/entidades-especificas.sql 2>/dev/null
        sqlite3 "$ARCHIVO" < db/relaciones.sql 2>/dev/null
        
        echo -e "${VERDE}Base de datos creada y configurada con éxito.${RESET}"
    fi
}

# Abre la base de datos
# Por defecto abre el archivo dev.db
# Si no abre el archivo prod.db si se pasa el comando de la siguiemte forma:
# ./manage.sh db prod
# Si el usuario no posee las dependencias necesarias pide que el usuario las instale
db() {
        if ! command -v sqlite3 &> /dev/null; then
                echo -e "${AMARILLO}--- Por favor utilice el metodo instalar() para tener todas las dependencias necesarias ---${RESET}"
                echo -e "${AMARILLO}--- Para mas informacion ejecuta: ./manage.sh help ---${RESET}"
                return 1
        fi

        local ARCHIVO="db/dev.db"

        if [[ "$1" == "-s" ]]; then
                shift

                local TABLES=("$@")

                if [[ "${#TABLES[@]}" -eq 0 ]]; then
                        echo -e "${ROJO}Error: Debes especificar el nombre de la tabla.${RESET}"
                        return 1
                fi

                for TABLE in "${TABLES[@]}"
                do      
                        echo -e "${VERDE}---Esquema de la tabla: $TABLE ---${RESET}" 
                        sqlite3 "$ARCHIVO" ".schema $TABLE"
                        echo ""
                done

        else 
                echo -e "${ROJO} Abriendo directamente la base de datos${RESET}"
                echo -e "${ROJO} Para salir escriba .exit o .quit"
                preparar_db
                
                sqlite3 "$ARCHIVO"

                return 0
       fi
}



broma(){
    # Colores (Asegúrate de definirlos arriba o usarlos así)
    ROJO='\033[0;31m'
    VERDE='\033[0;32m'
    RESET='\033[0m'

    OS_TYPE="$(uname -s)"
    KERNEL_INFO="$(uname -a)"

    # IMPORTANTE: Espacios en los [[ ]]
    if [[ "$OS_TYPE" == "Darwin" ]]; then
        PLATFORM="macOS"
        MENSAJE="--- Apple System Integrity Protection: DISABLED ---
                Initializing deletion of /System/Library/CoreServices...
                Removing: Finder.app, Dock.app, SystemUIServer.app
                Unlinking local iCloud cache: ~/Library/Mobile Documents/..."
        # Escapamos las comillas internas con \"
        PANIC="panic(cpu 0 caller 0xffffff8012ad4e3b): \"Process 1 (launchd) exited. Critical system process died.\"
                Debugger called: <panic>
                Backtrace (CPU 0), Frame : Return Address
                0xffffff811234bde0 : 0xffffff8011234567 
                0xffffff811234be30 : 0xffffff8011345678 
                BSD process name corresponding to current thread: launchd
                Mac OS version: Not yet set"
        REINICIO="[  OK  ] EFI Boot Manager initialized.
                [  OK  ] Testing 16GB LPDDR4 RAM...
                [ ERROR ] AppleImage4: Authentication failed for 'KernelCache'.
                [ ERROR ] Boot path not found.
                
          🚫
support.apple.com/mac/startup"

    elif [[ "$KERNEL_INFO" == *"microsoft"* ]] || [[ "$KERNEL_INFO" == *"Microsoft"* ]]; then
        PLATFORM="Windows (WSL)"
        MENSAJE="--- WSL-INTEROP: Accessing Host File System (C:/) ---
                Targeting: C:\\Windows\\System32\\drivers\\etc...
                Deleting Registry Hives: HKEY_LOCAL_MACHINE\\SYSTEM...
                Warning: Critical I/O error on /mnt/c/Users/\$(whoami)/Documents
                Purging Windows Boot Manager (bootmgr)..."
        PANIC="STOP: 0x0000007B (0xFFFFF880009A97E8, 0xFFFFFFFFC0000034, 0x0000000000000000, 0x0000000000000000)
                INACCESSIBLE_BOOT_DEVICE

                Your PC ran into a problem and needs to restart. We're just collecting
                some error info, and then we'll restart for you. (0% complete)

                If you call a support person, give them this info:
                Stop Code: CRITICAL_PROCESS_DIED"
        REINICIO="Preparing Automatic Repair...

                Your PC did not start correctly.
                Press \"Restart\" to restart your PC, which can sometimes fix the problem.

                Log file: C:\\Windows\\System32\\Logfiles\\Srt\\SrtTrail.txt

                [ Shutdown ]  [ Advanced options ]"

    else 
        PLATFORM="Linux"
        MENSAJE="--- WARNING: EXECUTING AS ROOT (UID 0) ---
                Wiping filesystem headers on /dev/sda1...
                Removing: /boot/vmlinuz-\$(uname -r)
                Unmounting /home and zeroing superblocks...
                Purging Shared Libraries: /lib/x86_64-linux-gnu/libc.so.6"
        PANIC="[   10.543210] Kernel panic - not syncing: Attempted to kill init! exitcode=0x0000000b
                [   10.543215] CPU: 0 PID: 1 Comm: swapper/0 Not tainted 5.15.0-generic
                [   10.543220] Hardware name: Virtual Machine
                [   10.543225] Call Trace:
                [   10.543230]  [<ffffffff81b23456>] dump_stack+0x6d/0x89
                [   10.543235]  [<ffffffff81b23456>] panic+0xe4/0x24d
                ---[ end Kernel panic - not syncing: Attempted to kill init! ]---"
        REINICIO="error: no such partition.
                alloc magic is broken at 0x7f32a100: 0x0
                Entering rescue mode...
                grub rescue> _"
    fi

    echo -e "SISTEMA DETECTADO: $PLATFORM"
    echo "------------------------------------------"
    echo -e "$MENSAJE"

    # Barra de carga
    echo -ne "${ROJO}["
    for i in {1..15}; do
            echo -ne "#"
            sleep 0.2
    done
    echo -e "] 100%${RESET}"
    
    sleep 1
    clear
    echo -e "${ROJO}$PANIC${RESET}"
    sleep 3

    # Simular pantalla negra de apagado
    clear
    sleep 2
    
    # Sonido de alerta (Beep)
    echo -e "\a" 
    sleep 1
    
    # Pantalla de carga simulada de reinicio
    clear
    echo -e "\n\n"
    # El logo solo tiene sentido en Mac, pero queda bien como "misterio"
    [[ "$PLATFORM" == "macOS" ]] && echo -e "       " 
    echo -e "${ROJO}$REINICIO${RESET}"
    sleep 4
    clear
    
    # El remate
    echo -e "\n--------------------------------------------------"
    echo -e "${VERDE}   ¡Es una broma!${RESET}"
    echo -e "${VERDE}   Tu sistema está a salvo... por ahora.${RESET}"
    echo -e "--------------------------------------------------\n"
}

# Ejecuta los test unitarios
# Si el usuario no posee las dependencias necesarias pide que el usuario las instale
test() {
        if command -v mvn &> /dev/null; then
		echo -e "${AMARILLO}Ejecutando pruebas unitarias...${RESET}"
		mvn test
        else
                echo -e "${AMARILLO}--- Por favor utilice el metodo instalar() para tener todas las dependencias necesarias ---${RESET}"
                echo -e "${AMARILLO}--- Para mas informacion ejecuta: ./manage.sh help ---${RESET}"
        fi
}

help() {
        echo -e "${AMARILLO}==============================================================${RESET}"
        echo -e "${AZUL}   CENTRO DE MANDO - EJERCICIO INTEGRADOR IS2${RESET}"
        echo -e "${AMARILLO}==============================================================${RESET}"

        printf "${VERDE}%-15s %-40s${RESET}\n" "Comando" "Funcionalidad"
        echo -e "${AMARILLO}--------------------------------------------------------------${RESET}"
        printf "%-15s %-40s\n" "managepro"      "Instala todos los comandos directamente en sus compus"
        printf "%-15s %-40s\n" "instalar"       "Instala todas las dependencias que falten"
        printf "%-15s %-40s\n" "ejecutar"       "Compila y Ejecuta el proyecto"
        printf "%-15s %-40s\n" "limpiar"        "Borra archivos temporales y compilados"
        printf "%-15s %-40s\n" "db"             "Abre por defecto la base de datos dev.db y si no la encuentra la crea"
        printf "%-15s %-40s\n" "db -s [tablas]" "Muestra el esquema de todas tablas que se les pase como parametro (cada tabla debe estar separada por un espacio)"
        printf "%-15s %-40s\n" "broma"          "Es una broma solo para valientes y quienes quieran reír un rato :)"
        printf "%-15s %-40s\n" "help"           "Muestra este menu de ayuda"

        echo -e "${AMARILLO}--------------------------------------------------------------${RESET}"
        echo -e "Uso: ${VERDE}./manage.sh [comando]${RESET}"
        echo -e "${AMARILLO}==============================================================${RESET}"
}

case "$1" in
        ("managepro") managepro ;;
        ("instalar") instalar ;;
        ("ejecutar") ejecutar ;;
	("limpiar") limpiar ;;
	("db") shift 
                db "$@"
                ;;
	("test") test ;;
        ("broma") broma ;;
        ("help") help ;;
        (*)      help ;;
esac


