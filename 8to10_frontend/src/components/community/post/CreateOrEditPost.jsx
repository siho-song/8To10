import {useEffect, useState} from 'react';
import {useNavigate, useParams} from "react-router-dom";

import PropTypes from "prop-types";
import api from "@/api/api.js";

function CreateOrEditPost({ isEditMode }) {
    const [title, setTitle] = useState('');
    const [contents, setContents] = useState('');

    const navigate = useNavigate()
    const id = useParams();

    const handleSubmit = async (e) => {
        e.preventDefault();

        const postData = {
            title: title,
            contents: contents,
        };

        const updatePostData = {
            id: id.postId,
            title: title,
            contents: contents,
        }

        if (!title || !contents) {
            alert('제목과 내용을 입력해주세요');
            return;
        }

        try {
            const url = isEditMode ? `/community/board` : `/community/board/add`;
            const response = isEditMode ? await api.put(url, updatePostData) : await api.post(url, postData);
            const data = response.data;

            if (isEditMode) {
                navigate(`/community/board/${data.id}`);
            } else {
                const newPost = {
                    ...data,
                    hasLike: false,
                    hasScrap: false,
                }
                navigate(`/community/board/${data.id}`, { state: newPost });
            }
        } catch (error) {
            console.log("글 등록에 실패했습니다.");
            console.error("Error : ", error);
        }

    };

    useEffect(() => {
        if (isEditMode && id) {
            const loadPostData = async () => {

                try {
                    const accessToken = localStorage.getItem('authorization');
                    const response = await fetch(`/api/community/board/${id.postId}`, {
                        method: 'GET',
                        headers: {
                            'authorization': `Bearer ${accessToken}`,
                        }
                    });

                    if (!response.ok) {
                        throw new Error('서버와의 통신에 실패했습니다.');
                    }

                    const boardData = await response.json();

                    console.log("boardData : ", boardData);

                    setTitle(boardData.title);
                    setContents(boardData.contents);
                } catch (error) {
                    console.error("Error : ", error);
                }
            }

            loadPostData();
        }
    }, []);

    return (
        <div className="create-post-container">
            <div className="create-post-header">
                <h2>{isEditMode ? "게시글 수정" : "게시글 작성"}</h2>
                <button
                    className="view-list-btn"
                    onClick={() => navigate("/community/board")}>
                    목록보기
                </button>
            </div>
            <hr/>
            <form className="post-form" onSubmit={handleSubmit}>
                <div className="post-form-group">
                    <label htmlFor="post-title">글 제목</label>
                    <input
                        type="text"
                        placeholder="글 제목을 입력하세요"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                    />
                    <span className="error-message" id="title-error"></span>
                </div>

                <div className="post-form-group">
                    <label htmlFor="post-content">글 내용</label>
                    <textarea
                        rows="10"
                        placeholder="내용을 입력하세요"
                        value={contents}
                        onChange={(e) => setContents(e.target.value)}
                    />
                    <span className="post-error-message" id="content-error"></span>
                </div>

                <button className="post-submit-btn" type="submit">등록</button>
            </form>
        </div>
    );
}


CreateOrEditPost.propTypes = {
    isEditMode: PropTypes.bool,
}

export default CreateOrEditPost;
