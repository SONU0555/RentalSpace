ALTER TABLE amenity_bookings
ADD COLUMN day_of_opening_alert_sent BOOLEAN NOT NULL DEFAULT FALSE,
ADD COLUMN hour_before_alert_sent BOOLEAN NOT NULL DEFAULT FALSE;