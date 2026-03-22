CREATE TABLE categories (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(120) NOT NULL UNIQUE
);

CREATE TABLE tags (
    id BINARY(16)  PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(120) NOT NULL UNIQUE
);

CREATE TABLE posts (
    id BINARY(16)  PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    content_md TEXT NOT NULL,
    excerpt TEXT,
    status ENUM ('DRAFT', 'PUBLISHED') NOT NULL,
    published_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    category_id BINARY(16) ,
    
    CONSTRAINT fk_post_category
        FOREIGN KEY (category_id)
        REFERENCES categories(id)
        ON DELETE SET NULL
);

CREATE TABLE post_tags (
    post_id BINARY(16)  NOT NULL,
    tag_id BINARY(16)  NOT NULL,
    
    PRIMARY KEY (post_id, tag_id),
    
    CONSTRAINT fk_post_tags_post
        FOREIGN KEY (post_id)
        REFERENCES posts(id)
        ON DELETE CASCADE,
        
    CONSTRAINT fk_post_tags_tag
        FOREIGN KEY (tag_id)
        REFERENCES tags(id)
        ON DELETE CASCADE
);

CREATE TABLE comments (
    id BINARY(16)  PRIMARY KEY,
    author_name VARCHAR(150) NOT NULL,
    author_email VARCHAR(150) NOT NULL,
    body TEXT NOT NULL,
    status ENUM ('PENDING', 'APPROVED') NOT NULL,
    created_at TIMESTAMP NOT NULL,
    post_id BINARY(16)  NOT NULL,
    
    CONSTRAINT fk_comment_post
        FOREIGN KEY (post_id)
        REFERENCES posts(id)
        ON DELETE CASCADE
);