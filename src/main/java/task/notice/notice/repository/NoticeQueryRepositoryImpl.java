package task.notice.notice.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import task.notice.notice.domain.Notice;
import task.notice.notice.dto.response.NoticeResponseDto;
import task.notice.notice.dto.response.QNoticeResponseDto;

import java.util.List;
import java.util.Objects;

import static task.notice.attachfile.domain.QAttachFile.attachFile;
import static task.notice.notice.domain.QNotice.notice;
import static task.notice.user.domain.QUser.user;


public class NoticeQueryRepositoryImpl implements NoticeQueryRepository {

    private final JPAQueryFactory query;

    public NoticeQueryRepositoryImpl(JPAQueryFactory query) {
        this.query = query;
    }

    @Override
    public Notice findNotice(Long noticeId) {
        return query.select(notice).distinct()
                .from(notice)
                .join(notice.user, user).fetchJoin()
                .leftJoin(notice.attachFiles, attachFile).fetchJoin()
                .where(noticeIdEquals(noticeId).and(attachFile.deleted.isFalse()))
                .fetchFirst();
    }

    @Override
    public Page<NoticeResponseDto> findNoticeList(Pageable pageable) {
        JPAQuery<NoticeResponseDto> jpaQuery = query.select(
                        new QNoticeResponseDto(
                                notice.id, notice.title, notice.content, notice.endDate, notice.viewCount, user.username)
                )
                .from(notice)
                .join(notice.user, user)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<NoticeResponseDto> content = query.select(
                    new QNoticeResponseDto(
                            notice.id, notice.title, notice.content, notice.endDate, notice.viewCount, user.username)
                )
                .from(notice)
                .join(notice.user, user)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, jpaQuery::fetchCount);
    }

    @Override
    public Notice findNoticeAndAttachFiles(Long noticeId) {
        return query.select(notice).distinct()
                .from(notice)
                .join(notice.attachFiles, attachFile).fetchJoin()
                .where(noticeIdEquals(noticeId).and(attachFile.deleted.isFalse()))
                .fetchFirst();
    }

    @Override
    public boolean existsByUserId(Long noticeId, Long userId) {
        Integer exist = query.selectOne()
                .from(notice)
                .where(noticeIdEquals(noticeId).and(userIdEquals(userId)))
                .fetchFirst();

        return exist != null;
    }

    public BooleanExpression noticeIdEquals(Long noticeId) {
        return !Objects.isNull(noticeId) ? notice.id.eq(noticeId) : null;
    }

    public BooleanExpression userIdEquals(Long userId) {
        return !Objects.isNull(userId) ? notice.user.id.eq(userId) : null;
    }
}
