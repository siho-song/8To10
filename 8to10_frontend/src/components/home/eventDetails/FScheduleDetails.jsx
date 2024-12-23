import PropTypes from "prop-types";
import {formatDateInfo, formatDateToLocalDateTime} from "@/helpers/TimeFormatter.js";
import {useCalendar} from "@/context/fullCalendar/UseCalendar.jsx";
import {useEffect, useState} from "react";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {createLocalDateTime, extractDateInfo} from "@/helpers/TimeFormatter.js";
import TimeEditForm from "@/components/home/eventDetails/TimeEditForm.jsx";

const FScheduleDetails = ({selectedEvent, onClose}) => {

    const {updateExtendedProps, updateProps} = useCalendar();

    const [detailDescription, setDetailDescription] = useState("");
    const [hasDetailDescription, setHasDetailDescription] = useState(false);
    const [hasCommonDescription, setHasCommonDescription] = useState(false);
    const [isDescriptionCreateMode, setIsDescriptionCreateMode] = useState(false);

    const [isItemEditMode, setIsItemEditMode] = useState(false);
    const startDateInfo = extractDateInfo(selectedEvent.start);
    const endDateInfo = extractDateInfo(selectedEvent.end);

    const [startDate, setStartDate] = useState({
        date: startDateInfo.date,
        period: startDateInfo.period,
        hour: startDateInfo.hour,
        minute: startDateInfo.minute,
    });
    const [endDate, setEndDate] = useState({
        date: endDateInfo.date,
        period: endDateInfo.period,
        hour: endDateInfo.hour,
        minute: endDateInfo.minute,
    });

    const [endDateError, setEndDateError] = useState("");

    useEffect(() => {
        setDetailDescription(selectedEvent.extendedProps.detailDescription);
        setHasDetailDescription(selectedEvent.extendedProps.detailDescription.length > 0);
        setHasCommonDescription(selectedEvent.extendedProps.commonDescription.length > 0);
        setIsDescriptionCreateMode(false);
        setIsItemEditMode(false);
        setEndDateError("");

        setStartDate(startDateInfo);
        setEndDate(endDateInfo);
    }, [selectedEvent]);


    const handleInputChange = (e) => {
        const inputValue = e.target.value;
        setDetailDescription(inputValue)
    }

    const handleCreateDetailDescription = async () => {
        setHasDetailDescription(false);
        setIsDescriptionCreateMode(true);
    }

    const handleItemEditButtonClick = () => {
        setIsItemEditMode(true);
    }

    const handleCreateCancel = () => {
        setDetailDescription(selectedEvent.extendedProps.detailDescription);
        setIsDescriptionCreateMode(false);
    }

    const handleEditCancel = () => {
        setDetailDescription(selectedEvent.extendedProps.detailDescription);
        const startDateInfo = extractDateInfo(selectedEvent.start);
        const endDateInfo = extractDateInfo(selectedEvent.end);

        setStartDate({
            date: startDateInfo.date,
            period: startDateInfo.period,
            hour: startDateInfo.hour,
            minute: startDateInfo.minute,
        });

        setEndDate({
            date: endDateInfo.date,
            period: endDateInfo.period,
            hour: endDateInfo.hour,
            minute: endDateInfo.minute,
        });
        setIsItemEditMode(false);
    }

    const handleItemEditSubmit = async () => {
        try {
            setEndDateError("");

            const startDateTime = createLocalDateTime(startDate);
            const endDateTime = createLocalDateTime(endDate);

            if (new Date(startDateTime) >= new Date(endDateTime)) {
                setEndDateError("종료 시간은 시작 시간 이후의 시간이어야 합니다.");
                return;
            }

            setEndDateError("");

            const url = "/schedule/fixed/detail";
            const data = {
                id: selectedEvent.extendedProps.originId,
                startDate: startDateTime,
                endDate: endDateTime,
                detailDescription: detailDescription,
            };

            const response = await authenticatedApi.patch(
                url,
                data,
                {apiEndPoint: API_ENDPOINT_NAMES.EDIT_F_SCHEDULE_ITEM,}
            );

            updateExtendedProps(selectedEvent.id, ['detailDescription'], [detailDescription])
            updateProps(selectedEvent.id, ['start', 'end'], startDateTime, endDateTime);
            setIsItemEditMode(false);
            setHasDetailDescription(detailDescription.length > 0);
        } catch (error) {
            console.error(error.toString());
            console.error(error);
        }
    }

    const handleDetailDescriptionSubmit = async () => {
        try {
            const url = "/schedule/fixed/detail";
            const data = {
                id: selectedEvent.extendedProps.originId,
                startDate: formatDateToLocalDateTime(selectedEvent.start),
                endDate: formatDateToLocalDateTime(selectedEvent.end),
                detailDescription: detailDescription,
            };

            const response = await authenticatedApi.patch(
                url,
                data,
                {apiEndPoint: API_ENDPOINT_NAMES.EDIT_F_SCHEDULE_ITEM,},
            );
            updateExtendedProps(selectedEvent.id, ['detailDescription'], [detailDescription]);
            setHasDetailDescription(true);
            setIsDescriptionCreateMode(false);
        } catch (error) {
            console.error(error.toString());
            console.error(error);
        }
    }

    const handleDelete = async () => {
        try {
            const url = "/schedule/fixed/detail";
            const response = await authenticatedApi.patch(
                url,
                {
                    id: selectedEvent.extendedProps.originId,
                    startDate: formatDateToLocalDateTime(selectedEvent.start),
                    endDate: formatDateToLocalDateTime(selectedEvent.end),
                    detailDescription: "",
                },
                {apiEndPoint: API_ENDPOINT_NAMES.EDIT_F_SCHEDULE_ITEM,},
            );

            updateExtendedProps(selectedEvent.id, ['detailDescription'], [""]);
            setHasDetailDescription(false);
            setDetailDescription("");
        } catch (error) {
            console.error(error.toString());
            console.error(error);
        }
    }


    return (
        <div id="event-details-container-fixed">
            <div className="event-details-header-fixed">
                <h2>일정 상세보기</h2>
                <button className="close-event-details" onClick={onClose}>&times;</button>
            </div>
            <div className="event-detail-props">
                {!isItemEditMode ? (
                    <>
                        <div className="event-detail-prop">
                            <h2>일정 제목</h2>
                            <hr className="event-detail-contour"/>
                            <p>{selectedEvent.title}</p>
                        </div>
                        <div className="event-detail-prop">
                            <h2>
                                <strong>시작 시간</strong>
                            </h2>
                            <hr className="event-detail-contour"/>
                            <TimeEditForm
                                type={"start"}
                                date={startDate}
                                setDate={setStartDate}
                                isDisabled={true}
                            />
                        </div>
                        <div className="event-detail-prop">
                            <h2>
                                <strong>종료 시간</strong>
                            </h2>
                            <hr className="event-detail-contour"/>
                            <TimeEditForm
                                type={"end"}
                                date={endDate}
                                setDate={setEndDate}
                                isDisabled={true}
                            />
                        </div>
                    </>
                ) : (
                    <>
                        <div className="event-detail-prop">
                            <h2>일정 제목</h2>
                            <hr className="event-detail-contour"/>
                            <p>{selectedEvent.title}</p>
                        </div>
                        <div className="event-detail-prop">
                            <h2>
                                <strong>시작 시간</strong>
                            </h2>
                            <hr className="event-detail-contour"/>
                            <TimeEditForm
                                type={"start"}
                                date={startDate}
                                setDate={setStartDate}
                                isDisabled={false}
                            />
                        </div>
                        <div className="event-detail-prop">
                            <h2>
                                <strong>종료 시간</strong>
                            </h2>
                            <hr className="event-detail-contour"/>
                            <TimeEditForm
                                type={"end"}
                                date={endDate}
                                setDate={setEndDate}
                                isDisabled={false}
                            />
                        </div>
                        {endDateError && <p className="error-message">{endDateError}</p>}
                    </>
                )}
                {hasCommonDescription &&
                    <div className="event-detail-prop">
                        <h2>
                            <strong>공통 일정 메모</strong>
                        </h2>
                        <hr className="event-detail-contour"/>
                        <p>{selectedEvent.extendedProps.commonDescription}</p>
                    </div>
                }
                {(hasDetailDescription && (!isItemEditMode && !isDescriptionCreateMode)) &&
                    <div className="event-detail-prop">
                        <div className="detail-description-header">
                            <h2>
                                <strong>개별 일정 메모</strong>
                            </h2>
                            <div className="detail-description-btns">
                                <p
                                    className="detail-description-delete-btn"
                                    onClick={handleDelete}
                                >삭제</p>
                            </div>
                        </div>
                        <hr className="event-details-contour"/>
                        <p>{detailDescription}</p>
                    </div>
                }
                {isDescriptionCreateMode &&
                    <div className="event-detail-prop">
                        <h2>
                            <strong>개별 메모 추가</strong>
                        </h2>
                        <hr className="event-details-contour"/>
                        <textarea
                            className="detail-description-textarea fixed"
                            value={detailDescription}
                            onChange={handleInputChange}
                            cols="30"
                            rows="3"
                        />
                        <div className="description-edit-btns">
                            <button
                                className="description-edit-btn fixed"
                                onClick={handleDetailDescriptionSubmit}
                            >등록
                            </button>
                            <button
                                className="description-cancel-btn"
                                onClick={handleCreateCancel}
                            >취소
                            </button>
                        </div>
                    </div>
                }
                {(isItemEditMode && hasDetailDescription) &&
                    <div className="event-detail-prop">
                        <h2>
                            <strong>개별 메모 수정</strong>
                        </h2>
                        <hr className="event-details-contour"/>
                        <textarea
                            className="detail-description-textarea fixed"
                            value={detailDescription}
                            onChange={handleInputChange}
                            cols="30"
                            rows="3"
                        />
                    </div>
                }
                <div>

                </div>
                {isItemEditMode &&
                    <button
                        className="fixed-edit-btn"
                        onClick={handleItemEditSubmit}
                    >
                        수정
                    </button>
                }
                {(!hasDetailDescription && !isDescriptionCreateMode && !isItemEditMode) &&
                    <button
                        className="create-detail-description fixed"
                        onClick={handleCreateDetailDescription}
                    >개별 메모 추가</button>
                }
                {!isDescriptionCreateMode &&
                    <>
                        {!isItemEditMode ? (
                            <button
                                className="fixed-edit-btn"
                                onClick={handleItemEditButtonClick}>
                                수정
                            </button>
                        ) : (
                            <button
                                className="fixed-edit-cancel-btn"
                                onClick={handleEditCancel}>
                                취소
                            </button>
                        )}
                    </>
                }
            </div>
        </div>
    );
}

FScheduleDetails.propTypes = {
    selectedEvent: PropTypes.shape({
        id: PropTypes.string.isRequired,
        groupId: PropTypes.string,
        title: PropTypes.string.isRequired,
        start: PropTypes.instanceOf(Date).isRequired,
        end: PropTypes.instanceOf(Date).isRequired,
        extendedProps: PropTypes.shape({
            type: PropTypes.string.isRequired,
            commonDescription: PropTypes.string,
            detailDescription: PropTypes.string,
            originId: PropTypes.number,
            parentId: PropTypes.number,
        }),
    }),
    onClose: PropTypes.func.isRequired,
}

export default FScheduleDetails;