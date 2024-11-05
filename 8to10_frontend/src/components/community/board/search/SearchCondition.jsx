import PropTypes from "prop-types";

function SearchCondition({ searchCondition, setSearchCondition }) {
    const handleSearchConditionChange = (e) => {
        setSearchCondition(e.target.value);
    };

    return (
        <div className="search-condition">
            <select
                id="searchCond"
                name="searchCond"
                value={searchCondition}
                onChange={handleSearchConditionChange}
            >
                <option value="TITLE">제목</option>
                <option value="CONTENTS">내용</option>
                <option value="WRITER">닉네임</option>
            </select>
        </div>
    );
}

SearchCondition.propTypes = {
    searchCondition: PropTypes.string.isRequired,
    setSearchCondition: PropTypes.func.isRequired,
};

export default SearchCondition;