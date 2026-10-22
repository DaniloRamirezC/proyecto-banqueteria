# Proyecto Banqueteria Paolicias.Sweet
Proyecto desarrollado con lenguaje de programación Kotlin para la gestión integral de un servicio de banquetería, mediante un patrón de arquitectura MVVM (Model-View-ViewModel).

### Integrantes del proyecto
- Benjamín González
- Danilo Ramírez
- Nicolás Suazo

### 1. Pantallas de la aplicación
| Pantalla | Responsabilidad | 
| :---          | :---        |
| `BanqueteriaScreen`| Pantalla de ingreso a la aplicación | 
| `CatalogoScreen`| Gestión del servicio de banquetería y tipos de eventos | 
| `LoginScreen`| Gestión de usuario y contraseña | 
| `RegisterScreen`| Gestión del registro de usuario con nombre, correo y contraseña |
| `RegistroEventoScreen`| Gestión del registro de evento | 
| `ResumenEventoScreen`| Encargado del comprobante de evento registrado y de la transacción financiera |

### 2. Herramientas y aplicaciones requeridas
- Lenguaje: Kotlin
- Sistema de compilación: Gradle, para la gestión de las dependencias y construcción del proyecto
- IDE: Android Studio Quail 3
- Base de Datos: SQLite
- Jetpack Compose: Para la creación de interfaces de usuario nativas en Android.

### 3. Pasos para ejecutar
Para que los recursos nativos funcionen en tiempo real, se debe duplicar la pantalla, para ello se hace lo siguiente:
1. En la parte superior derecha, se muestra un engranaje, presiona y te diriges a Settings.
2. En Tools te diriges a Device Mirroring, y selecciona las dos primeras opciones: "Activate mirroring when a new pshycal device is connected" y "Activate mirroring when launching an app on a physical device".
3. Para conectarlo a tu celular Android, tienes que dirigirte a Ajustes>Acerca del teléfono>Información de software. En Información de software presiona varias veces "Número de compilación" para que se active "Opciones de desarrollador"
4.  Dirígete a Seguridad y privacidad y desactiva "Bloqueador automático"
5.  Posteriormente dirígete a "Opciones de desarrollador" y activa Depuración por USB.
6.  Conecta el celular al computador mediante cable USB, te aparecerá un mensaje que dice "¿Permitir depuración por USB?" y presionas Permitir.
