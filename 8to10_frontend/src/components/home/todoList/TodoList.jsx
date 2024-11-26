import TodoListHeader from "@/components/home/todoList/TodoListHeader.jsx";

const TodoList = () => {
    return (
        <div className="todo-container">
            <h3>TO-DO</h3>
            <TodoListHeader/>
            <div id="todo-list"></div>
            <button id="submit-todo-btn">제출</button>
        </div>
    );
}

export default TodoList;