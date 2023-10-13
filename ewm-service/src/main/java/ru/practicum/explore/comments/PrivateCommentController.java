package ru.practicum.explore.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.comments.dto.CommentDto;
import ru.practicum.explore.comments.dto.NewCommentDto;
import ru.practicum.explore.comments.dto.UpdateComment;
import ru.practicum.explore.comments.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/comments")
@Validated
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@PathVariable Integer userId, @Valid @RequestBody NewCommentDto newCommentDto) {
        return commentService.create(userId, newCommentDto);
    }

    @PatchMapping
    public CommentDto updateComment(@PathVariable Integer userId, @Valid @RequestBody UpdateComment updateComment) {
        return commentService.updateComment(userId, updateComment);
    }

    @DeleteMapping("/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Integer userId, @PathVariable Integer comId) {
        commentService.deleteComment(userId, comId);
    }

    @GetMapping
    public List<CommentDto> getAllCommentsByAuthorAndEvent(@PathVariable Integer userId,
                                                           @RequestParam(required = false) Integer eventId,
                                                           @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                           @Positive @RequestParam(defaultValue = "10") Integer size) {
        return commentService.getAllCommentsByAuthorAndEvent(userId, eventId, from, size);
    }
}
