import PropTypes from 'prop-types';

import SearchBar from "@/components/community/board/search/SearchBar.jsx";
import SortCondition from "@/components/community/board/search/SortCondition.jsx";
import PostsPerPage from "@/components/community/board/search/PostsPerPage.jsx";
import SearchCondition from "@/components/community/board/search/SearchCondition.jsx";
import {useNavigate} from "react-router-dom";

BoardHeader.propTypes = {
    boardState: PropTypes.shape({
        sortCondition: PropTypes.string.isRequired,
        searchCondition: PropTypes.string.isRequired,
        searchKeyword: PropTypes.string.isRequired,
        postsPerPage: PropTypes.number.isRequired,
        totalPages: PropTypes.number.isRequired,
        pageNum: PropTypes.number.isRequired,
    }).isRequired,
    setBoardField: PropTypes.func.isRequired,
    handleSearch: PropTypes.func.isRequired
};

function BoardHeader({ boardState, setBoardField, handleSearch }) {

    const navigate = useNavigate();

    const { sortCondition, searchCondition, searchKeyword, postsPerPage } = boardState;

    return (
        <div className="board-header">
            <h2 id="board-title">자유게시판</h2>
            <button
                id="write-post"
                onClick={() => navigate("/community/board/add")}
            >
                글쓰기
            </button>

            <div className="board-controls">
                <PostsPerPage
                    postsPerPage={postsPerPage}
                    setPostsPerPage={(value) => setBoardField('postsPerPage', parseInt(value))}
                />

                <div className="search-window">
                    <SearchCondition
                        searchCondition={searchCondition}
                        setSearchCondition={(value) => setBoardField('searchCondition', value)}
                    />
                    <SearchBar
                        searchKeyword={searchKeyword}
                        setSearchKeyword={(value) => setBoardField('searchKeyword', value)}
                        handleSearch={handleSearch}
                    />
                </div>

                <SortCondition
                    sortCondition={sortCondition}
                    setSortCondition={(value) => setBoardField('sortCondition', value)}
                />
            </div>
        </div>
    );
}

export default BoardHeader;