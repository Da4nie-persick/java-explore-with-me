CREATE TABLE IF NOT EXISTS categories (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY UNIQUE,
  name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY UNIQUE,
  email VARCHAR(254) NOT NULL,
  name VARCHAR(250) NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY UNIQUE,
  annotation VARCHAR(2000) NOT NULL,
  category_id INTEGER NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
  confirmed_requests INTEGER,
  created_on TIMESTAMP,
  description VARCHAR(7000),
  event_date TIMESTAMP,
  initiator_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  lat FLOAT NOT NULL,
  lon FLOAT NOT NULL,
  paid BOOLEAN NOT NULL,
  participant_limit INTEGER NOT NULL,
  published_on TIMESTAMP,
  request_moderation BOOLEAN NOT NULL,
  state VARCHAR(30) NOT NULL,
  title VARCHAR(120) NOT NULL
);

CREATE TABLE IF NOT EXISTS requests (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY UNIQUE,
  created TIMESTAMP NOT NULL,
  event_id INTEGER REFERENCES events(id) NOT NULL,
  requester_id INTEGER REFERENCES users(id) NOT NULL,
  status VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY UNIQUE,
  pinned BOOLEAN NOT NULL,
  title VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations_events (
  compilation_id INTEGER NOT NULL REFERENCES compilations(id),
  event_id INTEGER NOT NULL REFERENCES events(id)
);

CREATE TABLE IF NOT EXISTS comments (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY UNIQUE,
  text VARCHAR(2000) NOT NULL,
  author_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  event_id INTEGER REFERENCES events(id) NOT NULL,
  created TIMESTAMP NOT NULL
);