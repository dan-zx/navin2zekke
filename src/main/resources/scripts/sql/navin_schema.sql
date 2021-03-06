DROP TABLE IF EXISTS path;
DROP TABLE IF EXISTS place;

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
