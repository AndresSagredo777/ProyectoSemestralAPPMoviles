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
                        "Técnica con chocolate",
                        "Contenido técnico de Master Martini sobre preparación y trabajo con chocolate.",
                        "Chocolatería",
                        "https://www.youtube.com/watch?v=v_WinlHRQt0"
                ),
                new Video(
                        2L,
                        "Preparación de pastelería",
                        "Contenido técnico de Master Martini orientado a preparaciones de pastelería.",
                        "Pastelería",
                        "https://www.youtube.com/watch?v=vmC2JpTvrLM"
                ),
                new Video(
                        3L,
                        "Preparación de heladería",
                        "Contenido técnico de Master Martini relacionado con productos y técnicas de heladería.",
                        "Heladería",
                        "https://www.youtube.com/watch?v=KhhA9pAJZxA"
                ),
                new Video(
                        4L,
                        "Masa de hojaldre",
                        "Preparación de masa laminada utilizando Master Export Hoja.",
                        "Bollería",
                        "https://www.youtube.com/watch?v=1yv3Kb7KdEk"
                )
        );
    }
}