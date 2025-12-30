-- Tabla autores
INSERT INTO autores (id, nombre, version) VALUES
    (1, 'Gabriel García Márquez', 1),
    (2, 'Isabel Allende', 1),
    (3, 'J.K. Rowling', 1);

-- Tabla libros
INSERT INTO libros (isbn, titulo, precio, version) VALUES
    ('100', 'Cien años de soledad', 199.99, 1),
    ('101', 'La casa de los espíritus', 149.50, 1),
    ('102', 'Harry Potter y la piedra filosofal', 120.00, 1);

-- Tabla libros_autores (el libro 100 tiene dos autores)
INSERT INTO libros_autores (libro_isbn, autor_id) VALUES
    ('100', 1),
    ('100', 2),
    ('101', 2),
    ('102', 3);

-- Tabla inventario
INSERT INTO inventario (isbn, vendidos, suministrados) VALUES
    ('100', 10, 50),
    ('101', 5, 30),
    ('102', 20, 100);

-- Tabla clientes
INSERT INTO clientes (id, nombre, correo, version) VALUES
    (1, 'Juan Pérez', 'juan.perez@email.com', 1),
    (2, 'Ana Gómez', 'ana.gomez@email.com', 1);

-- Tabla pedidos
INSERT INTO pedidos (id, cliente_id, total, estado, fecha_realizado, fecha_entregado) VALUES
    (1, 1, 349, 1, '2024-06-01 10:00:00', '2024-06-03 15:00:00'),
    (2, 2, 120, 0, '2024-06-02 12:30:00', NULL);

-- Tabla detalles_pedido
INSERT INTO detalles_pedido (id, pedido_id, cantidad, isbn) VALUES
    (1, 1, 1, '100'),
    (2, 1, 1, '101'),
    (3, 2, 1, '102');