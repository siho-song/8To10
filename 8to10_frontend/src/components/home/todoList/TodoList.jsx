import TodoListHeader from "@/components/home/todoList/TodoListHeader.jsx";
import TodoItemsList from "@/components/home/todoList/TodoItemsList.jsx";
import {useCalendar} from "@/context/fullCalendar/useCalendar.jsx";
import {useEffect, useState} from "react";

const TodoList = () => {
    const {events} = useCalendar();

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const [currentDate, setCurrentDate] = useState(today);
    const [todoEvents, setTodoEvents] = useState([]);

    const filterEventsByDate = (targetDate) => {
        if (!events) return [];

        const formattedTargetDate = new Date(targetDate).toDateString();

        return events.filter((event) => {
            const eventStartDate = new Date(event.start).toDateString();
            return eventStartDate === formattedTargetDate && event.extendedProps.type === "normal";
        });
    };

    useEffect(() => {
        setTodoEvents(filterEventsByDate(events));
    }, [events, currentDate]);

    const changeCurrentDate = (date) => {
        const newDate = new Date(date);
        setCurrentDate(newDate);
    }

    const handleTodoSubmit = (e) => {
        console.log("todolist 제출")
    }

    return (
        <div className="todo-container">
            <h3>TO-DO</h3>
            <TodoListHeader
                currentDate={currentDate}
                changeCurrentDate={changeCurrentDate}
            />
            <TodoItemsList
                todoEvents={todoEvents}
            />
            <button
                id="submit-todo-btn"
                onClick={handleTodoSubmit}
            >
                제출
            </button>
        </div>
    );
}

export default TodoList;