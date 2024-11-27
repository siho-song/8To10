
import { useRef, useState, useEffect } from 'react';

import HomeLeftSideBar from "./HomeLeftSideBar.jsx";
import HomeRightSideBar from "./HomeRightSideBar.jsx";

import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import listPlugin from '@fullcalendar/list';

import "@/styles/styles.css";
import "@/styles/home/Fullcalendar.css";

import { useCalendar } from '@/context/fullCalendar/useCalendar.jsx';

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

function Calendar() {
    const calendarRef = useRef(null);
    const { events } = useCalendar();
    const [selectedEvent, setSelectedEvent] = useState(null);  // 선택된 이벤트 상태 관리
    const [showScheduleForm, setShowScheduleForm] = useState(false); // 일정 폼 상태 관리

    const handleEventClick = (info) => {
        setShowScheduleForm(false);
        displayEventDetailsInSidebar(info.event);
    };

    const displayEventDetailsInSidebar = (event) => {
        setSelectedEvent({
            type: event.extendedProps.type,
            title: event.title,
            start: event.start.toLocaleString(),
            end: event.end ? event.end.toLocaleString() : event.start.toLocaleString(),
            commonDescription: event.extendedProps.commonDescription || '',
            detailDescription: event.extendedProps.detailDescription || '',
        });
    };

    const handleClose = () => {
        setSelectedEvent(null);
        setShowScheduleForm(false);
    };

    useEffect(() => {
        const resizeListener = () => handleWindowResize(calendarRef.current.getApi());
        window.addEventListener('resize', resizeListener);
        return () => {
            window.removeEventListener('resize', resizeListener);
        };
    }, []);

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

export default Calendar;
