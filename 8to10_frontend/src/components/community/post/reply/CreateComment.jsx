import {useState} from "react";
import PropTypes from "prop-types";
import api from "@/api/api.js";

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
            const url = "/community/reply/add";
            const response = await api.post(url, bodyData);
            const data = response.data;

            const newComment = {
                id: data.replyId,
                contents: data.contents,
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