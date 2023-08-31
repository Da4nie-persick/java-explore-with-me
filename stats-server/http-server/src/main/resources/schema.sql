CREATE TABLE IF NOT EXISTS statistics (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY UNIQUE,
  app VARCHAR(255) NOT NULL,
  uri VARCHAR(512) NOT NULL,
  ip VARCHAR(512) NOT NULL,
  time_stamp TIMESTAMP NOT NULL
);