import {createContext, useContext, useEffect, useState} from 'react';
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {formatFixedSchedule, formatNormalSchedule, formatVariableSchedule} from "../helpers/ScheduleFormatter.js";

const CalendarContext = createContext();

export const useCalendar = () => useContext(CalendarContext);

// Provider 컴포넌트
export const FullCalendarContext = ({ children }) => {
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

    return (
        <CalendarContext.Provider value={{ events, addEvent }}>
            {children}
        </CalendarContext.Provider>
    );
};
