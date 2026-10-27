-- ============================================================
--  SCRIPT DE INSERCIÓN DE DATOS DE EJEMPLO - CRONOSS
--  IMPORTANTE: Las contraseñas están hasheadas con BCrypt.
--  Contraseña real de todos los usuarios: Abc@123!
-- ============================================================

-- USUARIOS
-- Hash BCrypt de "Abc@123!" generado con factor 10
INSERT INTO usuarios (dni, nombre, email, password, rol) VALUES
('12345678O', 'OscarUser',   'oscar.user@email.com',   '$2a$10$QGAbOPfan3v1H6a2I0mjQeR/sVBbltyZRFyzQ.kLVdavFHuOunZju', 'USER'),
('87654321O', 'OscarAdmin',  'oscar.admin@email.com',  '$2a$10$QGAbOPfan3v1H6a2I0mjQeR/sVBbltyZRFyzQ.kLVdavFHuOunZju', 'ADMIN'),
('12345678S', 'SebasUser',   'sebas.user@email.com',   '$2a$10$QGAbOPfan3v1H6a2I0mjQeR/sVBbltyZRFyzQ.kLVdavFHuOunZju', 'USER'),
('87654321S', 'SebasAdmin',  'sebas.admin@email.com',  '$2a$10$QGAbOPfan3v1H6a2I0mjQeR/sVBbltyZRFyzQ.kLVdavFHuOunZju', 'ADMIN'),
('12345678M', 'MarcelUser',  'marcel.user@email.com',  '$2a$10$QGAbOPfan3v1H6a2I0mjQeR/sVBbltyZRFyzQ.kLVdavFHuOunZju', 'USER'),
('87654321M', 'MarcelAdmin', 'marcel.admin@email.com', '$2a$10$QGAbOPfan3v1H6a2I0mjQeR/sVBbltyZRFyzQ.kLVdavFHuOunZju', 'ADMIN'),
('12345678A', 'AlvaroUser',  'alvaro.user@email.com',  '$2a$10$QGAbOPfan3v1H6a2I0mjQeR/sVBbltyZRFyzQ.kLVdavFHuOunZju', 'USER'),
('87654321A', 'AlvaroAdmin', 'alvaro.admin@email.com', '$2a$10$QGAbOPfan3v1H6a2I0mjQeR/sVBbltyZRFyzQ.kLVdavFHuOunZju', 'ADMIN');

-- RELOJES
INSERT INTO relojs (nombre, modelo, descripcion, stock, precio) VALUES
(
  'Rolex Day-Date Rose Gold 40mm Ombre Black',
  '228235',
  'En oro rosa con esfera ombre negra es la combinacion perfecta de lujo y elegancia. Su exclusivo acabado en oro Everose de 18k y su distintiva esfera degradada lo convierten en una pieza sofisticada y muy deseada.',
  3,
  45000
),
(
  'Patek Philippe Nautilus',
  '5711',
  'Es uno de los relojes mas iconicos y codiciados del mundo. Su diseno elegante con caracter deportivo, junto a su inconfundible esfera y acabados de alta relojeria, lo convierten en una pieza unica.',
  2,
  120000
),
(
  'Omega Speedmaster Moonwatch Moonphase',
  'Co-Axial Master Chronometer 44.25mm Platinum',
  'Es una obra maestra de la relojeria, que combina innovacion y tradicion en un diseno imponente. Su caja de platino, esfera de oro y complicacion de fase lunar elevan este modelo a otro nivel de exclusividad.',
  5,
  35000
),
(
  'Rolex GMT-Master II',
  'GMT-Master II',
  'Lujo disenado para viajeros y profesionales que necesitan seguir multiples zonas horarias. Con su iconica funcion GMT, su robusta construccion y su diseno atemporal es un simbolo de prestigio.',
  8,
  18000
),
(
  'Hublot Big Bang Unico Dark Green Ceramic',
  'Big Bang Unico',
  'Destaca por su diseno audaz y su innovacion tecnica. Con su caja de ceramica, movimiento Unico de manufactura propia y su estetica vanguardista, es un simbolo de exclusividad y estilo en la alta relojeria.',
  4,
  25000
),
(
  'Jacob and Co. Astronomia Casino Roulette Tourbillon',
  'Astronomia Casino',
  'Pura extravagancia y alta relojeria llevadas al extremo. Su diseno tridimensional con ruleta funcional y tourbillon lo convierte en una pieza unica que no pasa desapercibida.',
  1,
  580000
),
(
  'Richard Mille RM 30-01',
  'RM 30-01',
  'Disenado para resistir las condiciones extremas del tenis profesional. Con su caja de titanio y carbono, su movimiento tourbillon y su diseno vanguardista, es una obra maestra de la ingenieria.',
  2,
  150000
),
(
  'Tissot PRX',
  'PRX Powermatic 80',
  'Combina estilo y funcionalidad en un diseno atemporal. Con su caja de acero inoxidable y su movimiento automatico, es la eleccion perfecta para quienes buscan calidad y durabilidad.',
  20,
  750
);

-- COMPRAS (relaciones de ejemplo)
INSERT INTO compras (id_usuario, id_reloj) VALUES
(1, 4),
(1, 8),
(3, 1),
(3, 3),
(5, 5),
(5, 8),
(7, 8);
