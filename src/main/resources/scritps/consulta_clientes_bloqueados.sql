-- consulta_clientes_bloqueados.sql

SELECT DISTINCT c.*
FROM cliente c
JOIN fatura f ON f.cliente_id = c.id
WHERE f.status = 'A'
  AND f.data_vencimento < CURRENT_DATE - INTERVAL 3 DAY
  AND c.status_bloqueio = 'B';
