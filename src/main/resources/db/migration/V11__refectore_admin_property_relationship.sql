-- Droping admin_id foreign key from property table
-- ALTER TABLE properties DROP FOREIGN KEY fk_properties_users_admin;

-- Removeing admin_id column form property table
-- ALTER TABLE properties DROP COLUMN admin_id;

-- Adding property_id column in admin table
-- ALTER TABLE admins ADD COLUMN property_id BINARY(16);

ALTER TABLE admins
ADD CONSTRAINT fk_admin_property
FOREIGN KEY (property_id) REFERENCES properties(id);

-- Adding new column in admin table for admin status
ALTER TABLE admins ADD COLUMN status ENUM('ASSIGNED', 'UNASSIGNED') NOT NULL DEFAULT 'UNASSIGNED';