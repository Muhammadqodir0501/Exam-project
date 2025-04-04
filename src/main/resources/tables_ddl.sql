-- Table for storing image files
CREATE TABLE attachments (
                             id SERIAL PRIMARY KEY,
                             content BYTEA NOT NULL
);

-- Users table
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password TEXT NOT NULL,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       photo_id INTEGER,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT fk_user_photo FOREIGN KEY (photo_id) REFERENCES attachments(id) ON DELETE SET NULL
);

-- Publications table
CREATE TABLE publications (
                              id SERIAL PRIMARY KEY,
                              user_id INTEGER NOT NULL,
                              title VARCHAR(255) NOT NULL,
                              description TEXT NOT NULL,
                              publication_photo_id INTEGER,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT fk_publication_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                              CONSTRAINT fk_publication_photo FOREIGN KEY (publication_photo_id) REFERENCES attachments(id) ON DELETE SET NULL
);

-- Comments table
CREATE TABLE comments (
                          id SERIAL PRIMARY KEY,
                          user_id INTEGER NOT NULL,
                          publication_id INTEGER NOT NULL,
                          content TEXT NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                          CONSTRAINT fk_comment_publication FOREIGN KEY (publication_id) REFERENCES publications(id) ON DELETE CASCADE
);
