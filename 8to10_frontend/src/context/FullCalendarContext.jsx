import {createContext, useContext, useEffect, useState} from 'react';
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";

const CalendarContext = createContext();

export const useCalendar = () => useContext(CalendarContext);

// Provider 컴포넌트
export const FullCalendarContext = ({ children }) => {
    const [events, setEvents] = useState([]);

    const generateEventId = (event) => {
        if (event.parentId) {
            return `${event.parentId}-${event.id}`;
        }
        return event.id;
    };


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

                const formattedEvents = data.items.map(event => ({
                    id: generateEventId(event),
                    groupId: event.parentId,
                    title: event.title,
                    start: event.start,
                    end: event.end,
                    color: event.color,
                    extendedProps: {
                        type: event.type,
                        parentId: event.parentId,
                        commonDescription: event.commonDescription,
                        detailDescription: "",
                    }
                }));

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
