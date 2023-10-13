package ru.practicum.explore.comments.mapper;

import ru.practicum.explore.comments.dto.CommentDto;
import ru.practicum.explore.comments.dto.NewCommentDto;
import ru.practicum.explore.comments.model.Comment;
import ru.practicum.explore.events.model.Event;
import ru.practicum.explore.user.mapper.UserMapper;
import ru.practicum.explore.user.model.User;

public class CommentMapper {
    public static Comment toComment(NewCommentDto newCommentDto, User user, Event event) {
        Comment comment = new Comment();
        comment.setText(newCommentDto.getText());
        comment.setAuthor(user);
        comment.setEvent(event);
        return comment;
    }

    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthor(UserMapper.toUserShortDto(comment.getAuthor()));
        commentDto.setEvent(comment.getEvent().getId());
        commentDto.setCreated(comment.getCreated());
        commentDto.setEdited(comment.getEdited());
        return commentDto;
    }
}
