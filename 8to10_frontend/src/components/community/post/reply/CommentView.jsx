import PropTypes from "prop-types";
import CommentDetail from "@/components/community/post/reply/CommentDetail.jsx";

function CommentView({ postId, email, replies, likedReplyIds, onReplySubmit, onCommentDelete, onReplyDelete }) {

    return (
        <div id="comment-container">
            {replies.filter(reply => !reply.parentId)
                    .map(reply => (
                        <CommentDetail
                            key={reply.id}
                            email={email}
                            postId={postId}
                            reply={reply}
                            replies={replies}
                            likedReplyIds={likedReplyIds}
                            onReplySubmit={onReplySubmit}
                            onCommentDelete={onCommentDelete}
                            onReplyDelete={onReplyDelete}
                        />
                    ))}
        </div>
    );
}


CommentView.propTypes = {
    postId: PropTypes.number.isRequired,
    email: PropTypes.string.isRequired,
    replies: PropTypes.arrayOf(
        PropTypes.shape({
            id: PropTypes.number,
            contents: PropTypes.string,
            createdAt: PropTypes.string,
            updatedAt: PropTypes.string,
            nickname: PropTypes.string,
            writer: PropTypes.string,
            parentId: PropTypes.number,
            totalLike: PropTypes.number,
         })
    ),
    likedReplyIds: PropTypes.arrayOf(PropTypes.number),
    onReplySubmit: PropTypes.func.isRequired,
    onCommentDelete: PropTypes.func.isRequired,
    onReplyDelete: PropTypes.func.isRequired,
}

export default CommentView;