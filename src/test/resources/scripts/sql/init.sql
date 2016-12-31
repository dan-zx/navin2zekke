CREATE TABLE place (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  latitude DOUBLE NOT NULL,
  longitude DOUBLE NOT NULL,
  CONSTRAINT place_uq UNIQUE (name, latitude, longitude)
);

CREATE TABLE path (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  place_id1 INT NOT NULL,
  place_id2 INT NOT NULL,
  distance DOUBLE NOT NULL DEFAULT '0',
  CONSTRAINT path_uq UNIQUE (place_id1, place_id2, distance)
);

ALTER TABLE path ADD FOREIGN KEY (place_id1) REFERENCES place (id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE path ADD FOREIGN KEY (place_id2) REFERENCES place (id) ON DELETE CASCADE ON UPDATE CASCADE;

INSERT INTO place (id, name, latitude, longitude) VALUES (1, 'CDMX', 19.389696, -99.118652);
INSERT INTO place (id, name, latitude, longitude) VALUES (2, 'Puebla', 19.031775, -98.184814);
INSERT INTO place (id, name, latitude, longitude) VALUES (3, 'Pachuca', 20.095030, -98.750610);
INSERT INTO place (id, name, latitude, longitude) VALUES (4, 'Veracruz', 19.153357, -98.130371);

INSERT INTO path (place_id1, place_id2, distance) VALUES (1, 2, 120000);
INSERT INTO path (place_id1, place_id2, distance) VALUES (2, 1, 120000);
INSERT INTO path (place_id1, place_id2, distance) VALUES (2, 3, 150000);
INSERT INTO path (place_id1, place_id2, distance) VALUES (3, 2, 150000);
INSERT INTO path (place_id1, place_id2, distance) VALUES (1, 3, 110000);
INSERT INTO path (place_id1, place_id2, distance) VALUES (3, 1, 110000);
INSERT INTO path (place_id1, place_id2, distance) VALUES (2, 4, 270000);
INSERT INTO path (place_id1, place_id2, distance) VALUES (4, 2, 270000);
