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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notes")
@Data
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noteid")
    private Long noteId;

    private String event;

    @ElementCollection
    @CollectionTable(
            name = "note_arguments_for",
            joinColumns = @JoinColumn(name = "note_id")
    )
    @Column(name = "argument")
    private List<String> argumentsFor = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "note_arguments_against",
            joinColumns = @JoinColumn(name = "note_id")
    )
    @Column(name = "argument")
    private List<String> argumentsAgainst = new ArrayList<>();

    @Column(name = "adaptive_response")
    private String adaptiveResponse;

    private String behavior;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "body_sensations")
    private String bodySensations;

    private String conclusion;

    @Column(name = "auto_thought")
    private String autoThought;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "emotionid")
    private Emotion emotion;
}