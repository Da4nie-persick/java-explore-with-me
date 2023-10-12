package ru.practicum.explore.comments;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.comments.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query(" select c from Comment c " +
            "where upper(c.text) like upper(concat('%', ?1, '%')) ")
    List<Comment> searchComment(String text, Pageable pageable);
    List<Comment> findAllByEventIdOrderByCreated(Integer eventId, Pageable pageable);
    List<Comment> findAllByAuthorIdOrderByCreated(Integer authorId, Pageable pageable);
    List<Comment> findAllByAuthorIdAndEventIdOrderByCreated(Integer authorId, Integer eventId, Pageable pageable);
}
