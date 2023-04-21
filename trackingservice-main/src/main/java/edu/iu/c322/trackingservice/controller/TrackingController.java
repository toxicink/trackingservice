package edu.iu.c322.trackingservice.controller;

import edu.iu.c322.trackingservice.model.Tracking;
import edu.iu.c322.trackingservice.repository.TrackingRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customers")
public class TrackingController {
    private TrackingRepository repository;

    public TrackingController(TrackingRepository repository) {
        this.repository = repository;
    }


    @GetMapping("/{orderId}/{itemId}")
    public ResponseEntity<List<Tracking>> find(@PathVariable int orderId,
                                               @PathVariable int itemId) {
        List<Tracking> trackings = repository.findByOrderIdAndItemId(orderId, itemId);

        if (trackings.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(trackings);
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<String> updateTrackingStatus(@PathVariable int orderId,
                                                       @RequestBody Map<String, String> payload) {
        int itemId = Integer.parseInt(payload.get("itemId"));
        String status = payload.get("status");

        List<Tracking> trackings = repository.findByOrderIdAndItemId(orderId, itemId);
        if (trackings.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Tracking tracking = trackings.get(0);
        tracking.setStatus(status);
        repository.save(tracking);

        return ResponseEntity.ok("Status updated successfully");
    }

}
