package show.schedulemanagement.repository.board;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import show.schedulemanagement.domain.board.QBoard;
import show.schedulemanagement.domain.member.QMember;
import show.schedulemanagement.dto.board.BoardPageResponse;
import show.schedulemanagement.dto.board.BoardPageRequest;
import show.schedulemanagement.dto.board.SearchCond;
import show.schedulemanagement.dto.board.SortCondition;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{

    private final EntityManager em;

    private JPAQueryFactory query;
    private QBoard qBoard;
    private QMember qMember;

    public BoardRepositoryCustomImpl(EntityManager em) {
        this.em = em;
        init();
    }

    @Override
    public Page<BoardPageResponse> searchPage(BoardPageRequest cond, Pageable pageable) {
        String keyword = cond.getKeyword();
        SearchCond searchCond = cond.getSearchCond();

        List<BoardPageResponse> contents = query
                .select(Projections.constructor(BoardPageResponse.class,
                        qBoard.id,
                        qBoard.title,
                        qBoard.content,
                        qBoard.member.createdBy,
                        qBoard.member.nickname,
                        qBoard.createdAt,
                        qBoard.updatedAt,
                        qBoard.totalLike,
                        qBoard.totalScrap
                ))
                .from(qBoard)
                .leftJoin(qBoard.member, qMember)
                .where(containTitle(searchCond, keyword),
                        containContents(searchCond, keyword),
                        containWriter(searchCond, keyword)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sortConds(cond.getSortCond())).fetch();


        JPAQuery<Long> countQuery = query
                .select(qBoard.count())
                .from(qBoard)
                .leftJoin(qBoard.member, qMember)
                .where(containTitle(searchCond, keyword),
                        containContents(searchCond, keyword),
                        containWriter(searchCond, keyword));

        //정렬  -- paging 쿼리에서 바로 정렬 할 것인가 ? 결과를 받고 따로 app level 에서 정렬 할 것 인가 ?/
        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier<?>[] sortConds(SortCondition cond){
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        if (cond.equals(SortCondition.LIKE)) {
            orderSpecifiers.add(new OrderSpecifier<>(Order.DESC,qBoard.totalLike));
        } else if (cond.equals(SortCondition.SCRAP)) {
            orderSpecifiers.add(new OrderSpecifier<>(Order.DESC,qBoard.totalScrap));
        }
        orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, qBoard.createdAt));
        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }


    private BooleanExpression containTitle(SearchCond cond, String title){
        if(cond.equals(SearchCond.TITLE)){
            return qBoard.title.contains(title);
        }
        return null;
    }

    private BooleanExpression containContents(SearchCond cond, String contents){
        if(cond.equals(SearchCond.CONTENTS)){
            return qBoard.content.contains(contents);
        }
        return null;
    }

    private BooleanExpression containWriter(SearchCond cond, String writer){
        if(cond.equals(SearchCond.WRITER)){
            return qBoard.member.nickname.contains(writer);
        }
        return null;
    }

    private void init() {
        query = new JPAQueryFactory(em);
        qBoard = QBoard.board;
        qMember = QMember.member;
    }
}
