import PropTypes from "prop-types";

import {formatDateTime} from "@/components/home/form/ScheduleTimeUtils/TimeUtils.jsx";
import {useEffect, useState} from "react";
import api from "@/api/api.js";

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
            contents: value,
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
            const url = "/community/reply";
            const response = await api.put(url, replyEditForm);
            const data = response.data;

            setCurrentReply(()=>({...data}));
        } catch (error) {
            console.error("Error : ", error);
        }
    }

    const handleLikeClick = async () => {
        try {
            const url = `/community/reply/${currentReply.id}/heart`;
            hasLike ? await api.delete(url) : await api.post(url);

            setTotalLike(!hasLike ? (totalLike + 1) : (totalLike - 1));
            setHasLike(!hasLike);
        } catch (error) {
            console.error("Error : ", error);
        }
    }

    const handleDeleteReply = async () => {
        try {
            const url = `/community/reply/${reply.id}`;
            await api.delete(url);

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