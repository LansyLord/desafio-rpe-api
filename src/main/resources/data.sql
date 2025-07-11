-- Clientes
INSERT INTO cliente (id, nome, cpf, data_nascimento, status_bloqueio, limite_credito) VALUES
(1, 'João da Silva', '11111111111', '1990-01-01', 'A', 5000.00),
(2, 'Maria Souza', '22222222222', '1985-05-20', 'B', 0.00),
(3, 'Carlos Oliveira', '33333333333', '1978-11-10', 'A', 3000.00),
(4, 'Ana Lima', '44444444444', '1992-03-15', 'B', 0.00),
(5, 'Pedro Santos', '55555555555', '2000-07-07', 'A', 2000.00);

-- Faturas
INSERT INTO fatura (id, cliente_id, data_vencimento, data_pagamento, valor, status) VALUES
(1, 1, '2025-07-01', '2025-07-05', 1000.00, 'P'), -- Paga
(2, 2, '2025-07-05', NULL, 800.00, 'A'),          -- Atrasada e cliente bloqueado
(3, 3, '2025-07-09', NULL, 600.00, 'B'),          -- Aberta
(4, 4, '2025-07-01', NULL, 500.00, 'A'),          -- Atrasada e cliente bloqueado
(5, 5, '2025-07-12', NULL, 700.00, 'B');          -- Aberta futura
