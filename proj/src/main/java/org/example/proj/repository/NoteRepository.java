package org.example.proj.repository;

import org.example.proj.entity.Emotion;
import org.example.proj.entity.Note;
import org.example.proj.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUser(User user);
    List<Note> findByUserAndDateBetween(User user, LocalDateTime start, LocalDateTime end);
    List<Note> findByUserAndEmotion(User user, Emotion emotion);
}