package task.notice.notice.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import task.notice.notice.domain.Notice;
import task.notice.notice.domain.QNotice;
import task.notice.user.domain.QUser;

import java.util.Objects;
import java.util.Optional;

import static task.notice.notice.domain.QNotice.notice;
import static task.notice.user.domain.QUser.user;


public class NoticeQueryRepositoryImpl implements NoticeQueryRepository {

    private final JPAQueryFactory query;

    public NoticeQueryRepositoryImpl(JPAQueryFactory query) {
        this.query = query;
    }

    @Override
    public Notice findNotice(Long noticeId) {
        return query.select(notice)
                .from(notice)
                .join(notice.user, user).fetchJoin()
                .where(noticeIdEquals(noticeId))
                .fetchFirst();
    }

    @Override
    public boolean existsByUserId(Long noticeId, Long userId) {
        Integer exist = query.selectOne()
                .from(notice)
                .where(noticeIdEquals(noticeId), userIdEquals(userId))
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
