-- USUARIOS (datos de ejemplo)
INSERT INTO usuarios (dni, nombre, email, password, rol) VALUES
('12345678O', 'ÓscarUser', 'oscar.user@email.com', 'Abc@123', 'USER'),
('87654321O', 'ÓscarAdmin', 'oscar.admin@email.com', 'Abc@123', 'ADMIN'),
('12345678S', 'SebasUser', 'sebas.user@email.com', 'Abc@123', 'USER'),
('87654321S', 'SebasAdmin', 'sebas.admin@email.com','Abc@123', 'ADMIN'),
('12345678M', 'MarcelUser', 'marcel.user@email.com', 'Abc@123', 'USER'),
('87654321M', 'MarcelAdmin', 'marcel.admin@email.com', 'Abc@123', 'ADMIN'),
('12345678A', 'AlvaroUser', 'alvaro.user@email.com', 'Abc@123', 'USER'),
('87654321A', 'AlvaroAdmi', 'alvaro.admin@email.com','Abc@123', 'ADMIN');

-- RELOJES
INSERT INTO relojs (nombre, modelo, descripcion, stock, precio) VALUES
(
  'Rolex Day-Date Rose Gold 40mm Ombré Black',
  '228235',
  'En oro rosa con esfera ombré negra es la combinación perfecta de lujo y elegancia. Su exclusivo acabado en oro Everose de 18k y su distintiva esfera degradada lo convierten en una pieza sofisticada y muy deseada.',
  3,
  45000
),
(
  'Patek Philippe Nautilus',
  '5711',
  'Es uno de los relojes más icónicos y codiciados del mundo. Su diseño elegante con carácter deportivo, junto a su inconfundible esfera y acabados de alta relojería, lo convierten en una pieza única.',
  2,
  120000
),
(
  'Omega Speedmaster Moonwatch Moonphase',
  'Co-Axial Master Chronometer 44.25mm Platinum',
  'Es una obra maestra de la relojería, que combina innovación y tradición en un diseño imponente. Su caja de platino, esfera de oro y complicación de fase lunar elevan este modelo a otro nivel de exclusividad. Un reloj sofisticado, preciso y con una presencia inigualable.',
  5,
  35000
),
(
  'Rolex GMT-Master II',
  'GMT-Master II',
  'Lujo diseñado para viajeros y profesionales que necesitan seguir múltiples zonas horarias. Con su icónica función GMT, su robusta construcción y su diseño atemporal, este reloj se ha convertido en un símbolo de prestigio y funcionalidad en el mundo de la relojería.',
  8,
  18000
),
(
  'Hublot Big Bang Unico Dark Green Ceramic',
  'Big Bang Unico',
  'Destaca por su diseño audaz y su innovación técnica. Con su caja de cerámica, movimiento Unico de manufactura propia y su estética vanguardista, este reloj se ha convertido en un símbolo de exclusividad y estilo en el mundo de la alta relojería.',
  4,
  25000
),
(
  'Jacob and Co. Astronomia Casino Roulette Tourbillon',
  'Astronomia Casino',
  'Pura extravagancia y alta relojería llevadas al extremo. Su diseño tridimensional con ruleta funcional y tourbillon lo convierte en una pieza única que no pasa desapercibida. Un reloj exclusivo para quienes buscan lujo, innovación y espectáculo en la muñeca.',
  1,
  580000
),
(
  'Richard Mille RM 30-01',
  'RM 30-01',
  'Diseñado para resistir las condiciones extremas del tenis profesional. Con su caja de titanio y carbono, su movimiento tourbillon y su diseño vanguardista, este reloj es una obra maestra de la ingeniería y la innovación, que combina resistencia, precisión y estilo en una pieza única.',
  2,
  150000
),
(
  'Tissot PRX',
  'PRX Powermatic 80',
  'Combina estilo y funcionalidad en un diseño atemporal. Con su caja de acero inoxidable, esfera con indicadores de hora y minutero, y su movimiento automático, este reloj es una elección perfecta para quienes buscan calidad y durabilidad en su accesorio.',
  20,
  750
);


-- COMPRAS (relaciones de ejemplo entre usuarios y relojes)
INSERT INTO compras (id_usuario, id_reloj) VALUES
(2, 4), 
(2, 8), 
(3, 1), 
(4, 3), 
(5, 5), 
(3, 8), 
(4, 8); 
