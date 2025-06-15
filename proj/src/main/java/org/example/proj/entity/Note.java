package org.example.proj.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.CollectionTable;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "notes")
@Data
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;

    private String event;

    @ElementCollection
    @CollectionTable(name = "note_arguments_for", joinColumns = @JoinColumn(name = "note_id"))
    @Column(name = "argument")
    private List<String> argumentsFor;

    @ElementCollection
    @CollectionTable(name = "note_arguments_against", joinColumns = @JoinColumn(name = "note_id"))
    @Column(name = "argument")
    private List<String> argumentsAgainst;

    private String adaptiveResponse;
    private String behavior;
    private Boolean isActive = true;
    private String bodySensations;
    private String conclusion;
    private String autoThought;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "emotionId")
    private Emotion emotion;
}