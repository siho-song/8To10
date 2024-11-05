import { useState } from "react";
import PropTypes from 'prop-types';

import "@/styles/home/RightSideBar.css"

import EventDetails from "./EventDetails.jsx";
import FixedScheduleForm from "./form/ScheduleForm/FixedScheduleForm.jsx";
import ScheduleTypePopup from "./form/ScheduleTypePopup.jsx";
import NormalScheduleForm from "@/components/home/form/ScheduleForm/NormalScheduleForm.jsx";
import VariableScheduleForm from "@/components/home/form/ScheduleForm/VariableScheduleForm.jsx";

HomeSidebarRight.propTypes = {
    selectedEvent: PropTypes.shape({
        title: PropTypes.string.isRequired,
        start: PropTypes.oneOfType([
            PropTypes.instanceOf(Date),
            PropTypes.string,
        ]).isRequired,
        end: PropTypes.oneOfType([
            PropTypes.instanceOf(Date),
            PropTypes.string,
        ]),
        description: PropTypes.string,
    }),
    onClose: PropTypes.func.isRequired,
    showScheduleForm: PropTypes.bool.isRequired,
    onShowForm: PropTypes.func.isRequired,
};

function HomeSidebarRight({ selectedEvent, onClose, showScheduleForm, onShowForm }) {
    const [showPopup, setShowPopup] = useState(false);
    const [scheduleType, setScheduleType] = useState(null);

    const handleTogglePopup = () => {
        setShowPopup((prev) => !prev);
    };

    const handleSelectType = (type) => {
        setScheduleType(type);
        setShowPopup(false);
        onShowForm();
    };

    return (
        <div className="sidebar" id="right-sidebar">
            <button id="toggle-add-schedule-btn" onClick={handleTogglePopup}>
                일정 생성
            </button>

            {!showScheduleForm && selectedEvent && (
                <EventDetails
                    selectedEvent={selectedEvent}
                    onClose={onClose}
                />
            )}

            {showPopup && (
                <ScheduleTypePopup
                    onClose={handleTogglePopup}
                    onSelectType={handleSelectType}
                />
            )}

            {showScheduleForm && scheduleType && (
                scheduleType === "fixed"
                    ? <FixedScheduleForm onClose={onClose} />
                    : (scheduleType === "normal" ? <NormalScheduleForm onClose={onClose} /> : <VariableScheduleForm onClose={onClose} />)
            )}
        </div>
    );
}

export default HomeSidebarRight;
