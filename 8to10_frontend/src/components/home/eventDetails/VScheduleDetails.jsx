import PropTypes from "prop-types";
import {createLocalDateTime, extractDateInfo, formatDateInfo} from "@/helpers/TimeFormatter.js";
import {useEffect, useState} from "react";
import {useCalendar} from "@/context/fullCalendar/UseCalendar.jsx";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import ConfirmDeleteModal from "@/components/modal/ConfirmDeleteModal.jsx";
import TimeEditForm from "@/components/home/eventDetails/TimeEditForm.jsx";

const VScheduleDetails = ({selectedEvent, onClose}) => {

    const {updateExtendedProps, updateProps, deleteEvent} = useCalendar();

    const [isEditMode, setIsEditMode] = useState(false);

    const [title, setTitle] = useState(selectedEvent.title);
    const [commonDescription, setCommonDescription] = useState(selectedEvent.extendedProps.commonDescription);

    const startDateInfo = extractDateInfo(selectedEvent.start);
    const endDateInfo = extractDateInfo(selectedEvent.end);
    const [startDate, setStartDate] = useState(startDateInfo);
    const [endDate, setEndDate] = useState(endDateInfo);

    const [isModalOpen, setIsModalOpen] = useState(false);

    const [endDateError, setEndDateError] = useState("");


    useEffect(() => {
        setTitle(selectedEvent.title);
        setCommonDescription(selectedEvent.extendedProps.commonDescription);
        setStartDate(startDateInfo);
        setEndDate(endDateInfo);
        setIsEditMode(false);
        setEndDateError("");
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

    const handleEditModeChange = () => {
        setIsEditMode(true);
    }

    const handleDescriptionChange = (e) => {
        const inputValue = e.target.value;
        setCommonDescription(inputValue);
    }

    const handleTitleChange = (e) => {
        const inputValue = e.target.value;
        setTitle(inputValue);
    }

    const openModal = () => {
        setIsModalOpen(true);
    };
    const closeModal = () => {
        setIsModalOpen(false);
    };
    const handleCancel = () => {
        setTitle(selectedEvent.title);
        setCommonDescription(selectedEvent.extendedProps.commonDescription);
        setStartDate(startDateInfo);
        setEndDate(endDateInfo);
        setIsEditMode(false);
        setEndDateError("");
    }

    const handleEditSubmit = async () => {
        try {
            setEndDateError("");

            const startDateTime = createLocalDateTime(startDate);
            const endDateTime = createLocalDateTime(endDate);

            if (new Date(startDateTime) >= new Date(endDateTime)) {
                setEndDateError("종료 시간은 시작 시간 이후의 시간이어야 합니다.");
                return;
            }

            const url = "/schedule/variable";
            const response = await authenticatedApi.put(
                url,
                {
                    id: selectedEvent.id,
                    title: title,
                    commonDescription: commonDescription,
                    startDate: startDateTime,
                    endDate: endDateTime,
                },
                {apiEndPoint: API_ENDPOINT_NAMES.EDIT_V_SCHEDULE,}
            );

            updateExtendedProps(selectedEvent.id, ['commonDescription'], [commonDescription]);
            updateProps(selectedEvent.id, ['title', 'start', 'end'], [title, startDateTime, endDateTime]);
            setIsEditMode(false);
        } catch (error) {
            console.error(error.toString());
            console.error(error);
        }
    }

    return (
        <>
            <div id="event-details-container-variable">
                <div
                    className="event-details-header-variable">
                    <h2>일정 상세보기</h2>
                    <button className="close-event-details" onClick={onClose}>&times;</button>
                </div>
                <>
                    {!isEditMode ? (
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
                                <TimeEditForm
                                    date={startDate}
                                    setDate={setStartDate}
                                    type={"start"}
                                    isDisabled={true}/>
                            </div>
                            <div className="event-detail-prop">
                                <h2>
                                    <strong>종료 시간</strong>
                                </h2>
                                <hr className="event-detail-contour"/>
                                <TimeEditForm
                                    date={endDate}
                                    setDate={setEndDate}
                                    type={"end"}
                                    isDisabled={true}/>
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
                                    onClick={handleEditModeChange}
                                >수정
                                </button>
                                <button
                                    className="delete-variable"
                                    onClick={openModal}
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
                                <input
                                    type="text"
                                    id="edit-schedule-title"
                                    name="title"
                                    placeholder="일정 제목"
                                    maxLength="80"
                                    value={title}
                                    onChange={handleTitleChange}
                                />
                            </div>
                            <div className="event-detail-prop">
                                <h2>
                                    <strong>시작 시간</strong>
                                </h2>
                                <hr className="event-detail-contour"/>
                                <TimeEditForm
                                    date={startDate}
                                    setDate={setStartDate}
                                    type={"start"}
                                    isDisabled={false}/>
                            </div>
                            <div className="event-detail-prop">
                                <h2>
                                    <strong>종료 시간</strong>
                                </h2>
                                <hr className="event-detail-contour"/>
                                <TimeEditForm
                                    date={endDate}
                                    setDate={setEndDate}
                                    type={"end"}
                                    isDisabled={false}/>
                            </div>
                            {endDateError && <p className="error-message">{endDateError}</p>}
                            <div className="event-detail-prop">
                                <h2>
                                    <strong>일정 메모</strong>
                                </h2>
                                <hr className="event-detail-contour"/>
                                <textarea
                                    className="detail-description-textarea variable"
                                    value={commonDescription}
                                    onChange={handleDescriptionChange}
                                    cols="30"
                                    rows="3"
                                />
                            </div>

                            <div className="handle-variable">
                                <button
                                    className="edit-variable"
                                    onClick={handleEditSubmit}
                                >등록
                                </button>
                                <button
                                    className="delete-variable"
                                    onClick={handleCancel}
                                >취소
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