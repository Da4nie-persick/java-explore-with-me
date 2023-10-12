package ru.practicum.explore.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.comments.dto.CommentDto;
import ru.practicum.explore.comments.dto.UpdateComment;
import ru.practicum.explore.comments.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comments")
@Validated
public class AdminCommentController {
    private final CommentService commentService;

    @PatchMapping
    public CommentDto updateCommentAdmin(@Valid @RequestBody UpdateComment updateComment) {
        return commentService.updateCommentAdmin(updateComment);
    }

    @DeleteMapping("/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentAdmin(@PathVariable Integer comId) {
        commentService.deleteCommentAdmin(comId);
    }

    @GetMapping
    public List<CommentDto> searchCommentAdmin(@RequestParam(required = false) String text,
                                               @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                               @Positive @RequestParam(defaultValue = "10")Integer size) {
        return commentService.searchCommentAdmin(text, from, size);
    }
}