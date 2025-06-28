package org.example.proj.dto;

public class EmotionStatsDto {
    private String emotion;
    private Long count;

    public EmotionStatsDto(String emotion, Long count) {
        this.emotion = emotion;
        this.count = count;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
