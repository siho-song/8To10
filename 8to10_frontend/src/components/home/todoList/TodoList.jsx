import TodoListHeader from "@/components/home/todoList/TodoListHeader.jsx";
import {useCalendar} from "@/context/fullCalendar/UseCalendar.jsx";
import {useEffect, useState} from "react";

import "@/styles/home/TodoList.css";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {formatDate} from "@/helpers/TimeFormatter.js";
import TodoItemsList from "@/components/home/todoList/TodoItemsList.jsx";
import {formatTodoEventsSubmit} from "@/helpers/ScheduleFormatter.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";

const TodoList = () => {
    const {events, updateExtendedProps} = useCalendar();

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const [currentDate, setCurrentDate] = useState(today);
    const [filteredEvents, setFilteredEvents] = useState([]);
    const [showTooltip, setShowTooltip] = useState(false);
    const [isCurrentDate, setIsCurrentDate] = useState(formatDate(currentDate) === formatDate(today));

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
        setIsCurrentDate(formatDate(currentDate) === formatDate(today));
        setFilteredEvents(filterNormalEventsByDate());
    }, [events, currentDate]);

    const changeCurrentDate = (date) => {
        const newDate = new Date(date);
        setCurrentDate(newDate);
        setIsCurrentDate(formatDate(currentDate) === formatDate(today));
    };

    const handleTodoSubmit = async () => {
        if (!isCurrentDate) {
            return;
        }
        let success = true;

        const url = "/schedule/normal/progress";
        const data = formatTodoEventsSubmit(currentDate, filteredEvents);

        const response = await authenticatedApi.patch(
            url,
            data,
            {apiEndPoint: API_ENDPOINT_NAMES.PATCH_N_SCHEDULE_PROGRESS}
        );

        if (!response.ok) {
            success = false;
        }

        if (success) {
            setShowTooltip(true);
            setTimeout(() => setShowTooltip(false), 800);
            for (let event of filteredEvents) {
                updateExtendedProps(event.id, ['completeStatus'], [event.extendedProps.isComplete]);
            }
        }
    };

    const handleDisabledSubmit = () => {
        setShowTooltip(true);
        setTimeout(() => setShowTooltip(false), 800);
    }

    return (
        <div className="todo-container">
            <h3>TODO</h3>
            <TodoListHeader
                currentDate={currentDate}
                changeCurrentDate={changeCurrentDate}
            />
            <TodoItemsList
                currentDate={currentDate}
                filteredEvents={filteredEvents}
            />
            <div
                className="submit-todo"
                onClick={handleDisabledSubmit}
            >
                <button
                    className="submit-todo-btn"
                    onClick={handleTodoSubmit}
                    disabled={!isCurrentDate}
                >
                    제출
                </button>
            </div>
            {(showTooltip && isCurrentDate)&&(
                <div className="tooltip">
                    제출이 완료되었습니다.
                </div>
            )}
            {(showTooltip && !isCurrentDate)&& (
                <div className="tooltip">
                    오늘의 일정만 업데이트할 수 있습니다.
                </div>
            )}
        </div>
    );
};

export default TodoList;