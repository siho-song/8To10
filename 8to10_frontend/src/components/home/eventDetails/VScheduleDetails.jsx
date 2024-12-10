import PropTypes from "prop-types";
import {extractDateInfo, formatDateTime} from "@/helpers/TimeFormatter.js";
import {useEffect, useState} from "react";
import {useCalendar} from "@/context/fullCalendar/UseCalendar.jsx";

const VScheduleDetails = ({selectedEvent, onClose}) => {

    const {updateExtendedProps, updatedEventTime} = useCalendar();

    const [isEditMode, setIsEditMode] = useState(false);

    const [title, setTitle] = useState(selectedEvent.title);
    const [commonDescription, setCommonDescription] = useState(selectedEvent.extendedProps.commonDescription);

    const startDateInfo = extractDateInfo(selectedEvent.start);
    const endDateInfo = extractDateInfo(selectedEvent.end);
    const [startDate, setStartDate] = useState(startDateInfo);
    const [endDate, setEndDate] = useState(endDateInfo);

    useEffect(() => {
        setTitle(selectedEvent.title);
        setCommonDescription(selectedEvent.extendedProps.commonDescription);
        setStartDate(startDateInfo);
        setEndDate(endDateInfo);
        setIsEditMode(false);
    }, [selectedEvent]);

    return (
        <div id="event-details-container-variable">
            <div
                className="event-details-header-variable">
                <h2>일정 상세보기</h2>
                <button className="close-event-details" onClick={onClose}>&times;</button>
            </div>
            <div className="event-detail-props">
                <div className="event-detail-prop">
                    <h2>
                        일정 제목
                    </h2>
                    <hr className="event-detail-contour"/>
                    <p>{selectedEvent.title}</p>
                </div>
                <div className="event-detail-prop">
                    <h2>
                        <strong>시작 시간</strong>
                    </h2>
                    <hr className="event-detail-contour"/>
                    <p>{formatDateTime(selectedEvent.start)}</p>
                </div>
                <div className="event-detail-prop">
                    <h2>
                        <strong>종료 시간</strong>
                    </h2>
                    <hr className="event-detail-contour"/>
                    <p>{formatDateTime(selectedEvent.end)}</p>
                </div>
                <div className="event-detail-prop">
                    <h2>
                        <strong>일정 메모</strong>
                    </h2>
                    <hr className="event-detail-contour"/>
                    <p>{selectedEvent.extendedProps.commonDescription}</p>
                </div>

                <div className="handle-variable">
                    <button
                        className="edit-variable"
                        onClick={() => {
                        }}
                    >수정
                    </button>
                    <button
                        className="delete-variable"
                        onClick={() => {
                        }}
                    >삭제
                    </button>
                </div>
            </div>
        </div>
    );
}

VScheduleDetails.propTypes = {
    selectedEvent: PropTypes.shape({
        id: PropTypes.string.isRequired,
        title: PropTypes.string.isRequired,
        start: PropTypes.instanceOf(Date).isRequired,
        end: PropTypes.instanceOf(Date).isRequired,
        extendedProps: PropTypes.shape({
            type: PropTypes.string.isRequired,
            commonDescription: PropTypes.string,
        }),
    }),
    onClose: PropTypes.func.isRequired,
}


export default VScheduleDetails;