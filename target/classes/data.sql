-- Insertion de données de test

-- Clients de test
INSERT INTO clients (nom, email, siret, date_creation) VALUES 
('Entreprise Alpha', 'contact@alpha.com', '12345678901234', CURRENT_TIMESTAMP),
('Société Beta', 'info@beta.fr', '23456789012345', CURRENT_TIMESTAMP),
('Compagnie Gamma', 'admin@gamma.org', '34567890123456', CURRENT_TIMESTAMP);

-- Factures de test
INSERT INTO factures (numero, date, client_id, total_ht, total_tva, total_ttc) VALUES 
('FAC-2024-0001', '2024-01-15', 1, 1000.00, 200.00, 1200.00),
('FAC-2024-0002', '2024-01-20', 2, 500.00, 100.00, 600.00),
('FAC-2024-0003', '2024-02-01', 1, 750.00, 150.00, 900.00);

-- Lignes de facture de test
INSERT INTO lignes_facture (description, quantite, prix_unitaire_ht, taux_tva, facture_id) VALUES 
('Développement application web', 10.00, 80.00, 20.00, 1),
('Formation utilisateurs', 5.00, 40.00, 20.00, 1),
('Consultation technique', 8.00, 62.50, 20.00, 2),
('Maintenance mensuelle', 3.00, 250.00, 20.00, 3);
