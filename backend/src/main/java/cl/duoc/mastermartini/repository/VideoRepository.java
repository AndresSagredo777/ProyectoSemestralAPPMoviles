package cl.duoc.mastermartini.repository;

import cl.duoc.mastermartini.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository
        extends JpaRepository<Video, Long> {
}