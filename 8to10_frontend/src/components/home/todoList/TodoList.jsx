import TodoListHeader from "@/components/home/todoList/TodoListHeader.jsx";
import TodoItemsList from "@/components/home/todoList/TodoItemsList.jsx";
import {useCalendar} from "@/context/fullCalendar/useCalendar.jsx";
import {useState} from "react";

const TodoList = () => {
    const {events} = useCalendar();

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const [currentDate, setCurrentDate] = useState(today);
    const [todoEvents, setTodoEvents] = useState([]);
    
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