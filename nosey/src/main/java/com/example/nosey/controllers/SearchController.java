package com.example.nosey.controllers;

import com.example.nosey.DTO.PostDTO.PostDTO;
import com.example.nosey.DTO.UserDTO.UserDTO;
import com.example.nosey.repositories.PostRepository;
import com.example.nosey.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SearchController {

    @Autowired
    private UserProfileRepository Repository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/search")
    public ResponseEntity<Map<String, List<?>>> search(@RequestParam String query) {
        List<UserDTO> users = Repository.findByFullNameContainingIgnoreCase(query)
                .stream()
                .map(u -> new UserDTO(u.getId(),u.getFirstName(), u.getLastName()))
                .collect(Collectors.toList());

        List<PostDTO> posts = postRepository.findByTitleContainingIgnoreCase(query)
                .stream()
                .map(p -> new PostDTO(p.getId(), p.getTitle(), p.getContent()))
                .collect(Collectors.toList());

        Map<String, List<?>> results = new HashMap<>();
        results.put("users", users);
        results.put("posts", posts);

        return ResponseEntity.ok(results);
    }
}
