-- 1. Add the column using the exact binary type Hibernate uses for UUIDs
ALTER TABLE tenants 
ADD COLUMN flat_id BINARY(16) NULL;

-- 2. Add the UNIQUE constraint to protect the One-to-One mapping rule
ALTER TABLE tenants 
ADD CONSTRAINT uk_tenants_flat_id UNIQUE (flat_id);

-- 3. Add the Foreign Key constraint linking tenants to flats
ALTER TABLE tenants 
ADD CONSTRAINT fk_tenants_flat_id 
FOREIGN KEY (flat_id) REFERENCES flats(id);
