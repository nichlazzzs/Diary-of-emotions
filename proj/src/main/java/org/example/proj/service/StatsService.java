package org.example.proj.service;

import org.example.proj.dto.EmotionStatsDto;
import org.example.proj.dto.TrendPointDto;
import org.example.proj.entity.User;
import org.example.proj.repository.NoteRepository;
import org.example.proj.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatsService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public StatsService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public List<EmotionStatsDto> getEmotionStats(String period, Authentication authentication) {
        User user = getUser(authentication);
        LocalDateTime[] range = resolvePeriod(period);
        List<Object[]> stats = noteRepository.countEmotionsByPeriod(user, range[0], range[1]);

        List<EmotionStatsDto> result = new ArrayList<>();
        for (Object[] row : stats) {
            String emotion = (String) row[0];
            Long count = (Long) row[1];
            result.add(new EmotionStatsDto(emotion != null ? emotion : "Не указано", count));
        }

        return result;
    }

    public List<TrendPointDto> getTrendStats(String period, Authentication authentication) {
        User user = getUser(authentication);
        LocalDateTime[] range = resolvePeriod(period);
        List<Object[]> stats = noteRepository.noteTrendsByDay(user, range[0], range[1]);

        List<TrendPointDto> result = new ArrayList<>();
        for (Object[] row : stats) {
            LocalDate date = ((java.sql.Timestamp) row[0]).toLocalDateTime().toLocalDate();
            Long count = (Long) row[1];
            result.add(new TrendPointDto(date, count));
        }

        return result;
    }

    private LocalDateTime[] resolvePeriod(String period) {
        LocalDateTime now = LocalDateTime.now();
        return switch (period.toLowerCase()) {
            case "week" -> new LocalDateTime[]{now.minusWeeks(1), now};
            case "month" -> new LocalDateTime[]{now.minusMonths(1), now};
            case "year" -> new LocalDateTime[]{now.minusYears(1), now};
            default -> throw new IllegalArgumentException("Неверный период: " + period);
        };
    }

    private User getUser(Authentication authentication) {
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }
}
