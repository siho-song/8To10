import {useContext} from "react";
import {CalendarContext} from "@/context/fullCalendar/FullCalendarContext.jsx";

export const useCalendar = () => useContext(CalendarContext);