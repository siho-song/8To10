import {createContext, useContext, useEffect, useState} from 'react';

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
                const accessToken = localStorage.getItem('authorization');
                const response = await fetch('/api/schedule', {
                    method:"GET",
                    headers: {
                        'authorization': `Bearer ${accessToken}`,
                    }
                });

                if (!response.ok) {
                    throw new Error('서버와의 통신에 실패했습니다.');
                }

                const data = await response.json();

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
                console.error('Failed to fetch events:', error);
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
