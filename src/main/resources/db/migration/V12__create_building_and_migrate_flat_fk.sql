-- create the building table
-- CREATE TABLE buildings (
-- id            BINARY(16)   NOT NULL,
-- name          VARCHAR(225) NOT NULL,
-- total_floors  INT          NOT NULL,
-- building_code CHAR(1)      NOT NULL,
-- property_id   BINARY(16)   NOT NULL,
-- created_at    DATETIME(6)  NOT NULL,
-- updated_at    DATETIME(6)  NOT NULL,
-- PRIMARY KEY (id),
-- KEY idx_building_property_id (property_id),  
--     CONSTRAINT fk_building_property
--         FOREIGN KEY (property_id) REFERENCES properties (id)
--         ON DELETE RESTRICT
--         ON UPDATE CASCADE
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- add building_id column and remove property_id column from flat table
-- ALTER TABLE flats
--     ADD COLUMN building_id BINARY(16) NOT NULL AFTER property_id;
-- 
-- ALTER TABLE flats DROP FOREIGN KEY FK1r9dvdb6wt430mpustc1ovleh;
-- ALTER TABLE flats DROP COLUMN property_id;

-- Add fk constraint and reference in flat table fro building id
ALTER TABLE flats
ADD KEY idx_flat_building_id (building_id),
ADD CONSTRAINT fk_flat_building
        FOREIGN KEY (building_id) REFERENCES buildings (id)
        ON DELETE CASCADE ON UPDATE CASCADE;