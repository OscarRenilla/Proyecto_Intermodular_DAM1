-- ============================================================
--  SCRIPT DE INICIALIZACIÓN COMPLETA - CRONOSS
--  Ejecutar en PostgreSQL con: psql -U postgres -f init.sql
--  O pegar en pgAdmin -> Query Tool
-- ============================================================

-- Eliminar tablas si ya existen (para poder re-ejecutar limpio)
DROP TABLE IF EXISTS compras CASCADE;
DROP TABLE IF EXISTS relojs CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;

-- Crear tablas
CREATE TABLE usuarios (
    id       INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    dni      VARCHAR(9)   UNIQUE NOT NULL,
    nombre   VARCHAR(100) NOT NULL,
    email    VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol      VARCHAR(20)  NOT NULL
);

CREATE TABLE relojs (
    id          INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre      VARCHAR(200) NOT NULL,
    modelo      VARCHAR(150),
    descripcion VARCHAR(500),
    stock       INT,
    precio      INT
);

CREATE TABLE compras (
    id           INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario   INT NOT NULL,
    id_reloj     INT NOT NULL,
    fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_compras_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_compras_reloj
        FOREIGN KEY (id_reloj)
        REFERENCES relojs(id)
        ON DELETE CASCADE
);

-- ============================================================
--  DATOS DE EJEMPLO
--  Contrasena de todos los usuarios: Abc@123!
--  (hash BCrypt generado con factor 10)
-- ============================================================

INSERT INTO usuarios (dni, nombre, email, password, rol) VALUES
('12345678O', 'OscarUser',   'oscar.user@email.com',   '$2a$10$QGAbOPfan3v1H6a2I0mjQeR/sVBbltyZRFyzQ.kLVdavFHuOunZju', 'USER'),
('87654321O', 'OscarAdmin',  'oscar.admin@email.com',  '$2a$10$QGAbOPfan3v1H6a2I0mjQeR/sVBbltyZRFyzQ.kLVdavFHuOunZju', 'ADMIN'),
('12345678S', 'SebasUser',   'sebas.user@email.com',   '$2a$10$QGAbOPfan3v1H6a2I0mjQeR/sVBbltyZRFyzQ.kLVdavFHuOunZju', 'USER'),
('87654321S', 'SebasAdmin',  'sebas.admin@email.com',  '$2a$10$QGAbOPfan3v1H6a2I0mjQeR/sVBbltyZRFyzQ.kLVdavFHuOunZju', 'ADMIN'),
('12345678M', 'MarcelUser',  'marcel.user@email.com',  '$2a$10$QGAbOPfan3v1H6a2I0mjQeR/sVBbltyZRFyzQ.kLVdavFHuOunZju', 'USER'),
('87654321M', 'MarcelAdmin', 'marcel.admin@email.com', '$2a$10$QGAbOPfan3v1H6a2I0mjQeR/sVBbltyZRFyzQ.kLVdavFHuOunZju', 'ADMIN'),
('12345678A', 'AlvaroUser',  'alvaro.user@email.com',  '$2a$10$QGAbOPfan3v1H6a2I0mjQeR/sVBbltyZRFyzQ.kLVdavFHuOunZju', 'USER'),
('87654321A', 'AlvaroAdmin', 'alvaro.admin@email.com', '$2a$10$QGAbOPfan3v1H6a2I0mjQeR/sVBbltyZRFyzQ.kLVdavFHuOunZju', 'ADMIN');

INSERT INTO relojs (nombre, modelo, descripcion, stock, precio) VALUES
('Rolex Day-Date Rose Gold 40mm Ombre Black', '228235',
 'En oro rosa con esfera ombre negra, combinacion perfecta de lujo y elegancia. Oro Everose de 18k.',
 3, 45000),
('Patek Philippe Nautilus', '5711',
 'Uno de los relojes mas iconicos del mundo. Diseno elegante con caracter deportivo.',
 2, 120000),
('Omega Speedmaster Moonwatch Moonphase', 'Co-Axial Master Chronometer 44.25mm Platinum',
 'Obra maestra de la relojeria. Caja de platino, esfera de oro y complicacion de fase lunar.',
 5, 35000),
('Rolex GMT-Master II', 'GMT-Master II',
 'Diseno para viajeros y profesionales. Iconica funcion GMT y construccion robusta.',
 8, 18000),
('Hublot Big Bang Unico Dark Green Ceramic', 'Big Bang Unico',
 'Diseno audaz con caja de ceramica y movimiento Unico de manufactura propia.',
 4, 25000),
('Jacob and Co. Astronomia Casino Roulette Tourbillon', 'Astronomia Casino',
 'Pura extravagancia y alta relojeria. Diseno tridimensional con ruleta funcional y tourbillon.',
 1, 580000),
('Richard Mille RM 30-01', 'RM 30-01',
 'Resistente a condiciones extremas del tenis. Caja de titanio y carbono con movimiento tourbillon.',
 2, 150000),
('Tissot PRX', 'PRX Powermatic 80',
 'Combina estilo y funcionalidad. Acero inoxidable y movimiento automatico atemporal.',
 20, 750);

INSERT INTO compras (id_usuario, id_reloj) VALUES
(1, 4),
(1, 8),
(3, 1),
(3, 3),
(5, 5),
(5, 8),
(7, 8);

