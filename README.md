    En la carpeta principal se encuentran 2 archivos ejecutables: manage.bat y manage.sh, dichos archivos tienen como propósito proporcionar métodos que: se encarguen de la instalación de las dependencias necesarias, de la ejecución del proyecto, y de proporcionar algunos métodos útiles. 

    El archivo manage.bat es exclusivamente para Windows, principalmente para Windows 10-11; 
    mientras que el manage.sh se puede ejecutar desde un sistema unix, como ubuntu, wsl, etc. 
    Al usarlo desde un sistema linux, es necesario darle permiso de ejecución ejecutando el comando " chmod +x manage.sh ", o ejecutando todos los comandos de la manera " bash [comando] [comandoOpcional] ".

    Ambos archivos poseen los mismos métodos, la diferencia es que algunoas han sido adaptados para cada sistema operarivo, en específico:
        El método instalar() que se encuentra en manage.sh contiene los comandos necesarios para instalar maven y sql lite 3, desde la terminal. Dicho método pide la contraseña del usuario. Mientras que el instalar() de manage.bat solo proporciona el link de las páginas por las que se puede descargar las dependencias.

        El método ejecutar() que se encuentra en manage.sh y el de manage.bat, son prácticamente idénticos, lo que cambia es la sintaxis y una condición: en el archivo .sh se verifica si se trata de un sistema nativo o de una consola como lo sería wsl. Dicho método en ambos cumple la función de compilar el proyecto y redireccionar hacia el navegador, en específico hacia el localhost:8080.

    El resto de métodos son iguales para ambos archivos lo único en lo que difieren, como ya se ha mencionado anteriormente, es la sintaxis.

    Por lo que a continuación enumeraremos y daremos una breve descripción de los métodos contenidos en los archivos:
        instalar(), como ya dijimos se encarga de verificar si se cuenta con las dependencias necesarias para correr el archivo o si se debe instalar algo. En caso de que se deba instalar algo en Windows solo se proporciona el link de la página oficial; y en UNIX se usa el comando sudo para instalar las dependencias directamente.

        ejecutar(), como ya mencionamos, dicho método compila y ejecuta el proyecto, para ello se verifica en qué sistema lo estamos haciendo y abre el navegador predilecto en la siguiente dirección: http://localhost:8080; dicha página puede tener un pequeño diley que corresponde a la ejecución del proyecto. Por lo que hasta que el mismo no se termine de cargar no se tendrá acceso a la página, apareciendo un error 404.

        limpiar(), borra los archivos temporales y las compilaciones anteriores mediante el comando mvn; si el usuario no posee dicha dependencia muestra un aviso

        test(), realiza los test unitarios que contenga el proyecto mediante el comando mvn; si no se posee dicha dependencia muestra un aviso.

        db(), este método abre por defecto el archivo db/dev.db del proyecto; si se pasa la palabra prod abre el archivo db/prod.db del proyecto. Para ello se utiliza el comando sqlite3, si no se posee la dependencia muestra un aviso.

        help(), este comando muestra a los métodos y su descripción, además de mostrar cómo se deben de llamar.
