import PropTypes from "prop-types";

const SortCondition = ({ sortCondition, setSortCondition }) => {

    const handleSortChange = (e) => {
        setSortCondition(e.target.value);
    };

    return (
        <div className="sort-condition">
            <select value={sortCondition} onChange={handleSortChange}>
                <option value="DATE">날짜 순</option>
                <option value="LIKE">좋아요 순</option>
                <option value="SCRAP">스크랩 순</option>
            </select>
        </div>
    );
};

SortCondition.propTypes = {
    sortCondition: PropTypes.string.isRequired,
    setSortCondition: PropTypes.func.isRequired,
};

export default SortCondition;
