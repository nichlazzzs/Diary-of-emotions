package org.example.proj.repository;

import org.example.proj.entity.Emotion;
import org.example.proj.entity.Note;
import org.example.proj.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUser(User user);
    List<Note> findByUserAndDateBetween(User user, LocalDateTime start, LocalDateTime end);
    List<Note> findByUserAndEmotion(User user, Emotion emotion);
    @Query("SELECT n.emotion.emotion, COUNT(n) FROM Note n WHERE n.user = :user AND n.date BETWEEN :start AND :end GROUP BY n.emotion.emotion")
    List<Object[]> countEmotionsByPeriod(@Param("user") User user, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT FUNCTION('DATE_TRUNC', 'day', n.date), COUNT(n) FROM Note n WHERE n.user = :user AND n.date BETWEEN :start AND :end GROUP BY FUNCTION('DATE_TRUNC', 'day', n.date) ORDER BY 1")
    List<Object[]> noteTrendsByDay(@Param("user") User user, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}