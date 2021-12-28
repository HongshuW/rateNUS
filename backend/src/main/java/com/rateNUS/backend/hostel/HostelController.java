package com.rateNUS.backend.hostel;

import com.rateNUS.backend.auth.MessageResponse;
import com.rateNUS.backend.security.ApplicationUserRole;
import com.rateNUS.backend.security.jwt.JwtUtils;
import com.rateNUS.backend.user.User;
import com.rateNUS.backend.user.UserService;
import com.rateNUS.backend.util.Config;
import com.rateNUS.backend.util.Facility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Serves as the API layer for Hostels.
 */
@RestController
@RequestMapping(path = "hostel")
@CrossOrigin(Config.frontendURL)
public class HostelController {
    private final HostelService hostelService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    private final int default_pageNum = 0;
    private final int default_pageSize = 5;

    @Autowired
    public HostelController(HostelService hostelService, UserService userService, JwtUtils jwtUtils) {
        this.hostelService = hostelService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping(path = "{hostelId}")
    public Hostel getHostel(@PathVariable("hostelId") long hostelId) {
        return hostelService.getHostel(hostelId);
    }

    @PostMapping
    public Page<Hostel> getHostels(@RequestBody Map<String, Object> jsonInput) {
        String orderBy = (String) jsonInput.getOrDefault("orderBy", "id");
        boolean isLowToHigh = (boolean) jsonInput.getOrDefault("isLowToHigh", true);
        int pageNum = (int) jsonInput.getOrDefault("pageNum", default_pageNum);
        int pageSize = (int) jsonInput.getOrDefault("pageSize", default_pageSize);

        return hostelService.getHostels(orderBy, isLowToHigh, pageNum, pageSize);
    }

    @PostMapping(path = "search")
    public Page<Hostel> findHostel(@RequestBody Map<String, Object> jsonInput) {
        String keyword = (String) jsonInput.getOrDefault("keyword", "");
        int pageNum = (int) jsonInput.getOrDefault("pageNum", default_pageNum);
        int pageSize = (int) jsonInput.getOrDefault("pageSize", default_pageSize);

        return hostelService.findHostel(keyword, pageNum, pageSize);
    }

    // admin function
    @PutMapping(path = "update/{hostelId}")
    public ResponseEntity<?> updateHostel(@PathVariable("hostelId") long hostelId,
                                          @RequestParam(name = "token") String token,
                                          @RequestParam(name = "username") String username,
                                          @RequestBody Map<String, Object> jsonInput) {
        if (!jwtUtils.tokenBelongsToUser(token, username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User user = userService.findByUsername(username);
        if (user == null || !user.getRoles().contains(ApplicationUserRole.ADMIN)) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse("User doesn't have permission to update hostel.")
            );
        }

        Hostel hostel;
        try {
            hostel = hostelService.getHostel(hostelId);
        } catch (TypeNotPresentException e) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse("Hostel with id: " + hostelId + " doesn't exist.")
            );
        }

        try {
            if (jsonInput.containsKey("name")) {
                String name = (String) jsonInput.get("name");
                hostel.setName(name);
            }
            if (jsonInput.containsKey("location")) {
                String location = (String) jsonInput.get("location");
                hostel.setLocation(location);
            }
            if (jsonInput.containsKey("description")) {
                String description = (String) jsonInput.get("description");
                hostel.setDescription(description);
            }
            if (jsonInput.containsKey("imageUrl")) {
                List<String> imageUrl = (List<String>) jsonInput.get("imageUrl");
                hostel.setImageUrl(imageUrl);
            }
            if (jsonInput.containsKey("facilities")) {
                List<Facility> facilities = (List<Facility>) jsonInput.get("facilities");
                hostel.setFacilities(facilities);
            }
            hostelService.saveHostel(hostel);
        } catch (ClassCastException e) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse(e.getMessage())
            );
        }

        return ResponseEntity.ok(new MessageResponse("Hostel updated successfully."));
    }

    // admin function
    @DeleteMapping(path = "delete/{hostelId}")
    public ResponseEntity<?> deleteHostel(@PathVariable("hostelId") long hostelId,
                                          @RequestParam(name = "token") String token,
                                          @RequestParam(name = "username") String username) {
        if (!jwtUtils.tokenBelongsToUser(token, username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User user = userService.findByUsername(username);
        if (user == null || !user.getRoles().contains(ApplicationUserRole.ADMIN)) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse("User doesn't have permission to delete hostel.")
            );
        }

        Hostel hostel;
        try {
            hostel = hostelService.deleteHostel(hostelId);
        } catch (TypeNotPresentException e) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse("Hostel with id: " + hostelId + " doesn't exist.")
            );
        }

        return ResponseEntity.ok(new MessageResponse("Hostel deleted successfully."));
    }
}
