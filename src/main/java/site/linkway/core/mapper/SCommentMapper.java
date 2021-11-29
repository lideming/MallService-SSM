package site.linkway.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import site.linkway.core.entity.po.SComment;

//table name:sComment  entity:po.SComment
@Mapper
public interface SCommentMapper {
    int insert(SComment sComment);
    int update(SComment sComment);
    int delete(SComment sComment);
    SComment select(SComment sComment);
}
