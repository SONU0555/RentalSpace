-- Modifying the amenity types
ALTER TABLE amenities
MODIFY COLUMN type ENUM('SWIMMING_POOL', 'GYM', 'CLUBHOUSE', 'PARKING', 'CONFERENCE_ROOM',
 'MARRIAGE_HALL', 'LIBRARY', 'SPORTS_COURT');



ALTER TABLE amenity_bookings 
DROP COLUMN start_date,
DROP COLUMN start_time,
DROP COLUMN end_date,
DROP COLUMN end_time,
DROP COLUMN duration_minutes,
ADD COLUMN start_at DATETIME NOT NULL,
ADD COLUMN end_at DATETIME NOT NULL;