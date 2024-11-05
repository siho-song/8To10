import {useNavigate} from "react-router-dom";
import PropTypes from "prop-types";

import {formatDateTime} from "@/components/home/form/ScheduleTimeUtils/TimeUtils.jsx";
import {useState} from "react";

function PostContent({ post, email }) {

    const navigate = useNavigate();

    const [hasLike, setHasLike] = useState(post.hasLike);
    const [totalLikes, setTotalLikes] = useState(post.totalLike);
    const [hasScrap, setHasScrap] = useState(post.hasScrap);
    const [totalScraps, setTotalScraps] = useState(post.totalScrap);

    const handleLikeClick = async () => {
        try {
            const response = await fetch(`/api/community/board/${post.id}/heart`, {
                method: hasLike ? "DELETE" : "POST",
                credentials: "include",
            })

            if (!response.ok) {
                throw new Error(hasLike ? "좋아요 취소에 실패했습니다." : "좋아요에 실패했습니다.");
            }

            setTotalLikes(!hasLike ? (totalLikes + 1) : (totalLikes - 1));
            setHasLike(!hasLike);
        } catch (error) {
            console.error("Error : ", error);
        }
    }

    const handleScrapClick = async () => {
        try {
            console.log(`method is ${hasScrap ? "DELETE" : "POST"}`);
            const response = await fetch(`/api/community/board/${post.id}/scrap`, {
                method: hasScrap ? "DELETE" : "POST",
                credentials: "include",
            })

            if (!response.ok) {
                throw new Error(hasScrap ? "스크랩 취소에 실패했습니다." : "스크랩에 실패했습니다.");
            }

            setTotalScraps(!hasScrap ? (totalScraps + 1) : (totalScraps - 1));
            setHasScrap(!hasScrap);
        } catch (error) {
            console.error("Error : ", error);
        }
    }

    const handleDeletePost = async () => {
        try {
            const response = await fetch(`/api/community/board/${post.id}`, {
                method: "DELETE",
                credentials: "include",
            });

            if (!response.ok) {
                throw new Error("게시글을 삭제할 수 없습니다");
            }

            navigate("/community/board");
        } catch (error) {
            console.error("Error : ", error);
        }
    }

    return (
        <div className="post-content">
            <div className="post-detail">

                <div className="post-header-button">
                    {email === post.writer &&
                        <div id="edit-delete-controls" className="edit-delete-controls">
                            <button onClick={() => {
                                navigate(`/community/board/edit/${post.id}`)
                            }}>수정
                            </button>
                            <button
                                className="delete-button"
                                onClick={handleDeletePost}
                            >삭제</button>
                        </div>
                    }
                    <button onClick={() => {
                        navigate("/community/board")
                    }}>글 목록
                    </button>
                </div>

                <div className="post-header">
                    <h3 className="post-title">{post.title}</h3>
                </div>

                <div className="post-info">
                    <div className="post-author-date">
                        <span className="post-author">{post.nickname}</span>
                        <span className="post-date">{formatDateTime(post.createdAt)}</span>
                    </div>
                </div>

                <hr/>

                <p className="post-body">{post.contents}</p>

                <div className="post-stats">
                    <div className="post-likes">
                        <button
                            id="like-button"
                            className={hasLike ? "like-button active" : "like-button"}
                            data-liked={hasLike}
                            onClick={handleLikeClick}>
                            <span className="heart">좋아요 {totalLikes}</span>
                        </button>
                    </div>
                    <div className="post-scraps">
                        <button
                            id="scrap-button"
                            className={hasScrap ? "scrap-button active" : "scrap-button"}
                            data-scraped={hasScrap}
                            onClick={handleScrapClick}>

                            <span className="scrap">스크랩 {totalScraps}</span>
                            {/*<span id="scrap-count">{totalScraps}</span>*/}
                        </button>
                    </div>
                </div>
            </div>
        </div>


    );
}

PostContent.propTypes = {
    post: PropTypes.shape({
        id: PropTypes.number.isRequired,
        title: PropTypes.string.isRequired,
        contents: PropTypes.string.isRequired,
        createdAt: PropTypes.string.isRequired,
        updatedAt: PropTypes.string.isRequired,
        writer: PropTypes.string.isRequired,
        nickname: PropTypes.string.isRequired,
        totalLike: PropTypes.number.isRequired,
        totalScrap: PropTypes.number.isRequired,
        hasLike: PropTypes.bool.isRequired,
        hasScrap: PropTypes.bool.isRequired,
    }).isRequired,
    email: PropTypes.string.isRequired,
}

export default PostContent;