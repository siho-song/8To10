
const TodoList = () => {
    return (
        <div className="todo-container">
            <h3>TO-DO</h3>
            <div id="date-navigation">
                <button id="prev-date-btn">이전</button>
                <span id="current-date"></span>
                <button id="next-date-btn">다음</button>
            </div>
            <div id="todo-list"></div>
            <button id="submit-todo-btn">제출</button>
        </div>
    );
}

export default TodoList;