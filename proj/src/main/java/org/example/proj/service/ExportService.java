package org.example.proj.service;

import org.example.proj.dto.EmotionStatsDto;
import org.example.proj.entity.User;
import org.example.proj.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ExportService {

    private final StatsService statsService;
    private final UserRepository userRepository;

    public ExportService(StatsService statsService, UserRepository userRepository) {
        this.statsService = statsService;
        this.userRepository = userRepository;
    }

    public ResponseEntity<byte[]> export(String format, String period, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        List<EmotionStatsDto> stats = statsService.getEmotionStats(period, auth);

        byte[] content;
        String filename;
        String contentType;

        if (format.equalsIgnoreCase("csv")) {
            content = generateCsv(stats).getBytes(StandardCharsets.UTF_8);
            filename = "stats_" + period + ".csv";
            contentType = "text/csv";
        } else {
            content = generatePdf(stats, user.getUsername(), period);
            filename = "stats_" + period + ".pdf";
            contentType = "application/pdf";
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType(contentType))
                .body(content);
    }

    private String generateCsv(List<EmotionStatsDto> stats) {
        StringBuilder sb = new StringBuilder();
        sb.append("Эмоция,Количество\n");
        for (EmotionStatsDto dto : stats) {
            sb.append(dto.getEmotion()).append(",").append(dto.getCount()).append("\n");
        }
        return sb.toString();
    }

    private byte[] generatePdf(List<EmotionStatsDto> stats, String username, String period) {
        try (PDDocument doc = new PDDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            InputStream fontStream = getClass().getResourceAsStream("/fonts/arial.ttf");
            var font = PDType0Font.load(doc, fontStream);

            PDPageContentStream content = new PDPageContentStream(doc, page);
            content.beginText();
            content.setFont(font, 12);
            content.setLeading(16);
            content.newLineAtOffset(50, 750);

            content.showText("Статистика эмоций");
            content.newLine();
            content.showText("Пользователь: " + username);
            content.newLine();
            content.showText("Период: " + period);
            content.newLine();
            content.newLine();

            for (EmotionStatsDto dto : stats) {
                content.showText(dto.getEmotion() + ": " + dto.getCount());
                content.newLine();
            }

            content.endText();
            content.close();

            doc.save(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании PDF через PDFBox", e);
        }
    }

}
