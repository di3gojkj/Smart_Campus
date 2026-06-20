-- Crear la BD para ms-clientes
CREATE DATABASE IF NOT EXISTS db_asignatura
 CHARACTER SET utf8mb4
 COLLATE utf8mb4_unicode_ci;

 -- Crear la BD para ms-productos
CREATE DATABASE IF NOT EXISTS db_carrera
 CHARACTER SET utf8mb4
 COLLATE utf8mb4_unicode_ci;

-- Crear la BD para ms-clientes
CREATE DATABASE IF NOT EXISTS db_curso_evaluacion
 CHARACTER SET utf8mb4
 COLLATE utf8mb4_unicode_ci;

 -- Crear la BD para ms-productos
CREATE DATABASE IF NOT EXISTS db_curso_seccion
 CHARACTER SET utf8mb4
 COLLATE utf8mb4_unicode_ci;

-- Crear la BD para ms-clientes
CREATE DATABASE IF NOT EXISTS db_evaluacion
 CHARACTER SET utf8mb4
 COLLATE utf8mb4_unicode_ci;

 -- Crear la BD para ms-productos
CREATE DATABASE IF NOT EXISTS db_estado
 CHARACTER SET utf8mb4
 COLLATE utf8mb4_unicode_ci;

-- Crear la BD para ms-clientes
CREATE DATABASE IF NOT EXISTS db_academico
 CHARACTER SET utf8mb4
 COLLATE utf8mb4_unicode_ci;

 -- Crear la BD para ms-productos
CREATE DATABASE IF NOT EXISTS db_usuario
 CHARACTER SET utf8mb4
 COLLATE utf8mb4_unicode_ci;

-- Crear la BD para ms-clientes
CREATE DATABASE IF NOT EXISTS db_tipo-asistencia
 CHARACTER SET utf8mb4
 COLLATE utf8mb4_unicode_ci;


-- Otorgar todos los permisos al usuario root
GRANT ALL PRIVILEGES ON db_asignatura.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON db_carrera.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON db_curso_evaluacion.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON db_curso_seccion.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON db_evaluacion.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON db_estado.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON db_academico.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON db_usuario.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON db_tipo-asistencia.* TO 'root'@'%';
-- Aplique los permisos inmediatamente
FLUSH PRIVILEGES;