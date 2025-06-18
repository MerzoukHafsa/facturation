-- Script d'initialisation de la base de données pour le module de facturation

-- Création des tables (si utilisation de PostgreSQL au lieu de H2)
-- CREATE TABLE IF NOT EXISTS clients (
--     id BIGSERIAL PRIMARY KEY,
--     nom VARCHAR(100) NOT NULL,
--     email VARCHAR(255) NOT NULL UNIQUE,
--     siret VARCHAR(14) NOT NULL UNIQUE,
--     date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
-- );

-- CREATE TABLE IF NOT EXISTS factures (
--     id BIGSERIAL PRIMARY KEY,
--     numero VARCHAR(50) NOT NULL UNIQUE,
--     date DATE NOT NULL,
--     client_id BIGINT NOT NULL REFERENCES clients(id),
--     total_ht DECIMAL(10,2) DEFAULT 0,
--     total_tva DECIMAL(10,2) DEFAULT 0,
--     total_ttc DECIMAL(10,2) DEFAULT 0
-- );

-- CREATE TABLE IF NOT EXISTS lignes_facture (
--     id BIGSERIAL PRIMARY KEY,
--     description VARCHAR(500) NOT NULL,
--     quantite DECIMAL(10,2) NOT NULL,
--     prix_unitaire_ht DECIMAL(10,2) NOT NULL,
--     taux_tva DECIMAL(5,2) NOT NULL,
--     facture_id BIGINT NOT NULL REFERENCES factures(id)
-- );

-- Données de test pour le développement
INSERT INTO clients (nom, email, siret, date_creation) VALUES 
('AriMayi Solutions', 'contact@arimayi.com', '12345678901234', CURRENT_TIMESTAMP),
('TechCorp SARL', 'info@techcorp.fr', '23456789012345', CURRENT_TIMESTAMP),
('Digital Services', 'admin@digital.com', '34567890123456', CURRENT_TIMESTAMP),
('Innovation Lab', 'hello@innovation.org', '45678901234567', CURRENT_TIMESTAMP);

-- Factures d'exemple
INSERT INTO factures (numero, date, client_id, total_ht, total_tva, total_ttc) VALUES 
('FAC-2024-0001', '2024-01-15', 1, 2000.00, 400.00, 2400.00),
('FAC-2024-0002', '2024-01-20', 2, 1500.00, 300.00, 1800.00),
('FAC-2024-0003', '2024-02-01', 3, 800.00, 160.00, 960.00),
('FAC-2024-0004', '2024-02-10', 1, 1200.00, 240.00, 1440.00);

-- Lignes de facture détaillées
INSERT INTO lignes_facture (description, quantite, prix_unitaire_ht, taux_tva, facture_id) VALUES 
-- Facture 1
('Développement module de facturation', 20.00, 80.00, 20.00, 1),
('Tests et validation', 5.00, 60.00, 20.00, 1),
('Documentation technique', 3.00, 100.00, 20.00, 1),
('Formation équipe', 2.00, 150.00, 20.00, 1),

-- Facture 2
('Audit sécurité application', 15.00, 90.00, 20.00, 2),
('Rapport d\'audit', 1.00, 150.00, 20.00, 2),

-- Facture 3
('Maintenance corrective', 8.00, 100.00, 20.00, 3),

-- Facture 4
('Développement API REST', 12.00, 100.00, 20.00, 4);
