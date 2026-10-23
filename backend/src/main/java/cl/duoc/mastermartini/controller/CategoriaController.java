package cl.duoc.mastermartini.controller;

import cl.duoc.mastermartini.model.Categoria;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @GetMapping
    public List<Categoria> obtenerCategorias() {

        return List.of(
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
        );
    }
}