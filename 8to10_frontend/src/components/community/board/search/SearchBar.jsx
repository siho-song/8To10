import PropTypes from "prop-types";

const SearchBar = ({ searchKeyword, setSearchKeyword, handleSearch }) => {

    const handleInputChange = (e) => {
        setSearchKeyword(e.target.value);
    };

    return (
        <div className="search-bar">
            <input
                type="text"
                placeholder="검색어를 입력하세요"
                value={searchKeyword}
                onChange={handleInputChange}
            />
            <button onClick={handleSearch}>검색</button>
        </div>
    );
};

SearchBar.propTypes = {
    searchKeyword: PropTypes.string.isRequired,
    setSearchKeyword: PropTypes.func.isRequired,
    handleSearch: PropTypes.func.isRequired,
};

export default SearchBar;
