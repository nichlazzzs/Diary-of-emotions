package org.example.proj.controller;

import org.example.proj.entity.Note;
import org.example.proj.entity.User;
import org.example.proj.repository.NoteRepository;
import org.example.proj.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteController(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public Note createNote(@RequestBody Note note, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        note.setUser(user);
        note.setDate(LocalDateTime.now());
        return noteRepository.save(note);
    }

    @GetMapping
    public List<Note> getNotes(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return noteRepository.findByUser(user);
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable Long id, @RequestBody Note noteDetails) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        note.setEvent(noteDetails.getEvent());
        note.setArgumentsFor(noteDetails.getArgumentsFor());
        note.setArgumentsAgainst(noteDetails.getArgumentsAgainst());
        note.setAdaptiveResponse(noteDetails.getAdaptiveResponse());
        note.setBehavior(noteDetails.getBehavior());
        note.setBodySensations(noteDetails.getBodySensations());
        note.setConclusion(noteDetails.getConclusion());
        note.setAutoThought(noteDetails.getAutoThought());
        return noteRepository.save(note);
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        note.setIsActive(false);
        noteRepository.save(note);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("@accessService.hasAccess(principal, #patientId)")
    public List<Note> getNotesForPatient(@PathVariable Long patientId) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return noteRepository.findByUser(patient);
    }

}