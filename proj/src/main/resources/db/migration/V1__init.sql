CREATE TABLE users (
    userID SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(30) NOT NULL,
    doctorID INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE patientDoctor (
    UserID INTEGER NOT NULL,
    DoctorID INTEGER NOT NULL,
    PRIMARY KEY (UserID, DoctorID),
    FOREIGN KEY (UserID) REFERENCES users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (DoctorID) REFERENCES users(UserID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id INT NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users(userID)
    );

INSERT INTO user_roles (user_id, role)
SELECT userID, 'USER' FROM users;

ALTER TABLE users ALTER COLUMN Password TYPE VARCHAR(100);