<?php

require_once 'db_config.php';

header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

// Responder preflight CORS
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

$pdo = conectarBBDD();

$metodo = $_SERVER['REQUEST_METHOD'];

// Leer el recurso de la URL: /api.php?recurso=relojes  o  /api.php?recurso=compras
$recurso = $_GET['recurso'] ?? '';

// ──────────────────────────────────────────────
//  /api.php?recurso=relojes
// ──────────────────────────────────────────────
if ($recurso === 'relojes') {

    if ($metodo === 'GET') {
        // Listar todos los relojes
        $stmt = $pdo->query("SELECT * FROM relojs ORDER BY id");
        $relojes = $stmt->fetchAll();
        echo json_encode($relojes);

    } elseif ($metodo === 'POST') {
        // Añadir un nuevo reloj
        $datos = json_decode(file_get_contents('php://input'), true);

        if (empty($datos['nombre']) || empty($datos['modelo']) || empty($datos['descripcion'])
            || !isset($datos['stock']) || !isset($datos['precio'])) {
            http_response_code(400);
            echo json_encode(['error' => 'Faltan campos obligatorios: nombre, modelo, descripcion, stock, precio']);
            exit;
        }

        $stmt = $pdo->prepare(
            "INSERT INTO relojs (nombre, modelo, descripcion, stock, precio)
             VALUES (:nombre, :modelo, :descripcion, :stock, :precio)"
        );
        $stmt->execute([
            ':nombre'      => $datos['nombre'],
            ':modelo'      => $datos['modelo'],
            ':descripcion' => $datos['descripcion'],
            ':stock'       => (int) $datos['stock'],
            ':precio'      => (int) $datos['precio'],
        ]);

        http_response_code(201);
        echo json_encode(['ok' => true, 'message' => 'Reloj añadido correctamente']);

    } elseif ($metodo === 'PUT') {
        // Editar un reloj existente: /api.php?recurso=relojes&id=X
        $id = isset($_GET['id']) ? (int) $_GET['id'] : 0;
        if ($id <= 0) {
            http_response_code(400);
            echo json_encode(['error' => 'Se requiere un id válido en la URL (?id=X)']);
            exit;
        }

        $datos = json_decode(file_get_contents('php://input'), true);

        $stmt = $pdo->prepare(
            "UPDATE relojs SET nombre=:nombre, modelo=:modelo, descripcion=:descripcion,
             stock=:stock, precio=:precio WHERE id=:id"
        );
        $stmt->execute([
            ':nombre'      => $datos['nombre'],
            ':modelo'      => $datos['modelo'],
            ':descripcion' => $datos['descripcion'],
            ':stock'       => (int) $datos['stock'],
            ':precio'      => (int) $datos['precio'],
            ':id'          => $id,
        ]);

        echo json_encode(['ok' => true, 'message' => 'Reloj actualizado correctamente']);

    } elseif ($metodo === 'DELETE') {
        // Eliminar un reloj: /api.php?recurso=relojes&id=X
        $id = isset($_GET['id']) ? (int) $_GET['id'] : 0;
        if ($id <= 0) {
            http_response_code(400);
            echo json_encode(['error' => 'Se requiere un id válido en la URL (?id=X)']);
            exit;
        }

        $stmt = $pdo->prepare("DELETE FROM relojs WHERE id = :id");
        $stmt->execute([':id' => $id]);

        echo json_encode(['ok' => true, 'message' => 'Reloj eliminado correctamente']);

    } else {
        http_response_code(405);
        echo json_encode(['error' => 'Método no permitido']);
    }

// ──────────────────────────────────────────────
//  /api.php?recurso=compras
// ──────────────────────────────────────────────
} elseif ($recurso === 'compras') {

    if ($metodo === 'GET') {
        // Obtener compras de un usuario: /api.php?recurso=compras&id_usuario=X
        $idUsuario = isset($_GET['id_usuario']) ? (int) $_GET['id_usuario'] : 0;
        if ($idUsuario <= 0) {
            http_response_code(400);
            echo json_encode(['error' => 'Se requiere id_usuario en la URL']);
            exit;
        }

        $stmt = $pdo->prepare(
            "SELECT r.id, r.nombre, r.modelo, r.descripcion, r.stock, r.precio,
                    COUNT(c.id_reloj) AS compras
             FROM relojs r
             INNER JOIN compras c ON r.id = c.id_reloj
             WHERE c.id_usuario = :id_usuario
             GROUP BY r.id, r.nombre, r.modelo, r.descripcion, r.stock, r.precio
             ORDER BY r.nombre"
        );
        $stmt->execute([':id_usuario' => $idUsuario]);
        $compras = $stmt->fetchAll();
        echo json_encode($compras);

    } elseif ($metodo === 'POST') {
        // Registrar una compra y decrementar stock
        $datos = json_decode(file_get_contents('php://input'), true);

        if (empty($datos['id_usuario']) || empty($datos['id_reloj'])) {
            http_response_code(400);
            echo json_encode(['error' => 'Se requieren id_usuario e id_reloj']);
            exit;
        }

        $idUsuario = (int) $datos['id_usuario'];
        $idReloj   = (int) $datos['id_reloj'];

        try {
            $pdo->beginTransaction();

            // Comprobar stock
            $stmtStock = $pdo->prepare("SELECT stock FROM relojs WHERE id = :id FOR UPDATE");
            $stmtStock->execute([':id' => $idReloj]);
            $reloj = $stmtStock->fetch();

            if (!$reloj) {
                $pdo->rollBack();
                http_response_code(404);
                echo json_encode(['error' => 'El reloj no existe']);
                exit;
            }
            if ($reloj['stock'] <= 0) {
                $pdo->rollBack();
                http_response_code(409);
                echo json_encode(['error' => 'No hay stock disponible para este reloj']);
                exit;
            }

            // Insertar compra
            $stmtCompra = $pdo->prepare(
                "INSERT INTO compras (id_usuario, id_reloj) VALUES (:id_usuario, :id_reloj)"
            );
            $stmtCompra->execute([':id_usuario' => $idUsuario, ':id_reloj' => $idReloj]);

            // Decrementar stock
            $stmtUpdate = $pdo->prepare("UPDATE relojs SET stock = stock - 1 WHERE id = :id");
            $stmtUpdate->execute([':id' => $idReloj]);

            $pdo->commit();
            echo json_encode(['ok' => true, 'message' => 'Compra realizada correctamente']);

        } catch (PDOException $e) {
            $pdo->rollBack();
            http_response_code(500);
            echo json_encode(['error' => 'Error al procesar la compra: ' . $e->getMessage()]);
        }

    } else {
        http_response_code(405);
        echo json_encode(['error' => 'Método no permitido']);
    }

// ──────────────────────────────────────────────
//  /api.php?recurso=usuarios
// ──────────────────────────────────────────────
} elseif ($recurso === 'usuarios') {

    if ($metodo === 'GET') {
        // Listar todos los usuarios (sin password)
        $stmt = $pdo->query("SELECT id, dni, nombre, email, rol FROM usuarios ORDER BY id");
        $usuarios = $stmt->fetchAll();
        echo json_encode($usuarios);

    } else {
        http_response_code(405);
        echo json_encode(['error' => 'Método no permitido']);
    }

// ──────────────────────────────────────────────
//  Recurso no reconocido
// ──────────────────────────────────────────────
} else {
    http_response_code(404);
    echo json_encode([
        'error'    => 'Recurso no encontrado',
        'recursos' => [
            'GET    /api.php?recurso=relojes'                       => 'Lista todos los relojes',
            'POST   /api.php?recurso=relojes'                       => 'Añade un reloj',
            'PUT    /api.php?recurso=relojes&id=X'                  => 'Edita un reloj',
            'DELETE /api.php?recurso=relojes&id=X'                  => 'Elimina un reloj',
            'GET    /api.php?recurso=compras&id_usuario=X'          => 'Compras de un usuario',
            'POST   /api.php?recurso=compras'                       => 'Registra una compra',
            'GET    /api.php?recurso=usuarios'                      => 'Lista usuarios',
        ]
    ]);
}
