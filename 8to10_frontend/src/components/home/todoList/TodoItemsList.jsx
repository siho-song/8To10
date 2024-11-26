import PropTypes from "prop-types";

const TodoItemsList = ({todoEvents}) => {
    return (
        <div id="todo-list-container">
            {todoEvents.length > 0 ? (
                todoEvents.map((event) => {
                    // TODO TodoItem구현
                })
            ): (
                <p>현재 할 일이 없습니다.</p>
            )}
        </div>
    );
}

TodoItemsList.propTypes = {
    todoEvents: PropTypes.arrayOf(
        PropTypes.shape({
            id: PropTypes.string.isRequired,
            groupId: PropTypes.number,
            title: PropTypes.string.isRequired,
            start: PropTypes.string.isRequired,
            end: PropTypes.string.isRequired,
            color: PropTypes.string,
            extendedProps: PropTypes.shape({
                type: PropTypes.string.isRequired,
                parentId: PropTypes.number,
                commonDescription: PropTypes.string,
                detailDescription: PropTypes.string,
                bufferTime: PropTypes.string,
                completeStatus: PropTypes.bool,
                dailyAmount: PropTypes.number,
            }).isRequired,
        })
    )
};

export default TodoItemsList;