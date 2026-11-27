package cl.duoc.mastermartini.service;

import cl.duoc.mastermartini.model.Categoria;
import cl.duoc.mastermartini.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> obtenerCategorias() {
        return categoriaRepository.findAll();
    }
}