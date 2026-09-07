-- Changing the attribute name in amenity enity isPaid to isFreeOrPaid
ALTER TABLE amenities CHANGE COLUMN is_paid is_free_or_paid BIT(1) NOT NULL;


ALTER TABLE amenity_bookings 
DROP COLUMN booking_date,
ADD COLUMN start_date DATETIME NOT NULL,
ADD COLUMN end_date DATETIME NOT NULL;