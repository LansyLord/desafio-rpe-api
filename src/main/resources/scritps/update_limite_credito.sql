-- update_limite_credito.sql

UPDATE cliente
SET limite_credito = 0.00
WHERE status_bloqueio = 'B';
