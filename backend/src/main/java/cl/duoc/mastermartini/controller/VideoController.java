
package cl.duoc.mastermartini.controller;

import cl.duoc.mastermartini.model.Video;
import cl.duoc.mastermartini.service.VideoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping
    public List<Video> obtenerVideos() {
        return videoService.obtenerVideos();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Video guardarVideo(@RequestBody Video video) {
        return videoService.guardarVideo(video);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVideo(@PathVariable Long id) {

        if (!videoService.eliminarVideo(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
