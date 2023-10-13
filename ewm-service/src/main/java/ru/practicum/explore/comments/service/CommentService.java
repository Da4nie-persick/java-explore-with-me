package ru.practicum.explore.comments.service;

import ru.practicum.explore.comments.dto.CommentDto;
import ru.practicum.explore.comments.dto.NewCommentDto;
import ru.practicum.explore.comments.dto.UpdateComment;

import java.util.List;

public interface CommentService {
    CommentDto create(Integer userId, NewCommentDto newCommentDto);

    CommentDto updateComment(Integer userId, UpdateComment updateCommentDto);

    void deleteComment(Integer userId, Integer comId);

    List<CommentDto> getAllCommentsByAuthorAndEvent(Integer userId, Integer eventId, Integer from, Integer size);

    CommentDto updateCommentAdmin(UpdateComment updateComment);

    void deleteCommentAdmin(Integer comId);

    List<CommentDto> searchCommentAdmin(String text, Integer from, Integer size);

    CommentDto getCommentId(Integer comId);

    List<CommentDto> getAllCommentsEvent(Integer eventId, Integer from, Integer size);
}
