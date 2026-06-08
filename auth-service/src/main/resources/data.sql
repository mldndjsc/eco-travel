INSERT INTO roles (name)
SELECT 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');

INSERT INTO roles (name)
SELECT 'GUEST'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'GUEST');

INSERT INTO roles (name)
SELECT 'GUIDE'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'GUIDE');

INSERT INTO users (username, api_key)
SELECT 'admin', 'admin-key-123'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO users (username, api_key)
SELECT 'guest', 'guest-key-123'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'guest');

INSERT INTO users (username, api_key)
SELECT 'guide', 'guide-key-123'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'guide');

INSERT INTO user_roles (user_id, role_id)
SELECT users.id, roles.id
FROM users
JOIN roles ON roles.name = 'ADMIN'
WHERE users.username = 'admin'
  AND NOT EXISTS (
      SELECT 1 FROM user_roles
      WHERE user_id = users.id
        AND role_id = roles.id
  );

INSERT INTO user_roles (user_id, role_id)
SELECT users.id, roles.id
FROM users
JOIN roles ON roles.name = 'GUEST'
WHERE users.username = 'guest'
  AND NOT EXISTS (
      SELECT 1 FROM user_roles
      WHERE user_id = users.id
        AND role_id = roles.id
  );

INSERT INTO user_roles (user_id, role_id)
SELECT users.id, roles.id
FROM users
JOIN roles ON roles.name = 'GUIDE'
WHERE users.username = 'guide'
  AND NOT EXISTS (
      SELECT 1 FROM user_roles
      WHERE user_id = users.id
        AND role_id = roles.id
  );
