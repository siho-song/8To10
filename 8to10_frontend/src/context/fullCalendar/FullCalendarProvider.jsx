import {useEffect, useState} from "react";
import {formatFixedSchedule, formatNormalSchedule, formatVariableSchedule} from "@/helpers/ScheduleFormatter.js";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {CalendarContext} from "@/context/fullCalendar/FullCalendarContext.jsx";

export const FullCalendarProvider = ({ children }) => {
    const [events, setEvents] = useState([]);

    useEffect(() => {

        const loadCalendarEvents = async () => {
            try {
                const url = '/schedule';
                const response = await authenticatedApi.get(
                    url,
                    {
                        apiEndPoint: API_ENDPOINT_NAMES.GET_EVENTS,
                    });
                const data = response.data;

                const formattedEvents = data.items.map((event) => {
                    if (event.type === "normal") {
                        return formatNormalSchedule(event);
                    } else if (event.type === "variable") {
                        return formatVariableSchedule(event);
                    } else if (event.type === "fixed") {
                        return formatFixedSchedule(event);
                    }
                });

                setEvents(formattedEvents);
            } catch (error) {
                console.error("Error : \n", error.toString());
                console.error(error);
            }
        }
        loadCalendarEvents();
    }, []);

    const addEvent = (event) => {
        setEvents((prevEvents) => [...prevEvents, event]);
    };

    const deleteEvent = (id) => {
        setEvents((prevEvents) =>
            prevEvents.filter((event) => event.id !== id)
        );
    };

    const updateExtendedProps = (id, keys, values) => {
        if (keys.length !== values.length) {
            console.error("Keys and values must have the same length.");
            return;
        }

        setEvents((prevEvents) =>
            prevEvents.map((event) =>
                id === event.id
                    ? {
                        ...event,
                        extendedProps: {
                            ...event.extendedProps,
                            ...keys.reduce((acc, key, index) => {
                                acc[key] = values[index];
                                return acc;
                            }, {}),
                        },
                    }
                    : event
            )
        );
    };

    const updateEventTime = (id, newStart, newEnd) => {
        setEvents((prevEvents) =>
            prevEvents.map((event) =>
                event.id === id
                    ? {
                        ...event,
                        start: newStart, // 새 시작 시간
                        end: newEnd,     // 새 종료 시간
                    }
                    : event
            )
        );
    };

    return (
        <CalendarContext.Provider value={{ events, addEvent, updateExtendedProps, updateEventTime, deleteEvent }}>
            {children}
        </CalendarContext.Provider>
    );
};