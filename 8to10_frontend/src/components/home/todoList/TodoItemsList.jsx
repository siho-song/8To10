import PropTypes from "prop-types";
import TodoItem from "@/components/home/todoList/TodoItem.jsx";

const TodoItemsList = ({filteredEvents}) => {
    return (
        <div id="todo-list-container">
            {filteredEvents.length > 0 ? (
                filteredEvents.map((event) => (
                    <TodoItem
                        key={event.id}
                        event={event}
                    />
                ))
            ) : (
                <p>현재 할 일이 없습니다.</p>
            )}
        </div>
    );
}

TodoItemsList.propTypes = {
    filteredEvents: PropTypes.arrayOf(
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
                isComplete: PropTypes.bool,
                dailyAmount: PropTypes.number,
                achievedAmount: PropTypes.number,
                originId: PropTypes.number,
            }).isRequired,
        }),
    ),
};

export default TodoItemsList;