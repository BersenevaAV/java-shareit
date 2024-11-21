package ru.practicum.shareit.item.comment;

import lombok.experimental.UtilityClass;
import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment, LocalDateTime dateTimeComment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem(),
                comment.getAuthor().getName(),
                dateTimeComment
        );
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem(),
                comment.getAuthor().getName(),
                null
        );
    }
}
