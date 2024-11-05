import {useState} from "react";
import PropTypes from "prop-types";

function CreateComment({ id, onCommentSubmit }) {

    const [bodyData, setBodyData] = useState({
        boardId : id,
        parentId : null,
        contents : '',
    });

    const handleContentChange = (e) => {
        const value = e.target.value;
        setBodyData({
            ...bodyData,
            contents: value,
        });
    }

    const handleCommentSubmit = async () => {
        try {
            const response = await fetch("/api/community/reply/add", {
                method: "POST",
                credentials: "include",
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(bodyData),
            });

            if (!response.ok) {
                throw new Error('서버와의 통신에 실패했습니다.');
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

            onCommentSubmit(newComment);
            setBodyData({...bodyData, contents: ''});

        }
        catch (error) {
            console.error("Error : ", error);
        }
    };


    return (
        <div>
            <form className="create-comment" onSubmit={(e) => {
                e.preventDefault();
                handleCommentSubmit();
            }}>
                <input
                    type="text"
                    id="comment-input"
                    placeholder="댓글을 입력하세요."
                    name="contents"
                    value={bodyData.contents}
                    onChange={handleContentChange}
                />
                <button type="submit" id="submit-comment">댓글 등록</button>
            </form>
        </div>
    );
}

CreateComment.propTypes = {
    id: PropTypes.number.isRequired,
    onCommentSubmit: PropTypes.func.isRequired,
}

export default CreateComment;