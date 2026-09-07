ALTER TABLE properties DROP FOREIGN KEY fk_properties_users_admin;

ALTER TABLE properties DROP COLUMN admin_user_id;

ALTER TABLE properties ADD COLUMN admin_id BINARY(16) NULL;

ALTER TABLE properties ADD CONSTRAINT uk_properties_admin_id UNIQUE (admin_id);

ALTER TABLE properties 
ADD CONSTRAINT fk_properties_users_admin 
FOREIGN KEY (admin_id) REFERENCES users(id);