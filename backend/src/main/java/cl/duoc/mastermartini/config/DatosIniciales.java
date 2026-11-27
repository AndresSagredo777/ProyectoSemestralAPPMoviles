
package cl.duoc.mastermartini.config;

import cl.duoc.mastermartini.model.Categoria;
import cl.duoc.mastermartini.model.Video;
import cl.duoc.mastermartini.repository.CategoriaRepository;
import cl.duoc.mastermartini.repository.VideoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatosIniciales implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final VideoRepository videoRepository;

    public DatosIniciales(
            CategoriaRepository categoriaRepository,
            VideoRepository videoRepository
    ) {
        this.categoriaRepository = categoriaRepository;
        this.videoRepository = videoRepository;
    }

    @Override
    public void run(String... args) {

        // Cargar categorías solo si la tabla está vacía
        if (categoriaRepository.count() == 0) {

            categoriaRepository.saveAll(List.of(
                    new Categoria(
                            1L,
                            "Chocolatería",
                            "Técnicas, recetas y preparaciones con chocolate",
                            "🍫"
                    ),
                    new Categoria(
                            2L,
                            "Pastelería",
                            "Postres y técnicas profesionales",
                            "🧁"
                    ),
                    new Categoria(
                            3L,
                            "Panadería",
                            "Masas, panes y procesos de horneado",
                            "🍞"
                    ),
                    new Categoria(
                            4L,
                            "Heladería",
                            "Preparaciones frías y técnicas de heladería",
                            "🍨"
                    ),
                    new Categoria(
                            5L,
                            "Bollería",
                            "Masas laminadas y productos de bollería",
                            "🥐"
                    ),
                    new Categoria(
                            6L,
                            "Galletas y horneo",
                            "Productos horneados y técnicas de cocción",
                            "🍪"
                    )
            ));
        }

        // Cargar videos solo si la tabla está vacía
        if (videoRepository.count() == 0) {

            videoRepository.saveAll(List.of(
                    new Video(
                            1L,
                            "Técnica con chocolate",
                            "Contenido técnico de Master Martini sobre preparación y trabajo con chocolate.",
                            "Chocolatería",
                            "https://www.youtube.com/watch?v=v_WinlHRQt0",
                            "/recetas/receta-chocolate.pdf"
                    ),
                    new Video(
                            2L,
                            "Preparación de pastelería",
                            "Contenido técnico de Master Martini orientado a preparaciones de pastelería.",
                            "Pastelería",
                            "https://www.youtube.com/watch?v=vmC2JpTvrLM",
                            "/recetas/tarta-coco.pdf"
                    ),
                    new Video(
                            3L,
                            "Preparación de heladería",
                            "Contenido técnico de Master Martini relacionado con productos y técnicas de heladería.",
                            "Heladería",
                            "https://www.youtube.com/watch?v=KhhA9pAJZxA",
                            "/recetas/receta-helado.pdf"
                    ),
                    new Video(
                            4L,
                            "Masa de hojaldre",
                            "Preparación de masa laminada utilizando Master Export Hoja.",
                            "Bollería",
                            "https://www.youtube.com/watch?v=1yv3Kb7KdEk",
                            "/recetas/receta-ojaldre.pdf"
                    )
            ));
        }

        System.out.println(
                "Datos H2: "
                        + categoriaRepository.count() + " categorías y "
                        + videoRepository.count() + " videos"
        );
    }
}
