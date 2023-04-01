CREATE TABLE users(
                      id BIGSERIAL PRIMARY KEY,
                      firstname VARCHAR(128) NOT NULL ,
                      lastname VARCHAR(128) NOT NULL ,
                      username VARCHAR(128) NOT NULL UNIQUE ,
                      email VARCHAR(128) NOT NULL UNIQUE ,
                      bio TEXT,
                      password VARCHAR(3000),
                      created_date TIMESTAMP
);

CREATE TABLE post(
                     id BIGSERIAL PRIMARY KEY,
                     title VARCHAR(128),
                     caption VARCHAR(250),
                     location VARCHAR(128),
                     likes INT,
                     created_date TIMESTAMP,
                     user_id BIGINT REFERENCES users (id)
);

CREATE TABLE image(
                      id BIGSERIAL PRIMARY KEY,
                      name VARCHAR(128),
                      image_path VARCHAR(128),
                      user_id BIGINT REFERENCES users (id),
                      post_id BIGINT REFERENCES post (id)
);

CREATE TABLE comment(
                        id BIGSERIAL PRIMARY KEY,
                        post_id BIGINT REFERENCES post (id),
                        username VARCHAR(128) NOT NULL,
                        user_id BIGINT REFERENCES users (id),
                        created_date TIMESTAMP,
                        message TEXT NOT NULL
);