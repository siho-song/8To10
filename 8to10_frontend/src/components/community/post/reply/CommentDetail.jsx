import {useEffect, useState} from "react";
import PropTypes from "prop-types";

import "@/styles/community/Board.css";

import ReplyDetail from "@/components/community/post/reply/ReplyDetail.jsx";
import {formatDateTime} from "@/components/home/form/ScheduleTimeUtils/TimeUtils.jsx";

function CommentDetail({ postId, email, reply, replies, likedReplyIds, onReplySubmit, onCommentDelete, onReplyDelete }) {

    const [comment, setComment] = useState(reply);

    const [replyForm, setReplyForm] = useState({
        boardId: postId,
        parentId: reply.id,
        contents: "",
    });

    const [commentEditForm, setCommentEditForm] = useState({
        id: reply.id,
        contents: reply.contents,
    });

    const [showReplyInput, setShowReplyInput] = useState(false);
    const childReplies = replies.filter(r => r.parentId === reply.id);

    const [hasLike, setHasLike] = useState(likedReplyIds.includes(reply.id));
    const [totalLike, setTotalLike] = useState(reply.totalLike);

    const [isCommentEditMode, setIsCommentEditMode] = useState(false);


    const handleReplyInput = (e) => {
        const value = e.target.value;
        setReplyForm({
            ...replyForm,
            contents: value,
        });
    };

    const resetReplyInput = () => {
        setReplyForm({
            ...replyForm,
            contents: '',
        });
    }

    const handleReplySubmit = async() => {
        try {
            console.log("reply Form : ", replyForm);
            const response = await fetch("/api/community/reply/add", {
                method: "POST",
                credentials: "include",
                headers: {
                    'Content-Type' : 'application/json'
                },
                body: JSON.stringify(replyForm),
            });

            if (!response.ok) {
                throw new Error('대댓글의 작성에 실패했습니다');
            }

            const data = await response.json();

            const newComment = {
                id: data.replyId,
                content: data.contents,
                createdAt: data.createdAt,
                updatedAt: data.updatedAt,
                nickname: data.nickname,
                writer: data.writer,
                parentId: data.parentId,
                totalLike: 0,
            }

            onReplySubmit(newComment);
            resetReplyInput();
            setShowReplyInput(false);
        } catch (error) {
            console.log("Error : ", error);
        }
    }

    const handleReplyLikeSubmit = async () => {
        try {
            const response = await fetch(`/api/community/reply/${reply.id}/heart`, {
                method: hasLike ? "DELETE" : "POST",
                credentials: "include",
            })

            if (!response.ok) {
                throw new Error(hasLike ? "좋아요 취소에 실패했습니다." : "좋아요에 실패했습니다.");
            }

            setTotalLike(!hasLike ? (totalLike + 1) : (totalLike - 1));
            setHasLike(!hasLike);
        } catch (error) {
            console.error("Error : ", error);
        }
    }

    const handleCommentEditInput = (e) => {
        const value = e.target.value;
        setCommentEditForm((prevData) => ({
            ...prevData,
            content: value,
        }))
    }

    const resetCommentEditInput = () => {
        setCommentEditForm((prevData) => ({
            ...prevData,
            content: comment.contents,
        }))
    }

    const handleCommentEditSubmit = async () => {
        try {
            console.log(commentEditForm);
            const response = await fetch("/api/community/reply", {
                method: "PUT",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(commentEditForm),
            })

            const data = await response.json();
            setComment(()=>({...data}));
        } catch (error) {
            console.error("Error : ", error);
        }
    }

    const handleDeleteComment = async () => {
        try {
            const response = await fetch(`/api/community/reply/${reply.id}`, {
                method: "DELETE",
            });

            if (!response.ok) {
                throw new Error("댓글을 삭제할 수 없습니다.");
            }

            onCommentDelete(reply.id);
        } catch (error) {
            console.log("Error : ", error);
        }
    }

    useEffect(() => {
    }, [showReplyInput, comment]);

    return (
        <div className="comment" id={`comment-${comment.id}`}>
            { !isCommentEditMode ? (
                <div className="comment-header">
                    <div className="comment-profile">
                        <span className="comment-author">{comment.nickname}</span>
                        {email === comment.writer &&
                            <div id="edit-delete-controls" className="edit-delete-controls">
                                <button
                                    className="comment-edit-button"
                                    onClick={() => {
                                        setIsCommentEditMode(true);
                                    }}
                                >수정
                                </button>
                                <button
                                    className="comment-delete-button"
                                    onClick={handleDeleteComment}
                                >삭제</button>
                            </div>
                        }
                    </div>
                    <span className="post-date">{formatDateTime(comment.createdAt)}</span>
                    <span className="comment-text">{comment.contents}</span>

                    <div className="comment-actions">
                        <button className="reply-button" onClick={() => {
                            setShowReplyInput(!showReplyInput);
                        }}>
                            대댓글 달기
                        </button>
                        <button className={hasLike ? "comment-like-button active" : "comment-like-button"}
                                data-liked={hasLike}
                                onClick={handleReplyLikeSubmit}
                        >
                            <span className="replyHeart">좋아요 {totalLike}</span>
                        </button>
                    </div>
                </div>

            ) : (
                <div>
                    <form className="create-comment" onSubmit={(e) => {
                        e.preventDefault();
                        handleCommentEditSubmit();
                        setIsCommentEditMode(false);
                    }}>
                        <input
                            type="text"
                            id="comment-input"
                            placeholder="댓글을 입력하세요."
                            name="contents"
                            value={commentEditForm.contents}
                            onChange={handleCommentEditInput}
                        />
                        <button type="submit" id="submit-comment">댓글 수정</button>
                        <button className="hide-reply-section" onClick={() => {
                            setIsCommentEditMode(false);
                            resetCommentEditInput();
                        }}>
                            취소
                        </button>
                    </form>
                </div>
            )}

            {showReplyInput && (
                <form
                    className="reply-section"
                    onSubmit={(e) => {
                        e.preventDefault();
                        handleReplySubmit();
                    }}
                >
                    <input
                        type="text"
                        className="reply-input"
                        placeholder="덧글을 입력하세요"
                        name="contents"
                        value={replyForm.contents}
                        onChange={handleReplyInput}
                    />
                    <button type="submit" className="reply-submit">덧글 등록</button>
                    <button className="hide-reply-section" onClick={() => {
                        setShowReplyInput(false)
                        resetReplyInput();
                    }}>취소
                    </button>
                </form>
            )}

            <div className="replies-container">
                {childReplies.map(childReply => (
                    <ReplyDetail
                        key={childReply.id}
                        email={email}
                        reply={childReply}
                        likedReplyIds={likedReplyIds}
                        onReplyDelete={onReplyDelete}
                    />
                ))}
            </div>
        </div>
    );
}


CommentDetail.propTypes = {
    postId: PropTypes.number.isRequired,
    email: PropTypes.string.isRequired,
    reply: PropTypes.shape({
        id: PropTypes.number,
        contents: PropTypes.string.isRequired,
        createdAt: PropTypes.string.isRequired,
        updatedAt: PropTypes.string.isRequired,
        nickname: PropTypes.string.isRequired,
        writer: PropTypes.string.isRequired,
        parentId: PropTypes.number,
        totalLike: PropTypes.number.isRequired,
    }).isRequired,
    replies: PropTypes.arrayOf(
        PropTypes.shape({
            id: PropTypes.number.isRequired,
            contents: PropTypes.string.isRequired,
            createdAt: PropTypes.string.isRequired,
            updatedAt: PropTypes.string.isRequired,
            nickname: PropTypes.string.isRequired,
            writer: PropTypes.string.isRequired,
            parentId: PropTypes.number,
            totalLike: PropTypes.number.isRequired,
        }).isRequired,
    ).isRequired,
    likedReplyIds: PropTypes.arrayOf(PropTypes.number),
    onReplySubmit: PropTypes.func.isRequired,
    onCommentDelete: PropTypes.func.isRequired,
    onReplyDelete: PropTypes.func.isRequired,
}

export default CommentDetail;