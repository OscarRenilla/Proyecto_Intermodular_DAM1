<?php

define('DB_HOST', 'localhost');
define('DB_NAME', 'nombre_bbdd');
define('DB_USER', 'usuario');
define('DB_PASS', 'contraseña');

function conectarBBDD(): PDO {

    // describe el tipo de base de datos que es y su host
    $dsn = "pgsql:host=" . DB_HOST . ";port=5432;dbname=" . DB_NAME . ";user=" . DB_USER . ";password=" . DB_PASS;

    // comportamiento de la conexión de la bbdd
    $opciones = [

        // Lanza excepciones en caso de error
        PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,

        // Resultados de las consultas 
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,

    ];

    try{    

        $conn = new PDO($dsn, null, null, $opciones);
        return $conn;

    }catch(PDOException $e){
        http_response_code(500);

        echo json_encode(['error' => 'Error con la conexión a la base de datos' . $e -> getMessage()]);

        exit;
    }
}