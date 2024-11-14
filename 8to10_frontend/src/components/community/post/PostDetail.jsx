import {useEffect, useState} from 'react';
import {useLocation, useParams} from 'react-router-dom';
import PostContent from "@/components/community/post/PostContent.jsx";
import CreateComment from "@/components/community/post/reply/CreateComment.jsx";
import CommentView from "@/components/community/post/reply/CommentView.jsx";
import {useAuth} from "@/context/UseAuth.jsx";
import api from "@/api/api.js";

const PostDetail = () => {
    const location = useLocation();

    const { email } = useAuth();
    const [post, setPost] = useState(location.state || null);
    const [responseReplies, setResponseReplies] = useState([]);
    const [responseLikedReplyIds, setResponseLikedReplyIds] = useState([]);


    const { id } = useParams();

    const addNewComment = (newComment) => {
        setResponseReplies((prevData) => [...prevData, newComment]);
    }

    useEffect(() => {
        const loadPostData = async () => {
            try {
                const url = `/community/board/${id}`;
                const response = await api.get(url);
                const data = response.data;

                const { replies, likedReplyIds, ...restOfPost } = data;

                setPost(restOfPost);
                setResponseReplies(replies);
                setResponseLikedReplyIds(likedReplyIds);
            } catch (error) {
                console.error("Error : \n", error.toString());
                console.error(error);
            }
        }
        if (!post) {
            loadPostData();
        }
    },[post]);

    if (!post) {
        return ("게시물을 불러오는 중입니다.");
    }

    const deleteReplyById = (replyId) => {
        setResponseReplies((prevReplies) =>
            prevReplies.filter((reply) => reply.id !== replyId)
        );
    };

    const deleteCommentById = (replyId) => {
        setResponseReplies((prevComments) =>
            prevComments.filter((reply) => reply.id !== replyId && reply.parentId !== replyId)
        );
    };

    return (
        <div className="post-container">
            <div className="post-main-content">
                <PostContent
                    post={post}
                    email={email}
                />
                <CreateComment
                    id={post.id}
                    onCommentSubmit={addNewComment}
                />
                <CommentView
                    postId={post.id}
                    email={email}
                    replies={responseReplies}
                    likedReplyIds={responseLikedReplyIds}
                    onReplySubmit={addNewComment}
                    onCommentDelete={deleteCommentById}
                    onReplyDelete={deleteReplyById}
                />
            </div>

        </div>
    );
};

export default PostDetail;
