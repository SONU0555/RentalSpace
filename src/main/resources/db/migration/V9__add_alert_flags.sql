-- Removing one column from amenities table
ALTER TABLE amenities 
DROP COLUMN max_slots_per_tenant;

-- fixing foreign key conflict b/w property and amenity
ALTER TABLE properties
DROP FOREIGN KEY fk_properties_users_admin;

ALTER TABLE properties 
ADD CONSTRAINT fk_properties_users_admin
FOREIGN KEY (admin_id) REFERENCES admins(id); 