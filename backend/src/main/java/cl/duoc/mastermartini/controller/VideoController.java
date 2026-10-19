package cl.duoc.mastermartini.controller;

import cl.duoc.mastermartini.model.Video;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @GetMapping
    public List<Video> obtenerVideos() {

        return List.of(
                new Video(
                        1L,
                        "Introducción al temperado de chocolate",
                        "Aprende los conceptos básicos para trabajar correctamente el chocolate.",
                        "Chocolatería",
                        "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
                ),
                new Video(
                        2L,
                        "Fundamentos de panadería",
                        "Conceptos esenciales para preparar masas y panes.",
                        "Panadería",
                        "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
                ),
                new Video(
                        3L,
                        "Técnicas básicas de pastelería",
                        "Revisión de técnicas utilizadas en preparaciones de pastelería.",
                        "Pastelería",
                        "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
                )
        );
    }
}