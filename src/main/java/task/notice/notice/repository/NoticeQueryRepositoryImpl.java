package task.notice.notice.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import task.notice.notice.domain.Notice;

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
                .join(notice.attachFiles, attachFile).fetchJoin()
                .where(noticeIdEquals(noticeId).and(attachFile.deleted.isTrue()))
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
