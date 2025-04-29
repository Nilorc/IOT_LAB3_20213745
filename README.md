# TeleTrivia - Proyecto Android

Este repositorio contiene el desarrollo de la aplicación **TeleTrivia**, realizada como parte del **Tercer Laboratorio** del curso **Servicios y Aplicaciones para IoT [1TEL05]** de la **Pontificia Universidad Católica del Perú**, durante el semestre **2025-1**.

## Descripción
**TeleTrivia** es una aplicación de trivia en la cual los usuarios seleccionan una categoría, cantidad de preguntas y nivel de dificultad para comenzar una serie de preguntas de tipo verdadero/falso.

La aplicación fue desarrollada con el apoyo de herramientas de inteligencia artificial para estructurar su arquitectura, planificar funcionalidades y acelerar tareas de codificación.  
Se realizaron **correcciones y adaptaciones propias** para mejorar su funcionalidad y adecuarla a los requisitos del laboratorio.

Además, la aplicación está optimizada para funcionar mejor en **modo claro** por temas de compatibilidad visual con el sistema Android.

## Funcionalidades principales
- **Menú principal** con selección de:
  - Categoría (Cultura General, Libros, Películas, Música, Computación, Matemática, Deportes, Historia)
  - Cantidad de preguntas
  - Nivel de dificultad (fácil, medio, difícil)
- **Validación de conexión a Internet** y activación de opciones solo si se detecta conexión.
- **Obtención de preguntas** mediante llamadas **GET** a la API de OpenTrivia (`https://opentdb.com/api.php`).
- **Contador descendente** basado en cantidad y dificultad de preguntas, que continúa funcionando aunque se rote o cierre la pantalla.
- **Interfaz de trivia** con navegación entre preguntas y validación de selección de respuesta.
- **Vista de estadísticas** al finalizar:
  - Número de respuestas correctas e incorrectas
  - Preguntas no respondidas (si el tiempo se terminó)
  - Opción para volver a jugar.

## Consideraciones
- Se realizaron pruebas principalmente en **modo claro** para garantizar la mejor experiencia de usuario.

## Créditos
Desarrollado de manera individual por Nilo Cori - 20213745
