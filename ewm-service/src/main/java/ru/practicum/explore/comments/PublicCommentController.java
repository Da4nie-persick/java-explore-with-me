package ru.practicum.explore.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.comments.dto.CommentDto;
import ru.practicum.explore.comments.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
public class PublicCommentController {
    private final CommentService commentService;

    @GetMapping("/{comId}")
    public CommentDto getCommentId(@PathVariable Integer comId) {
        return commentService.getCommentId(comId);
    }

    @GetMapping
    public List<CommentDto> getAllCommentsEvent(@RequestParam Integer eventId,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        return commentService.getAllCommentsEvent(eventId, from, size);
    }
}