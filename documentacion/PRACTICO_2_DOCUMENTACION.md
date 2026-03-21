# PRÁCTICO 2B – DOCUMENTACIÓN
## grupo 2 - comision 2

##INTEGRANTES:


## Luchini, Lucia Antonella.
## Gillo Mayer, Celina.
## Sangroniz, Maria Candela.
## Echenique, Mateo.

  
## Análisis del código fuente y patrón de diseño preexistente

En el código base del proyecto se identifica la presencia del **Patrón de Diseño Singleton**, aplicado en la clase `DBConfigSingleton`. Este patrón permite garantizar que exista **una única instancia** de la configuración de la base de datos durante toda la ejecución del sistema.

---

## ¿Qué patrón identificamos?

El patrón identificado es **Singleton**, como vimos en el teorico es uno de los patrones creacionales del libro *Design Patterns*.

Este patrón se utiliza cuando se necesita que una clase tenga **solo una instancia global**, accesible en cualquier parte del programa.

---

## ¿Dónde aparece implementado?

La implementación del patrón ocurre en la clase: com.is1.proyecto.config.DBConfigSingleton
DBConfigSingleton encapsula toda la configuración necesaria para conectarse a la base de datos:

**Driver**

**URL**

**Usuario**

**Contraseña**

El patrón se usa durante el ciclo de vida de la aplicación mediante filtros before y after, donde se abre y cierra la conexión.
