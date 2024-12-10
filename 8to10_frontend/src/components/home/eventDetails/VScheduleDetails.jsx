import PropTypes from "prop-types";
import {extractDateInfo, formatDateInfo} from "@/helpers/TimeFormatter.js";
import {useEffect, useState} from "react";
import {useCalendar} from "@/context/fullCalendar/UseCalendar.jsx";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import ConfirmDeleteModal from "@/components/modal/ConfirmDeleteModal.jsx";
import TimeEditForm from "@/components/home/eventDetails/TimeEditForm.jsx";

const VScheduleDetails = ({selectedEvent, onClose}) => {

    const {updateExtendedProps, updatedEventTime, deleteEvent} = useCalendar();

    const [isEditMode, setIsEditMode] = useState(false);

    const [title, setTitle] = useState(selectedEvent.title);
    const [commonDescription, setCommonDescription] = useState(selectedEvent.extendedProps.commonDescription);

    const startDateInfo = extractDateInfo(selectedEvent.start);
    const endDateInfo = extractDateInfo(selectedEvent.end);
    const [startDate, setStartDate] = useState(startDateInfo);
    const [endDate, setEndDate] = useState(endDateInfo);

    const [isModalOpen, setIsModalOpen] = useState(false);

    useEffect(() => {
        setTitle(selectedEvent.title);
        setCommonDescription(selectedEvent.extendedProps.commonDescription);
        setStartDate(startDateInfo);
        setEndDate(endDateInfo);
        setIsEditMode(false);
    }, [selectedEvent]);

    const handleDelete = async () => {
        try {
            const url = `/schedule/${selectedEvent.id}`;
            const response = await authenticatedApi.delete(
                url,
                {apiEndPoint: API_ENDPOINT_NAMES.DELETE_SCHEDULE,},
            )
            deleteEvent(selectedEvent.id);
            onClose();
        } catch(error) {
            console.error(error.toString());
            console.error(error);
        }
    }


    const openModal = () => {
        setIsModalOpen(true);
    };
    const closeModal = () => {
        setIsModalOpen(false);
    };

    return (
        <>
            <div id="event-details-container-variable">
                <div
                    className="event-details-header-variable">
                    <h2>일정 상세보기</h2>
                    <button className="close-event-details" onClick={onClose}>&times;</button>
                </div>
                <>
                    {isEditMode ? (
                        <div className="event-detail-props">
                            <div className="event-detail-prop">
                                <h2>
                                    일정 제목
                                </h2>
                                <hr className="event-detail-contour"/>
                                <p>{title}</p>
                            </div>
                            <div className="event-detail-prop">
                                <h2>
                                    <strong>시작 시간</strong>
                                </h2>
                                <hr className="event-detail-contour"/>
                                <p>{formatDateInfo(startDate)}</p>
                            </div>
                            <div className="event-detail-prop">
                                <h2>
                                    <strong>종료 시간</strong>
                                </h2>
                                <hr className="event-detail-contour"/>
                                <p>{formatDateInfo(endDate)}</p>
                            </div>
                            <div className="event-detail-prop">
                                <h2>
                                    <strong>일정 메모</strong>
                                </h2>
                                <hr className="event-detail-contour"/>
                                <p>{commonDescription}</p>
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
                                    onClick={handleDelete}
                                >삭제
                                </button>
                            </div>
                        </div>
                    ) : (
                        <div className="event-detail-props">
                            <div className="event-detail-prop">
                                <h2>
                                    일정 제목
                                </h2>
                                <hr className="event-detail-contour"/>
                                <p>{title}</p>
                            </div>
                            <div className="event-detail-prop">
                                <h2>
                                    <strong>시작 시간</strong>
                                </h2>
                                <hr className="event-detail-contour"/>
                                {/*<TimeEditForm setDate={startDate} type={"start"}/>*/}
                            </div>
                            <div className="event-detail-prop">
                                <h2>
                                    <strong>종료 시간</strong>
                                </h2>
                                <hr className="event-detail-contour"/>
                                <p>{formatDateInfo(endDate)}</p>
                            </div>
                            <div className="event-detail-prop">
                                <h2>
                                    <strong>일정 메모</strong>
                                </h2>
                                <hr className="event-detail-contour"/>
                                <p>{commonDescription}</p>
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
                                    onClick={openModal}
                                >삭제
                                </button>
                            </div>
                        </div>
                    )}

                    <ConfirmDeleteModal
                        isOpen={isModalOpen}
                        onClose={closeModal}
                        onConfirm={handleDelete}
                        itemName={selectedEvent.title}
                    />
                </>
            </div>
        </>

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