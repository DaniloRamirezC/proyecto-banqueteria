# Proyecto Banqueteria Paolicias.Sweet
Proyecto desarrollado con lenguaje de programación Kotlin para la gestión integral de un servicio de banquetería, mediante un patrón de arquitectura MVVM (Model-View-ViewModel).

### Integrantes del proyecto
- Benjamín González
- Danilo Ramírez
- Nicolás Suazo

### 1. Pantallas de la aplicación
| Pantalla | Responsabilidad | 
| :---          | :---        |
| `AdminScreen` | Pantalla de administración y gestión de las reservas|
| `BanqueteriaScreen`| Pantalla de ingreso a la aplicación | 
| `CatalogoScreen`| Gestión del servicio de banquetería y tipos de eventos | 
| `LoginScreen`| Gestión de usuario y contraseña | 
| `RegisterScreen`| Gestión del registro de usuario con nombre, correo y contraseña |
| `RegistroEventoScreen`| Gestión del registro de evento | 
| `ResumenEventoScreen`| Encargado del comprobante de evento registrado y de la transacción financiera |

### 2. Microservicios y Puertos
| Microservicio | Responsabilidad | Puerto | Base de datos | Tablas | Endpoints |
| :---          | :---        | :---   | :---    | :---    | :--- | :--- |
| `service-catalogo` | Gestión del servicio de banquetería y pastelería | 8081 | `bd_catalogo` | `categoria`, `menu` | GET, POST, PUT, DELETE |
| `service-cliente`| Gestión de los datos del cliente | 8082 | `bd_cliente` | `cliente` | GET, PUT, POST, DELETE | 
| `servicio-pedido`| Gestión de registro de evento y detalles | 8083 | `bd_pedido` | `evento`, `detalle_evento` | GET, POST | 
| `service-administrador`| Gestión de los comprobantes de las reservas | 8084 | `bd_admin` | `administrador`, `validacion_pago` | GET, PUT, POST, DELETE |
| `service-autenticacion`| Gestión de usuarios, contraseñas y roles | 8085 | `bd_autenticacion` | `roles`, `usuarios` | GET |
| `service-resumen`| Gestión del resumen del evento | 8086 | `bd_resumen` | `comprobantes`, `resumenes` | GET, POST, PATCH |

### 3. Herramientas y aplicaciones requeridas
- Lenguaje: Kotlin
- IDE: Android Studio Quail 3
- Base de Datos: SQLite (local), PostgreSQL
- Jetpack Compose: Para la creación de interfaces de usuario nativas en Android.
- Docker Compose
- Postman: Para documentar y probar APIs.
- Visual Studio Code: Para desarrollar el backend.
- Amazon Web Services: Para crear instancias en la nube.

### 4. Pasos para ejecutar 
Para que los recursos nativos funcionen en tiempo real, se debe duplicar la pantalla, para ello se hace lo siguiente:
1. En la parte superior derecha, se muestra un engranaje, presiona y te diriges a Settings.
2. En Tools te diriges a Device Mirroring, y selecciona las dos primeras opciones: "Activate mirroring when a new pshycal device is connected" y "Activate mirroring when launching an app on a physical device".
3. Para conectarlo a tu celular Android, tienes que dirigirte a Ajustes>Acerca del teléfono>Información de software. En Información de software presiona varias veces "Número de compilación" para que se active "Opciones de desarrollador"
4.  Dirígete a Seguridad y privacidad y desactiva "Bloqueador automático"
5.  Posteriormente dirígete a "Opciones de desarrollador" y activa Depuración por USB.
6.  Conecta el celular al computador mediante cable USB, te aparecerá un mensaje que dice "¿Permitir depuración por USB?" y presionas Permitir.

Para usar la aplicación móvil: 
- Debe conectarse en la instancia AWS con IP elástica, si no está conectado, no le responderá la aplicación.
- Encender el Docker con cada uno de los microservicios y la base de datos pgAdmin (de PostgreSQL)
- Si no tiene creado un usuario, debe crear uno con sus datos personales y luego iniciar sesión con el correo y la contraseña.
- En la aplicación debe crear el catálogo de banquetería y pastelería, para ello, en Postman y mediante un POST cree un menú de banquetería.

