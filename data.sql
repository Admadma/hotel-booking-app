INSERT INTO roles (name) VALUES ('ADMIN'), ('USER');

-- The password '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa' is the string 'TestPassword!' encrypted with BCryptPasswordEncoder
INSERT INTO users (username, password, email, enabled) VALUES
    ('jane_doe', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'jane@example.com', 1),
    ('bob_smith', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'bob@example.com', 1),
    ('emily_jones', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'emily@example.com', 1),
    ('michael_brown', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'michael@example.com', 1),
    ('susan_smith', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'susan@example.com', 1),
    ('david_jones', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'david@example.com', 1),
    ('jessica_williams', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'jessica@example.com', 1),
    ('matthew_taylor', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'matthew@example.com', 1),
    ('sarah_miller', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'sarah@example.com', 1),
    ('chris_wilson', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'chris@example.com', 1),
    ('laura_anderson', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'laura@example.com', 1),
    ('daniel_thomas', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'daniel@example.com', 1),
    ('rebecca_wilson', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'rebecca@example.com', 1),
    ('justin_anderson', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'justin@example.com', 1),
    ('lisa_roberts', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'lisa@example.com', 1),
    ('eric_taylor', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'eric@example.com', 1),
    ('ashley_miller', '$2a$10$rMWQL0K1e3XSlxCEO1zKDOfWMqCcIDp7sjEiUI0.uFXp24BK21Afa', 'ashley@example.com', 1);

INSERT INTO users_roles (user_id, role_id)
    SELECT u.id, r.id FROM users u CROSS JOIN roles r WHERE r.name='USER';

INSERT INTO confirmation_tokens (token, user_id, created_at, expires_at)
    SELECT '71a0a0d5-b57f-444c-bf9b-883256e5a323', u.id, '2024-04-02 12:38:50.077802', '2024-04-02 13:08:50.077802' FROM users u;
