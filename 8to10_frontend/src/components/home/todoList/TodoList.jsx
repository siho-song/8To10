import TodoListHeader from "@/components/home/todoList/TodoListHeader.jsx";
import {useCalendar} from "@/context/fullCalendar/UseCalendar.jsx";
import {useEffect, useState} from "react";

import "@/styles/home/TodoList.css";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {formatDate} from "@/helpers/TimeFormatter.js";
import TodoItemsList from "@/components/home/todoList/TodoItemsList.jsx";
import {formatTodoEventSubmit} from "@/helpers/ScheduleFormatter.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";

const TodoList = () => {
    const {events, updateExtendedProps} = useCalendar();

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const [currentDate, setCurrentDate] = useState(today);
    const [filteredEvents, setFilteredEvents] = useState([]);

    useEffect(() => {
        if (!events) return(<p>로딩중....</p>);

        const filterNormalEventsByDate = () => {
            const targetDate = formatDate(currentDate);
            const filtered = events.filter((event) => {
                const eventDate = event.start.split("T")[0];
                return eventDate === targetDate && event.extendedProps.type === "normal";
            });
            return filtered.sort((a, b) => new Date(a.start) - new Date(b.start));
        };

        setFilteredEvents(filterNormalEventsByDate());
    }, [events, currentDate]);

    const changeCurrentDate = (date) => {
        const newDate = new Date(date);
        setCurrentDate(newDate);
    };

    const handleTodoSubmit = async () => {
        for (const event of filteredEvents) {
            const data = formatTodoEventSubmit(event);
            try {
                const url = "/schedule/normal/progress";
                const response = await authenticatedApi.patch(
                    url,
                    data,
                    {
                        apiEndPoint: API_ENDPOINT_NAMES.PATCH_N_SCHEDULE_PROGRESS,
                    });

                updateExtendedProps(event.id, ['completeStatus'], [event.extendedProps.isComplete]);
            } catch (error) {
                console.error(error.toString());
                console.error(error);
            }
        }
    };

    return (
        <div className="todo-container">
            <h3>TODO</h3>
            <TodoListHeader
                currentDate={currentDate}
                changeCurrentDate={changeCurrentDate}
            />
            <TodoItemsList
                filteredEvents={filteredEvents}
            />
            <button
                className="submit-todo-btn"
                onClick={handleTodoSubmit}
            >
                제출
            </button>
        </div>
    );
};

export default TodoList;