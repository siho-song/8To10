import PropTypes from "prop-types";

function PostsPerPage({ postsPerPage, setPostsPerPage}) {

    const handlePostsPerPageChange = (e) => {
        setPostsPerPage(e.target.value);
    }

    return (
        <div className="board-control-left">
            페이지 당 글 개수:
            <select
                id="posts-per-page"
                value={postsPerPage}
                onChange={handlePostsPerPageChange}
            >
                <option value="10">10</option>
                <option value="30">30</option>
                <option value="50">50</option>
            </select>
        </div>
    );
}

PostsPerPage.propTypes = {
    postsPerPage: PropTypes.number.isRequired,
    setPostsPerPage: PropTypes.func.isRequired,
};

export default PostsPerPage;