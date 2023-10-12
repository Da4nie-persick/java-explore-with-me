package ru.practicum.explore.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.comments.CommentRepository;
import ru.practicum.explore.comments.dto.CommentDto;
import ru.practicum.explore.comments.dto.NewCommentDto;
import ru.practicum.explore.comments.dto.UpdateComment;
import ru.practicum.explore.comments.mapper.CommentMapper;
import ru.practicum.explore.comments.model.Comment;
import ru.practicum.explore.events.EventRepository;
import ru.practicum.explore.events.model.Event;
import ru.practicum.explore.events.model.State;
import ru.practicum.explore.exception.ConditionsNotConflictException;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.user.UserRepository;
import ru.practicum.explore.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    /*
    private
     */
    @Transactional
    @Override
    public CommentDto create(Integer userId, Integer eventId, NewCommentDto newCommentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User with id= " + userId + " was not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event with id= " + eventId + " was not found"));
        if (event.getState().equals(State.PENDING)) {
            throw new ConditionsNotConflictException("The event with id= " + eventId + " has not been published yet, you can't leave a comment");
        }
        Comment comment = CommentMapper.toComment(newCommentDto, user, event);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public CommentDto updateComment(Integer userId,  UpdateComment updateComment) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("User with id= " + userId + " was not found");
        }
        Comment comment = commentRepository.findById(updateComment.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Comment with id= " + updateComment.getId() + " was not found"));
        if (comment.getAuthor().getId() != userId) {
            throw new ConditionsNotConflictException("Only the author can edit the comment");
        }
        comment.setText(updateComment.getText());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public void deleteComment(Integer userId, Integer comId) {
        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() -> new ObjectNotFoundException("Comment with id= " + comId + " was not found"));
        if (comment.getAuthor().getId() != userId) {
            throw new ConditionsNotConflictException("Only the author can delete the comment");
        }
        commentRepository.deleteById(comId);
    }

    @Override
    public List<CommentDto> getAllCommentsByAuthorAndEvent(Integer userId, Integer eventId, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("User with id= " + userId + " was not found");
        }
        Pageable pageable = PageRequest.of(from, size);
        List<Comment> commentList;
        if (eventId == null) {
            commentList = commentRepository.findAllByAuthorIdOrderByCreated(userId, pageable);
        } else {
            commentList = commentRepository.findAllByAuthorIdAndEventIdOrderByCreated(userId, eventId, pageable);
        }
        return commentList.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    /*
    admin
     */
    @Transactional
    @Override
    public CommentDto updateCommentAdmin(UpdateComment updateComment) {
        Comment comment = commentRepository.findById(updateComment.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Comment with id= " + updateComment.getId() + " was not found"));
        comment.setText(updateComment.getText());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public void deleteCommentAdmin(Integer comId) {
        if (!commentRepository.existsById(comId)) {
            throw new ObjectNotFoundException("Comment with id= " + comId + " was not found");
        }
        commentRepository.deleteById(comId);
    }

    @Override
    public List<CommentDto> searchCommentAdmin(String text, Integer from, Integer size) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        Pageable pageable = PageRequest.of(from, size);
        List<Comment> commentList = commentRepository.searchComment(text, pageable);
        return commentList.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    /*
    public
     */
    @Override
    public CommentDto getCommentId(Integer comId) {
        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() -> new ObjectNotFoundException("Comment with id= " + comId + " was not found"));
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public List<CommentDto> getAllCommentsEvent(Integer eventId, Integer from, Integer size) {
        if (!eventRepository.existsById(eventId)) {
            throw new ObjectNotFoundException("Event with id= " + eventId + " was not found");
        }
        Pageable pageable = PageRequest.of(from, size);
        List<Comment> commentList = commentRepository.findAllByEventIdOrderByCreated(eventId, pageable);
        return commentList.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }
}
