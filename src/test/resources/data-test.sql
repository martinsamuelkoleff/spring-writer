-- categories
INSERT INTO categories (id, name, slug) VALUES
  (UUID_TO_BIN('a1b2c3d4-0001-0000-0000-000000000001'), 'Tecnología',   'tecnologia'),
  (UUID_TO_BIN('a1b2c3d4-0002-0000-0000-000000000002'), 'Programación', 'programacion'),
  (UUID_TO_BIN('a1b2c3d4-0003-0000-0000-000000000003'), 'Devops',        'devops');

-- tags
INSERT INTO tags (id, name, slug) VALUES
  (UUID_TO_BIN('b1b2c3d4-0001-0000-0000-000000000001'), 'Java',        'java'),
  (UUID_TO_BIN('b1b2c3d4-0002-0000-0000-000000000002'), 'Spring Boot', 'spring-boot'),
  (UUID_TO_BIN('b1b2c3d4-0003-0000-0000-000000000003'), 'Docker',      'docker'),
  (UUID_TO_BIN('b1b2c3d4-0004-0000-0000-000000000004'), 'Testing',     'testing');

-- posts
INSERT INTO posts (id, title, slug, content_md, excerpt, status, published_at, created_at, updated_at, category_id) VALUES
  (
    UUID_TO_BIN('c1b2c3d4-0001-0000-0000-000000000001'),
    'Introducción a Spring Boot',
    'introduccion-spring-boot',
    '# Introducción a Spring Boot\n\nSpring Boot simplifica la configuración...',
    'Una guía rápida para empezar con Spring Boot.',
    'PUBLISHED',
    '2024-03-01 10:00:00',
    '2024-03-01 09:00:00',
    '2024-03-01 10:00:00',
    UUID_TO_BIN('a1b2c3d4-0002-0000-0000-000000000002')  -- Programación
  ),
  (
    UUID_TO_BIN('c1b2c3d4-0002-0000-0000-000000000002'),
    'Testing con JUnit 5 y Mockito',
    'testing-junit5-mockito',
    '# Testing en Java\n\nEn este post vemos cómo testear con JUnit 5...',
    'Aprende a escribir tests unitarios en Java.',
    'PUBLISHED',
    '2024-04-15 12:00:00',
    '2024-04-14 08:00:00',
    '2024-04-15 12:00:00',
    UUID_TO_BIN('a1b2c3d4-0002-0000-0000-000000000002')  -- Programación
  ),
  (
    UUID_TO_BIN('c1b2c3d4-0003-0000-0000-000000000003'),
    'Docker para desarrolladores',
    'docker-para-desarrolladores',
    '# Docker\n\nDocker permite containerizar aplicaciones fácilmente...',
    'Introducción práctica a Docker.',
    'DRAFT',
    NULL,
    '2024-05-01 10:00:00',
    '2024-05-01 10:00:00',
    UUID_TO_BIN('a1b2c3d4-0003-0000-0000-000000000003')  -- Devops
  );

-- post_tags
INSERT INTO post_tags (post_id, tag_id) VALUES
  (UUID_TO_BIN('c1b2c3d4-0001-0000-0000-000000000001'), UUID_TO_BIN('b1b2c3d4-0001-0000-0000-000000000001')),  -- Spring Boot post -> Java
  (UUID_TO_BIN('c1b2c3d4-0001-0000-0000-000000000001'), UUID_TO_BIN('b1b2c3d4-0002-0000-0000-000000000002')),  -- Spring Boot post -> Spring Boot
  (UUID_TO_BIN('c1b2c3d4-0002-0000-0000-000000000002'), UUID_TO_BIN('b1b2c3d4-0001-0000-0000-000000000001')),  -- Testing post    -> Java
  (UUID_TO_BIN('c1b2c3d4-0002-0000-0000-000000000002'), UUID_TO_BIN('b1b2c3d4-0004-0000-0000-000000000004')),  -- Testing post    -> Testing
  (UUID_TO_BIN('c1b2c3d4-0003-0000-0000-000000000003'), UUID_TO_BIN('b1b2c3d4-0003-0000-0000-000000000003'));  -- Docker post     -> Docker

-- comments
INSERT INTO comments (id, author_name, author_email, body, status, created_at, post_id) VALUES
  (
    UUID_TO_BIN('d1b2c3d4-0001-0000-0000-000000000001'),
    'Ana García',
    'ana@example.com',
    'Excelente post, muy claro y conciso.',
    'APPROVED',
    '2024-03-02 11:00:00',
    UUID_TO_BIN('c1b2c3d4-0001-0000-0000-000000000001')
  ),
  (
    UUID_TO_BIN('d1b2c3d4-0002-0000-0000-000000000002'),
    'Carlos López',
    'carlos@example.com',
    'Me ayudó mucho, gracias.',
    'APPROVED',
    '2024-04-16 09:30:00',
    UUID_TO_BIN('c1b2c3d4-0002-0000-0000-000000000002')
  ),
  (
    UUID_TO_BIN('d1b2c3d4-0003-0000-0000-000000000003'),
    'Spam Bot',
    'spam@spam.com',
    'Buy cheap products at...',
    'PENDING',
    '2024-04-17 03:00:00',
    UUID_TO_BIN('c1b2c3d4-0002-0000-0000-000000000002')
  );