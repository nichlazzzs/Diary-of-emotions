package org.example.proj;

import org.example.proj.controller.NoteController;
import org.example.proj.entity.Note;
import org.example.proj.entity.User;
import org.example.proj.repository.NoteRepository;
import org.example.proj.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class NoteControllerTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private NoteController noteController;

    @Test
    void createNote_ShouldSaveNote() {
        // Arrange
        Note note = new Note();
        User user = new User();
        user.setUsername("testuser");

        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(noteRepository.save(any())).thenReturn(note);

        // Act
        Note result = noteController.createNote(note, authentication);

        // Assert
        assertNotNull(result);
        verify(noteRepository, times(1)).save(any());
    }

    @Test
    void getNotes_ShouldReturnUserNotes() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        Note note1 = new Note();
        Note note2 = new Note();

        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(noteRepository.findByUser(user)).thenReturn(Arrays.asList(note1, note2));

        // Act
        List<Note> notes = noteController.getNotes(authentication);

        // Assert
        assertEquals(2, notes.size());
    }

    @Test
    void updateNote_ShouldUpdateExistingNote() {
        // Arrange
        Note existingNote = new Note();
        existingNote.setNoteId(1L);

        Note updatedNote = new Note();
        updatedNote.setEvent("Updated event");

        when(noteRepository.findById(1L)).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(any())).thenReturn(existingNote);

        // Act
        Note result = noteController.updateNote(1L, updatedNote);

        // Assert
        assertEquals("Updated event", result.getEvent());
    }

    @Test
    void deleteNote_ShouldDeactivateNote() {
        // Arrange
        Note note = new Note();
        note.setNoteId(1L);
        note.setIsActive(true);

        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));
        when(noteRepository.save(any())).thenReturn(note);

        // Act
        noteController.deleteNote(1L);

        // Assert
        assertFalse(note.getIsActive());
    }
}
