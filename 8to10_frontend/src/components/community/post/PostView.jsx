import {useEffect, useState} from 'react';
import {useLocation, useParams} from 'react-router-dom';
import PostItem from "@/components/community/post/PostItem.jsx";
import CreateComment from "@/components/community/post/reply/CreateComment.jsx";
import CommentSection from "@/components/community/post/reply/CommentSection.jsx";
import {useAuth} from "@/context/auth/UseAuth.jsx";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";

const PostView = () => {
    const location = useLocation();

    const { email } = useAuth();
    const [post, setPost] = useState(location.state?.post || null);
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
                const response = await authenticatedApi.get(
                    url,
                    {
                        apiEndPoint: API_ENDPOINT_NAMES.GET_POST,
                });
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
    },[]);

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
                <PostItem
                    post={post}
                    email={email}
                />
                <CreateComment
                    id={post.id}
                    onCommentSubmit={addNewComment}
                />
                <CommentSection
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

export default PostView;
