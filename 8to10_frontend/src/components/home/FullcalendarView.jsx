
import { useRef, useState, useEffect } from 'react';

import HomeLeftSideBar from "./HomeLeftSideBar.jsx";
import HomeRightSideBar from "./HomeRightSideBar.jsx";

import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import listPlugin from '@fullcalendar/list';

import "@/styles/Styles.css";
import "@/styles/home/Fullcalendar.css";

import { useCalendar } from '@/context/fullCalendar/UseCalendar.jsx';

function handleWindowResize(calendar) {
    if (window.innerWidth < 768) {
        calendar.changeView('dayGridDay');
    } else if (window.innerWidth >= 768 && window.innerWidth < 1024) {
        calendar.changeView('dayGridWeek');
    } else {
        calendar.changeView('dayGridMonth');
    }
}

function getHeaderToolbarOptions() {
    if (window.innerWidth < 480) {
        return { left: 'prev,next', center: 'title', right: 'dayGridMonth,timeGridDay' };
    } else {
        return { left: 'prev,next today', center: 'title', right: 'dayGridMonth,timeGridWeek,dayGridDay' };
    }
}

function handleDateClick(calendar, info) {
    calendar.changeView('timeGridWeek', info.dateStr);
}

function FullCalendarView() {
    const calendarRef = useRef(null);
    const { events } = useCalendar();
    const [selectedGroupId, setSelectedGroupId] = useState(null);
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [showScheduleForm, setShowScheduleForm] = useState(false);

    const handleEventClick = (info) => {
        setSelectedGroupId(info.event.groupId);
        setShowScheduleForm(false);
        displayEventDetailsInSidebar(info.event);
    };

    const highlightGroupEvents = (groupId) => {
        const calendarApi = calendarRef.current.getApi();
        const allEvents = calendarApi.getEvents();

        allEvents.forEach((event) => {
            if (event.groupId === groupId) {
                event.setProp('classNames', ['highlighted']);
            } else {
                event.setProp('classNames', []); // Reset other events
            }
        });
    };

    const displayEventDetailsInSidebar = (event) => {
        const selected = {
            id: event.id,
            title: event.title,
            start: event.start,
            end: event.end,
            groupId: event.groupId || null,
            extendedProps: event.extendedProps
        };
        setSelectedEvent(selected);
    };

    const handleClose = () => {
        setSelectedEvent(null);
        setShowScheduleForm(false);
        setSelectedGroupId(null);
    };

    const resetEventHighlights = () => {
        const calendarApi = calendarRef.current.getApi();
        const allEvents = calendarApi.getEvents();

        allEvents.forEach((event) => {
            event.setProp('classNames', []); // Reset highlights
        });
    };

    useEffect(() => {
        const resizeListener = () => handleWindowResize(calendarRef.current.getApi());
        window.addEventListener('resize', resizeListener);
        return () => {
            window.removeEventListener('resize', resizeListener);
        };
    }, []);

    useEffect(()=> {
        if (calendarRef.current) {
            calendarRef.current.getApi().refetchEvents();
        }
    }, [events])

    return (
        <div className="main-container">
            <HomeLeftSideBar />
            <div className="main-content">
                <div id="calendar">
                    <FullCalendar
                        ref={calendarRef}
                        plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin, listPlugin]}
                        initialView="dayGridMonth"
                        initialDate={new Date()}
                        nowIndicator={true}
                        locale="ko"
                        headerToolbar={getHeaderToolbarOptions()}
                        dayMaxEvents={true}
                        allDaySlot={false}
                        firstDay={1}
                        events={events}
                        titleRangeSeparator=" - "
                        dateClick={(info) => handleDateClick(calendarRef.current.getApi(), info)}
                        eventClick={handleEventClick}
                        eventClassNames={(event) =>
                            event.event.groupId === selectedGroupId ? ['highlighted'] : []
                        }
                        selectable={true}
                    />
                </div>
            </div>
            <HomeRightSideBar
                selectedEvent={selectedEvent}
                showScheduleForm={showScheduleForm}
                onClose={handleClose}
                onShowForm={() => {
                    setSelectedEvent(null);
                    setShowScheduleForm(true);
                }}
            />
        </div>
    );
}

export default FullCalendarView;
