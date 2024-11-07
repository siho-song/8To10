import {useEffect, useState} from 'react';
import {useLocation, useParams} from 'react-router-dom';
import PostContent from "@/components/community/post/PostContent.jsx";
import CreateComment from "@/components/community/post/reply/CreateComment.jsx";
import CommentView from "@/components/community/post/reply/CommentView.jsx";
import LeftSideBar from "@/components/community/LeftSideBar.jsx";
import {useAuth} from "@/components/context/UseAuth.jsx";

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
                const accessToken = localStorage.getItem('authorization');
                const response = await fetch(`/api/community/board/${id}`, {
                    method: 'GET',
                    headers: {
                        'authorization': `Bearer ${accessToken}`,
                    },
                });

                if (!response.ok) {
                    throw new Error('서버와의 통신에 실패했습니다.');
                }

                const data = await response.json();

                const { replies, likedReplyIds, ...restOfPost } = data;

                console.log("restOfPosts :", restOfPost);

                setPost(restOfPost);
                setResponseReplies(replies);
                setResponseLikedReplyIds(likedReplyIds);
            } catch (error) {
                console.error('Error:', error);
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
