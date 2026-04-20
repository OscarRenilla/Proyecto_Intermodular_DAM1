CREATE TABLE usuarios(
	id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	dni VARCHAR(9) UNIQUE NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	email VARCHAR(150) UNIQUE NOT NULL,
	password VARCHAR(255) NOT NULL,
	rol VARCHAR(20) NOT NULL
);

CREATE TABLE relojs (
	id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	nombre VARCHAR(200) NOT NULL,
	modelo VARCHAR(150),
	descripcion VARCHAR(500),
	stock INT, 
	precio INT CHECK (precio < 0)
);

CREATE TABLE compras (
	id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	id_usuario INT NOT NULL,
	id_reloj INT NOT NULL,
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

-- USUARIOS
INSERT INTO usuarios (dni, nombre, email, password, rol) VALUES
('12345678A', 'Carlos Martínez', 'carlos.martinez@email.com', '$2b$12$hashedpassword1', 'admin'),
('23456789B', 'Laura Sánchez', 'laura.sanchez@email.com', '$2b$12$hashedpassword2', 'cliente'),
('34567890C', 'Miguel Torres', 'miguel.torres@email.com', '$2b$12$hashedpassword3', 'cliente'),
('45678901D', 'Ana Gómez', 'ana.gomez@email.com', '$2b$12$hashedpassword4', 'cliente'),
('56789012E', 'Roberto Fernández', 'roberto.fernandez@email.com', '$2b$12$hashedpassword5', 'cliente');

-- RELOJES
INSERT INTO relojs (nombre, modelo, descripcion, stock, precio) VALUES
('Rolex Day-Date 40 Oro Rosa Ombré Negro', '228235', 'Reloj de alta joyería en oro rosa 18 quilates con esfera degradada negra. Movimiento Calibre 3255, reserva de marcha 70 horas. Bisel y lugs engastados con diamantes. Edición 2025.', 3, 52000),
('Patek Philippe Nautilus', '5711/1A-010', 'Icónico reloj deportivo de lujo en acero inoxidable con esfera azul característica. Movimiento automático Calibre 26-330 S C. Resistente al agua 120m. El más codiciado del mercado secundario.', 1, 130000),
('Omega Speedmaster Moonwatch', '310.30.42.50.01.001', 'El reloj que fue a la Luna. Cronógrafo manual Calibre 3861 con certificación METAS. Caja de acero 42mm con cristal hesalita. Patrimonio histórico de la exploración espacial.', 8, 6300),
('Rolex GMT-Master II', '126710BLNR', 'Conocido como "Batman" por su bisel Cerachrom azul y negro. Movimiento Calibre 3285, indicador de segunda zona horaria. Bracelet Jubilee en Oystersteel. Ideal para viajeros.', 5, 15500),
('Hublot Big Bang Unico Dark Green Ceramic', '442.CI.1770.RX', 'Caja de 42mm en cerámica verde mate con movimiento manufacture UNICO HUB1280. Cronógrafo flyback con 72h de reserva de marcha. Correa de caucho intercambiable. Edición limitada.', 4, 22000),
('Jacob & Co. Astronomia Casino Roulette Tourbillon', 'AT110.40.AA.AA.A', 'Alta relojería con ruleta de casino en miniatura completamente funcional con bola giratoria. Tourbillon de tres ejes, esfera celestial giratoria. Movimiento JCAM37 totalmente visible. Arte mecánico único.', 1, 580000),
('Richard Mille RM 30-01', 'RM030-01', 'Caja ultraligera en titanio y carbono con sistema de amortiguación de golpes exclusivo. Movimiento automático con indicador de reserva de marcha y decoupling rotor. Peso inferior a 40g. Ingeniería extrema.', 2, 95000),
('Tissot PRX', 'T137.410.11.041.00', 'Reloj integrado de estilo setentero con brazalete y caja de acero en diseño continuo. Movimiento automático Powermatic 80 con 80h de reserva de marcha. La opción más accesible del lujo integrado.', 20, 650);

-- COMPRAS
INSERT INTO compras (id_usuario, id_reloj, fecha_compra) VALUES
(2, 3, '2025-01-15 10:23:00'),
(3, 8, '2025-02-03 16:45:00'),
(4, 4, '2025-02-20 09:10:00'),
(2, 8, '2025-03-01 14:30:00'),
(5, 1, '2025-03-18 11:55:00'),
(3, 4, '2025-04-02 17:20:00'),
(4, 6, '2025-04-10 13:00:00');
