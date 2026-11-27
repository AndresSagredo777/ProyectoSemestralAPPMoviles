package cl.duoc.mastermartini.service;

import cl.duoc.mastermartini.model.Video;
import cl.duoc.mastermartini.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {

    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public List<Video> obtenerVideos() {
        return videoRepository.findAll();
    }
}