import {useNavigate} from "react-router-dom";
import PropTypes from "prop-types";

import {formatDateTime} from "@/components/home/form/ScheduleTimeUtils/TimeUtils.jsx";
import {useState} from "react";
import api from "@/api/api.js";

function PostContent({ post, email }) {

    const navigate = useNavigate();

    const [hasLike, setHasLike] = useState(post.hasLike);
    const [totalLikes, setTotalLikes] = useState(post.totalLike);
    const [hasScrap, setHasScrap] = useState(post.hasScrap);
    const [totalScraps, setTotalScraps] = useState(post.totalScrap);

    const handleLikeClick = async () => {
        try {
            const url = `/community/board/${post.id}/heart`;
            hasLike ? await api.delete(url) : await api.post(url);

            setTotalLikes(!hasLike ? (totalLikes + 1) : (totalLikes - 1));
            setHasLike(!hasLike);
        } catch (error) {
            console.error("Error : ", error);
        }
    }

    const handleScrapClick = async () => {
        try {
            const url = `/community/board/${post.id}/scrap`;
            hasScrap ? await api.delete(url) : await api.post(url);

            setTotalScraps(!hasScrap ? (totalScraps + 1) : (totalScraps - 1));
            setHasScrap(!hasScrap);
        } catch (error) {
            console.error("Error : ", error);
        }
    }

    const handlePostDelete = async () => {
        try {
            const url = `/community/board/${post.id}`;
            await api.delete(url);

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
                                onClick={handlePostDelete}
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