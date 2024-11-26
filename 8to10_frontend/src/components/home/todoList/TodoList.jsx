import TodoListHeader from "@/components/home/todoList/TodoListHeader.jsx";
import TodoItemsList from "@/components/home/todoList/TodoItemsList.jsx";

const TodoList = () => {
    return (
        <div className="todo-list-container">
            <h3>TO-DO</h3>
            <TodoListHeader/>
            <TodoItemsList/>
            <button id="submit-todo-btn">제출</button>
        </div>
    );
}

export default TodoList;