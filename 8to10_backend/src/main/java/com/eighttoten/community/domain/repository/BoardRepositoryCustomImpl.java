package com.eighttoten.community.domain.repository;

import com.eighttoten.community.domain.QBoard;
import com.eighttoten.community.domain.QBoardHeart;
import com.eighttoten.community.domain.QBoardScrap;
import com.eighttoten.community.domain.QReply;
import com.eighttoten.community.domain.QReplyHeart;
import com.eighttoten.community.domain.Reply;
import com.eighttoten.community.domain.SearchCond;
import com.eighttoten.community.domain.SortCondition;
import com.eighttoten.community.dto.BoardPageRequest;
import com.eighttoten.community.dto.BoardPageResponse;
import com.eighttoten.community.dto.BoardSearchResponse;
import com.eighttoten.community.dto.ReplySearchResponse;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.QMember;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{
    private final EntityManager em;

    private JPAQueryFactory query;
    private QBoard qBoard;
    private QMember qMember;
    private QReply qReply;
    private QBoardHeart qBoardHeart;
    private QBoardScrap qBoardScrap;
    private QReplyHeart qReplyHeart;

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
                        qBoard.contents,
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

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<BoardSearchResponse> searchBoard(Long id, Member member) {
        List<Reply> boardReplies = query.select(qReply)
                .from(qReply)
                .leftJoin(qReply.member)
                .fetchJoin()
                .where(qReply.board.id.eq(id))
                .fetch();

        List<Long> likedReplyIds = query.select(qReplyHeart.reply.id)
                .from(qReplyHeart)
                .where(qReplyHeart.reply.in(boardReplies)
                        .and(qReplyHeart.member.id.eq(member.getId())))
                .fetch();

        BoardSearchResponse boardSearchResponse = query.select(
                        Projections.fields(BoardSearchResponse.class,
                                qBoard.id,
                                qBoard.title,
                                qBoard.contents,
                                qMember.nickname,
                                qMember.email.as("writer"),
                                qBoard.createdAt,
                                qBoard.updatedAt,
                                qBoard.totalLike,
                                qBoard.totalScrap,
                                hasLike(id, member).as("hasLike"),
                                hasScrap(id, member).as("hasScrap")
                        )
                ).from(qBoard, qBoard)
                .leftJoin(qBoard.member, qMember)
                .on(qBoard.member.id.eq(qMember.id))
                .where(qBoard.id.eq(id))
                .fetchOne();

        if (boardSearchResponse != null){
            boardSearchResponse.setLikedReplyIds(likedReplyIds);
            boardSearchResponse.setReplies(boardReplies.stream().map(ReplySearchResponse::from).toList());
        }

        return Optional.ofNullable(boardSearchResponse);
    }

    private BooleanExpression hasScrap(Long id, Member member) {
        return JPAExpressions.selectOne()
                .from(qBoardScrap)
                .where(qBoardScrap.board.id.eq(id)
                        .and(qBoardScrap.member.id.eq(member.getId()))
                ).exists();
    }

    private BooleanExpression hasLike(Long id, Member member) {
        return JPAExpressions.selectOne()
                .from(qBoardHeart)
                .where(qBoardHeart.board.id.eq(id)
                        .and(qBoardHeart.member.id.eq(member.getId()))
                ).exists();
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
            return qBoard.contents.contains(contents);
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
        qReply = QReply.reply;
        qBoardHeart = QBoardHeart.boardHeart;
        qBoardScrap = QBoardScrap.boardScrap;
        qReplyHeart = QReplyHeart.replyHeart;
    }
}