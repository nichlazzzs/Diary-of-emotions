CREATE TABLE emotions (
    emotionId SERIAL PRIMARY KEY,
    emotion TEXT NOT NULL
);

CREATE TABLE notes (
     noteId SERIAL PRIMARY KEY,
     event TEXT,
     arguments_for TEXT[],
     arguments_against TEXT[],
     adaptive_response TEXT,
     behavior TEXT,
     is_active BOOLEAN DEFAULT TRUE,
     body_sensations TEXT,
     conclusion TEXT,
     auto_thought TEXT,
     userId INTEGER NOT NULL,
     date TIMESTAMP NOT NULL,
     emotionId INTEGER,
     CONSTRAINT fk_notes_user FOREIGN KEY (userId) REFERENCES users(UserID),
     CONSTRAINT fk_notes_emotion FOREIGN KEY (emotionId) REFERENCES emotions(emotionId)
);

CREATE TABLE note_arguments_for (
                                    note_id INTEGER NOT NULL,
                                    argument TEXT NOT NULL,
                                    FOREIGN KEY (note_id) REFERENCES notes(noteid) ON DELETE CASCADE
);

CREATE TABLE note_arguments_against (
                                        note_id INTEGER NOT NULL,
                                        argument TEXT NOT NULL,
                                        FOREIGN KEY (note_id) REFERENCES notes(noteid) ON DELETE CASCADE
);