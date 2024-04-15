-- DISCLAIMER
-- This script is only for demonstration purposes.
-- It must not be used in live application


INSERT INTO roles (name) VALUES ('ADMIN'), ('USER');

-- The password '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa' is the string 'TestPassword!' encrypted with BCryptPasswordEncoder
INSERT INTO users (username, password, email, enabled) VALUES
    ('jane_doe', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'jane@example.com', 1),
    ('bob_smith', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'bob@example.com', 1),
    ('emily_jones', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'emily@example.com', 1),
    ('michael_brown', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'michael@example.com', 1),
    ('susan_smith', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'susan@example.com', 1),
    ('lisa_roberts', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'lisa@example.com', 1),
    ('eric_taylor', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'eric@example.com', 1),
    ('ashley_miller', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'ashley@example.com', 1);

INSERT INTO users_roles (user_id, role_id)
    SELECT u.id, r.id FROM users u CROSS JOIN roles r WHERE r.name='USER';

INSERT INTO confirmation_tokens (token, user_id, created_at, expires_at)
    SELECT '71a0a0d5-b57f-444c-bf9b-883256e5a323', u.id, '2024-04-02 12:38:50.077802', '2024-04-02 13:08:50.077802' FROM users u;

INSERT INTO hotels (hotel_name, city, image_name, average_rating) VALUES
    ('Hotel 1', 'City 1', 'hotel_image_1.png', 0.0),
    ('Hotel 2', 'City 1', 'hotel_image_2.png', 0.0),
    ('Hotel 3', 'City 2', 'hotel_image_3.png', 0.0),
    ('Hotel 4', 'City 2', 'hotel_image_4.png', 0.0),
    ('Hotel 5', 'City 3', 'hotel_image_5.png', 0.0),
    ('Hotel 6', 'City 4', 'hotel_image_6.png', 0.0);

INSERT INTO rooms (room_number, single_beds, double_beds, price_per_night, room_type, version, hotel_id) VALUES
    (1, 1, 0, 100, 'SINGLE_ROOM', 1, 1),
    (2, 1, 0, 100, 'SINGLE_ROOM', 1, 1),
    (3, 1, 0, 100, 'SINGLE_ROOM', 1, 1),
    (4, 1, 0, 100, 'SINGLE_ROOM', 1, 1),
    (5, 1, 0, 100, 'SINGLE_ROOM', 1, 1),
    (6, 2, 1, 300, 'FAMILY_ROOM', 1, 1),
    (7, 2, 1, 300, 'FAMILY_ROOM', 1, 1),
    (8, 2, 1, 300, 'FAMILY_ROOM', 1, 1),
    (9, 2, 1, 300, 'FAMILY_ROOM', 1, 1),
    (10, 2, 1, 300, 'FAMILY_ROOM', 1, 1),
    (11, 2, 2, 400, 'TWIN_ROOM', 1, 1),
    (12, 2, 2, 400, 'TWIN_ROOM', 1, 1),
    (13, 2, 2, 400, 'TWIN_ROOM', 1, 1),
    (14, 2, 2, 400, 'TWIN_ROOM', 1, 1),
    (15, 2, 2, 400, 'TWIN_ROOM', 1, 1),
    (1, 1, 0, 100, 'SINGLE_ROOM', 1, 2),
    (2, 1, 0, 100, 'SINGLE_ROOM', 1, 2),
    (3, 1, 0, 100, 'SINGLE_ROOM', 1, 2),
    (4, 1, 0, 100, 'SINGLE_ROOM', 1, 2),
    (5, 1, 0, 100, 'SINGLE_ROOM', 1, 2),
    (6, 2, 1, 300, 'FAMILY_ROOM', 1, 2),
    (7, 2, 1, 300, 'FAMILY_ROOM', 1, 2),
    (8, 2, 1, 300, 'FAMILY_ROOM', 1, 2),
    (9, 2, 1, 300, 'FAMILY_ROOM', 1, 2),
    (10, 2, 1, 300, 'FAMILY_ROOM', 1, 2),
    (11, 2, 2, 400, 'TWIN_ROOM', 1, 2),
    (12, 2, 2, 400, 'TWIN_ROOM', 1, 2),
    (13, 2, 2, 400, 'TWIN_ROOM', 1, 2),
    (14, 2, 2, 400, 'TWIN_ROOM', 1, 2),
    (15, 2, 2, 400, 'TWIN_ROOM', 1, 2),
    (1, 1, 0, 100, 'SINGLE_ROOM', 1, 3),
    (2, 1, 0, 100, 'SINGLE_ROOM', 1, 3),
    (3, 1, 0, 100, 'SINGLE_ROOM', 1, 3),
    (4, 1, 0, 100, 'SINGLE_ROOM', 1, 3),
    (5, 1, 0, 100, 'SINGLE_ROOM', 1, 3),
    (6, 2, 1, 300, 'FAMILY_ROOM', 1, 3),
    (7, 2, 1, 300, 'FAMILY_ROOM', 1, 3),
    (8, 2, 1, 300, 'FAMILY_ROOM', 1, 3),
    (9, 2, 1, 300, 'FAMILY_ROOM', 1, 3),
    (10, 2, 1, 300, 'FAMILY_ROOM', 1, 3),
    (11, 2, 2, 400, 'TWIN_ROOM', 1, 3),
    (12, 2, 2, 400, 'TWIN_ROOM', 1, 3),
    (13, 2, 2, 400, 'TWIN_ROOM', 1, 3),
    (14, 2, 2, 400, 'TWIN_ROOM', 1, 3),
    (15, 2, 2, 400, 'TWIN_ROOM', 1, 3),
    (1, 1, 0, 100, 'SINGLE_ROOM', 1, 4),
    (2, 1, 0, 100, 'SINGLE_ROOM', 1, 4),
    (3, 1, 0, 100, 'SINGLE_ROOM', 1, 4),
    (4, 1, 0, 100, 'SINGLE_ROOM', 1, 4),
    (5, 1, 0, 100, 'SINGLE_ROOM', 1, 4),
    (6, 2, 1, 300, 'FAMILY_ROOM', 1, 4),
    (7, 2, 1, 300, 'FAMILY_ROOM', 1, 4),
    (8, 2, 1, 300, 'FAMILY_ROOM', 1, 4),
    (9, 2, 1, 300, 'FAMILY_ROOM', 1, 4),
    (10, 2, 1, 300, 'FAMILY_ROOM', 1, 4),
    (11, 2, 2, 400, 'TWIN_ROOM', 1, 4),
    (12, 2, 2, 400, 'TWIN_ROOM', 1, 4),
    (13, 2, 2, 400, 'TWIN_ROOM', 1, 4),
    (14, 2, 2, 400, 'TWIN_ROOM', 1, 4),
    (15, 2, 2, 400, 'TWIN_ROOM', 1, 4),
    (1, 1, 0, 100, 'SINGLE_ROOM', 1, 5),
    (2, 1, 0, 100, 'SINGLE_ROOM', 1, 5),
    (3, 1, 0, 100, 'SINGLE_ROOM', 1, 5),
    (4, 1, 0, 100, 'SINGLE_ROOM', 1, 5),
    (5, 1, 0, 100, 'SINGLE_ROOM', 1, 5),
    (6, 2, 1, 300, 'FAMILY_ROOM', 1, 5),
    (7, 2, 1, 300, 'FAMILY_ROOM', 1, 5),
    (8, 2, 1, 300, 'FAMILY_ROOM', 1, 5),
    (9, 2, 1, 300, 'FAMILY_ROOM', 1, 5),
    (10, 2, 1, 300, 'FAMILY_ROOM', 1, 5),
    (11, 2, 2, 400, 'TWIN_ROOM', 1, 5),
    (12, 2, 2, 400, 'TWIN_ROOM', 1, 5),
    (13, 2, 2, 400, 'TWIN_ROOM', 1, 5),
    (14, 2, 2, 400, 'TWIN_ROOM', 1, 5),
    (15, 2, 2, 400, 'TWIN_ROOM', 1, 5),
    (1, 1, 0, 100, 'SINGLE_ROOM', 1, 6),
    (2, 1, 0, 100, 'SINGLE_ROOM', 1, 6),
    (3, 1, 0, 100, 'SINGLE_ROOM', 1, 6),
    (4, 1, 0, 100, 'SINGLE_ROOM', 1, 6),
    (5, 1, 0, 100, 'SINGLE_ROOM', 1, 6),
    (6, 2, 1, 300, 'FAMILY_ROOM', 1, 6),
    (7, 2, 1, 300, 'FAMILY_ROOM', 1, 6),
    (8, 2, 1, 300, 'FAMILY_ROOM', 1, 6),
    (9, 2, 1, 300, 'FAMILY_ROOM', 1, 6),
    (10, 2, 1, 300, 'FAMILY_ROOM', 1, 6),
    (11, 2, 2, 400, 'TWIN_ROOM', 1, 6),
    (12, 2, 2, 400, 'TWIN_ROOM', 1, 6),
    (13, 2, 2, 400, 'TWIN_ROOM', 1, 6),
    (14, 2, 2, 400, 'TWIN_ROOM', 1, 6),
    (15, 2, 2, 400, 'TWIN_ROOM', 1, 6);

INSERT INTO reservations (UUID, room_id, user_id, start_date, end_date, total_price, reservation_status) VALUES
    (UUID_TO_BIN(UUID()), 1, 1, '2024-04-02', '2024-04-10', 1000, 'COMPLETED'),
    (UUID_TO_BIN(UUID()), 16, 1, '2024-04-02', '2024-04-10', 1000, 'COMPLETED'),
    (UUID_TO_BIN(UUID()), 31, 1, '2024-04-02', '2024-04-10', 1000, 'COMPLETED'),
    (UUID_TO_BIN(UUID()), 46, 1, '2024-04-02', '2024-04-10', 1000, 'COMPLETED'),
    (UUID_TO_BIN(UUID()), 61, 1, '2024-04-02', '2024-04-10', 1000, 'COMPLETED'),
    (UUID_TO_BIN(UUID()), 76, 1, '2024-04-02', '2024-04-10', 1000, 'COMPLETED');

INSERT INTO reviews (rating, comment, hotel_id, user_id) SELECT (SELECT
                                                                  	CASE
                                                                  		WHEN RAND() < 0.6 THEN 5
                                                                  		WHEN RAND() < 0.7 THEN 4
                                                                          ELSE FLOOR(1 + RAND() * 3)
                                                                  	END),
                                                                 	 'Some comment.',
                                                                 	  hotels.id,
                                                                 	  user_ids.user_id FROM hotels
                                                                 	    CROSS JOIN (SELECT u.id 'user_id'
                                                                 	        FROM users u
                                                                 	        join users_roles ur on u.id=ur.user_id
                                                                 	        join roles r on ur.role_id=r.id
                                                                 	        where r.name='USER')
                                                                 	    AS user_ids;

UPDATE hotels JOIN
	(SELECT h.id 'id', ROUND(avg(r.rating), 1) 'avg_rating_rounded'
		FROM hotels h JOIN reviews r on h.id=r.hotel_id GROUP BY h.id)
    AS select_result ON hotels.id = select_result.id
    SET hotels.average_rating = select_result.avg_rating_rounded;
