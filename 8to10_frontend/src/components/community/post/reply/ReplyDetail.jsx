import PropTypes from "prop-types";

import {formatDateTime} from "@/components/home/form/ScheduleTimeUtils/TimeUtils.jsx";
import {useEffect, useState} from "react";

function ReplyDetail({ email, reply, likedReplyIds, onReplyDelete }) {

    const [currentReply, setCurrentReply] = useState(reply);

    const [replyEditForm, setReplyEditForm] = useState({
        id: reply.id,
        contents: reply.contents,
    })

    const [hasLike, setHasLike] = useState(likedReplyIds.includes(reply.id));
    const [totalLike, setTotalLike] = useState(reply.totalLike);

    const [isReplyEditMode, setIsReplyEditMode] = useState(false);

    const handleReplyEditInput = (e) => {
        const value = e.target.value;
        setReplyEditForm((prevData) => ({
            ...prevData,
            content: value,
        }))
    }

    const resetReplyEditInput = () => {
        setReplyEditForm((prevData) => ({
            ...prevData,
            contents: currentReply.contents,
        }))
    }

    const handleReplyEditSubmit = async () => {
        try {
            const accessToken = localStorage.getItem('authorization');
            const response = await fetch("/api/community/reply", {
                method: "PUT",
                headers: {
                    'Content-Type': 'application/json',
                    'authorization': `Bearer ${accessToken}`,
                },
                body: JSON.stringify(replyEditForm),
            })

            const data = await response.json();
            setCurrentReply(()=>({...data}));
        } catch (error) {
            console.error("Error : ", error);
        }
    }

    const handleLikeClick = async () => {
        try {
            const accessToken = localStorage.getItem('authorization');
            const response = await fetch(`/api/community/reply/${currentReply.id}/heart`, {
                method: hasLike ? "DELETE" : "POST",
                headers: {
                    'authorization': `Bearer ${accessToken}`,
                },
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

    const handleDeleteReply = async () => {
        try {
            const accessToken = localStorage.getItem('authorization');
            const response = await fetch(`/api/community/reply/${reply.id}`, {
                method: "DELETE",
                headers: {
                    'authorization': `Bearer ${accessToken}`,
                },
            });

            if (!response.ok) {
                throw new Error("대댓글을 삭제할 수 없습니다.");
            }

            onReplyDelete(reply.id);
        } catch (error) {
            console.error("Error : ", error);
        }
    };

    useEffect(() => {

    }, [currentReply]);

    return (
        <div className="reply">
            {!isReplyEditMode ? (
                <div className="reply-group">
                    <div className="reply-header">
                        <span className="comment-author">{currentReply.nickname}</span>
                        {email === currentReply.writer &&
                            <div className="reply-actions">
                                <button
                                    className="comment-edit-button"
                                    onClick={() => {
                                        setIsReplyEditMode(true)
                                    }}
                                >수정
                                </button>
                                <button
                                    className="comment-delete-button"
                                    onClick={handleDeleteReply}
                                >삭제</button>
                            </div>
                        }
                    </div>
                    <span className="post-date">{formatDateTime(currentReply.createdAt)}</span>
                    <span className="comment-text">{currentReply.contents}</span>

                    <div className="comment-actions">
                        <button className={hasLike ? "comment-like-button active" : "comment-like-button"}
                                data-liked={hasLike}
                                onClick={handleLikeClick}
                        >
                            <span className="heart">좋아요 {totalLike}</span>
                        </button>
                    </div>
                </div>
            ) : (
                <form
                    className="reply-section"
                    onSubmit={(e) => {
                        e.preventDefault();
                        handleReplyEditSubmit();
                        setIsReplyEditMode(false);
                    }}
                >
                    <input
                        type="text"
                        className="reply-input"
                        placeholder="덧글을 입력하세요"
                        name="contents"
                        value={replyEditForm.contents}
                        onChange={handleReplyEditInput}
                    />
                    <button type="submit" className="reply-submit">덧글 등록</button>
                    <button className="hide-reply-section" onClick={() => {
                        setIsReplyEditMode(false)
                        resetReplyEditInput();
                    }}>취소
                    </button>
                </form>
            )}

        </div>
    );
}

ReplyDetail.propTypes = {
    email: PropTypes.string.isRequired,
    reply: PropTypes.shape({
        id: PropTypes.number.isRequired,
        contents: PropTypes.string.isRequired,
        createdAt: PropTypes.string.isRequired,
        updatedAt: PropTypes.string.isRequired,
        nickname: PropTypes.string.isRequired,
        writer: PropTypes.string.isRequired,
        parentId: PropTypes.number,
        totalLike: PropTypes.number.isRequired,
    }).isRequired,
    likedReplyIds: PropTypes.arrayOf(PropTypes.number).isRequired,
    onReplyDelete: PropTypes.func.isRequired,
}

export default ReplyDetail;