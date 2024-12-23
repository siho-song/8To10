import PropTypes from "prop-types";
import {formatDateToLocalDateTime} from "@/helpers/TimeFormatter.js";
import {useCalendar} from "@/context/fullCalendar/UseCalendar.jsx";
import {useEffect, useState} from "react";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {createLocalDateTime, extractDateInfo} from "@/helpers/TimeFormatter.js";
import TimeEditForm from "@/components/home/eventDetails/TimeEditForm.jsx";
import ScheduleDeleteModal from "@/components/modal/ScheduleDeleteModal.jsx";
import {validateDateTime, validateTitle} from "@/components/home/eventDetails/ValidateEventDetails.js";

const FScheduleDetails = ({selectedEvent, onClose}) => {
    const {updateExtendedProps, updateProps, updatePropsByGroupId, updateExtendedPropsByGroupId} = useCalendar();

    const [detailDescription, setDetailDescription] = useState("");
    const [hasDetailDescription, setHasDetailDescription] = useState(false);
    const [commonDescription, setCommonDescription] = useState("");
    const [hasCommonDescription, setHasCommonDescription] = useState(false);

    const [isDetailDescriptionCreateMode, setIsDetailDescriptionCreateMode] = useState(false);
    const [isCommonDescriptionCreateMode, setIsCommonDescriptionCreateMode] = useState(false);

    const [isModalOpen, setIsModalOpen] = useState(false);

    const [isItemEditMode, setIsItemEditMode] = useState(false);

    const startDateInfo = extractDateInfo(selectedEvent.start);
    const endDateInfo = extractDateInfo(selectedEvent.end);
    const [title, setTitle] = useState(selectedEvent.title);
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

    const [titleError, setTitleError] = useState("");
    const [dateError, setDateError] = useState("");

    useEffect(() => {
        setDetailDescription(selectedEvent.extendedProps.detailDescription);
        setCommonDescription(selectedEvent.extendedProps.commonDescription);
        setHasDetailDescription(selectedEvent.extendedProps.detailDescription.length > 0);
        setHasCommonDescription(selectedEvent.extendedProps.commonDescription.length > 0);
        setIsDetailDescriptionCreateMode(false);
        setIsCommonDescriptionCreateMode(false);
        setIsItemEditMode(false);
        setDateError("");
        setTitleError("");

        setIsModalOpen(false);
        setTitle(selectedEvent.title);
        setStartDate(startDateInfo);
        setEndDate(endDateInfo);
    }, [selectedEvent]);


    const handleDetailDescriptionChange = (e) => {
        const inputValue = e.target.value;
        setDetailDescription(inputValue);
    }

    const handleCommonDescriptionChange = (e) => {
        const inputValue = e.target.value;
        setCommonDescription(inputValue);
    }

    const handleTitleChange = (e) => {
        const inputValue = e.target.value;
        validateTitle(inputValue, setTitleError);
        setTitle(inputValue);
    }

    const handleStartDateChange = (updatedStartDate) => {
        setStartDate(updatedStartDate);
        const startDateTime = createLocalDateTime(updatedStartDate);
        const endDateTime = createLocalDateTime(endDate);
        validateDateTime(startDateTime, endDateTime, setDateError);
    };

    const handleEndDateChange = (updatedEndDate) => {
        setEndDate(updatedEndDate);
        const startDateTime = createLocalDateTime(startDate);
        const endDateTime = createLocalDateTime(updatedEndDate);
        validateDateTime(startDateTime, endDateTime, setDateError);
    };


    const handleCreateDetailDescription = () => {
        setIsDetailDescriptionCreateMode(true);
    }

    const handleCreateCommonDescription = () => {
        setIsCommonDescriptionCreateMode(true);
    }

    const handleItemEditButtonClick = () => {
        setIsItemEditMode(true);
    }

    const handleDetailCreateCancel = () => {
        setDetailDescription(selectedEvent.extendedProps.detailDescription);
        setIsDetailDescriptionCreateMode(false);
    }

    const handleCommonCreateCancel = () => {
        setCommonDescription(selectedEvent.extendedProps.detailDescription);
        setIsCommonDescriptionCreateMode(false);
    }

    const handleEditCancel = () => {
        setDetailDescription(selectedEvent.extendedProps.detailDescription);
        setCommonDescription(selectedEvent.extendedProps.commonDescription);
        const startDateInfo = extractDateInfo(selectedEvent.start);
        const endDateInfo = extractDateInfo(selectedEvent.end);

        setTitle(selectedEvent.title);
        setTitleError("")

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
        setDateError("");
        setIsItemEditMode(false);
    }

    const handleItemEditSubmit = async () => {
        const startDateTime = createLocalDateTime(startDate);
        const endDateTime = createLocalDateTime(endDate);

        if (!validateTitle(title, setTitleError) || !validateDateTime(startDateTime, endDateTime, setDateError)) {
            return;
        }

        const urlOfItemData = "/schedule/fixed/detail";
        const urlOfTotalData = "/schedule/fixed"
        const itemData = {
            id: selectedEvent.extendedProps.originId,
            startDate: startDateTime,
            endDate: endDateTime,
            detailDescription: detailDescription,
        };
        const totalData = {
            id: selectedEvent.extendedProps.parentId,
            title: title,
            commonDescription: commonDescription,
        }

        await authenticatedApi.patch(
            urlOfItemData,
            itemData,
            {apiEndPoint: API_ENDPOINT_NAMES.EDIT_F_SCHEDULE_ITEM,}
        );
        updateExtendedProps(selectedEvent.id, ['detailDescription'], [detailDescription])
        updateProps(selectedEvent.id, ['start', 'end'], [startDateTime, endDateTime]);
        setHasDetailDescription(detailDescription.length > 0);

        await authenticatedApi.put(
            urlOfTotalData,
            totalData,
            {apiEndPoint: API_ENDPOINT_NAMES.EDIT_F_SCHEDULE},
        )
        updateExtendedPropsByGroupId(parseInt(selectedEvent.groupId), ['commonDescription'], [commonDescription])
        setHasCommonDescription(commonDescription.length > 0);
        updatePropsByGroupId(parseInt(selectedEvent.groupId), ['title'], [title]);

        setIsItemEditMode(false);
    }

    const handleCommonDescriptionSubmit = async () => {
        const url = "/schedule/fixed";
        const data = {
            id: selectedEvent.extendedProps.parentId,
            title: selectedEvent.title,
            commonDescription: commonDescription,
        }

        await authenticatedApi.put(
            url,
            data,
            {apiEndPoint: API_ENDPOINT_NAMES.EDIT_F_SCHEDULE},
        );

        // updateExtendedProps(selectedEvent.id, ['commonDescription'], [commonDescription]);
        updateExtendedPropsByGroupId(parseInt(selectedEvent.groupId), ['commonDescription'], [commonDescription]);
        setHasCommonDescription(true);
        setIsCommonDescriptionCreateMode(false);
    }

    const handleDetailDescriptionSubmit = async () => {
        const url = "/schedule/fixed/detail";
        const data = {
            id: selectedEvent.extendedProps.originId,
            startDate: formatDateToLocalDateTime(selectedEvent.start),
            endDate: formatDateToLocalDateTime(selectedEvent.end),
            detailDescription: detailDescription,
        };

        await authenticatedApi.patch(
            url,
            data,
            {apiEndPoint: API_ENDPOINT_NAMES.EDIT_F_SCHEDULE_ITEM,},
        );
        updateExtendedProps(selectedEvent.id, ['detailDescription'], [detailDescription]);
        setHasDetailDescription(true);
        setIsDetailDescriptionCreateMode(false);
    }

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
        <>
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
                                <p>{title}</p>
                            </div>
                            <div className="event-detail-prop">
                                <h2>
                                    <strong>시작 시간</strong>
                                </h2>
                                <hr className="event-detail-contour"/>
                                <TimeEditForm
                                    type={"start"}
                                    date={startDate}
                                    setDate={handleStartDateChange}
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
                                    setDate={handleEndDateChange}
                                    isDisabled={true}
                                />
                            </div>
                        </>
                    ) : (
                        <>
                            <div className="event-detail-prop">
                                <h2>일정 제목</h2>
                                <hr className="event-detail-contour"/>
                                <input
                                    type="text"
                                    id="edit-schedule-title-fixed"
                                    name="title"
                                    placeholder="일정 제목"
                                    maxLength="80"
                                    value={title}
                                    onChange={handleTitleChange}
                                />
                                {titleError && <p className="error-message">{titleError}</p>}
                            </div>
                            <div className="event-detail-prop">
                                <h2>
                                    <strong>시작 시간</strong>
                                </h2>
                                <hr className="event-detail-contour"/>
                                <TimeEditForm
                                    type={"start"}
                                    date={startDate}
                                    setDate={handleStartDateChange}
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
                                    setDate={handleEndDateChange}
                                    isDisabled={false}
                                />
                            </div>
                            {dateError && <p className="error-message">{dateError}</p>}
                        </>
                    )}
                    <div className="event-detail-prop">
                        <div className="description-header">
                            <h2>
                                <strong>일정 공통 메모</strong>
                            </h2>
                            {!hasCommonDescription && !isCommonDescriptionCreateMode && !isItemEditMode &&
                                <div className="description-btns">
                                    <div className="description-btns">
                                        <p className="description-btn fixed" onClick={handleCreateCommonDescription}>수정</p>
                                    </div>
                                </div>
                            }
                        </div>
                        <hr className="event-details-contour"/>
                        {isItemEditMode || isCommonDescriptionCreateMode ? (
                            <textarea
                                className="description-textarea fixed"
                                value={commonDescription}
                                onChange={handleCommonDescriptionChange}
                                placeholder={"일정 공통 메모를 입력해주세요."}
                                cols="30"
                                rows="3"
                            />
                        ) : hasCommonDescription ? (
                            <p>{commonDescription}</p>
                        ) : (
                            <p className="no-description">등록된 일정 공통 메모가 없습니다.</p>
                        )}
                        {isCommonDescriptionCreateMode &&
                            <div className="description-edit-btns">
                                <button
                                    className="description-edit-btn fixed"
                                    onClick={handleCommonDescriptionSubmit}
                                >등록
                                </button>
                                <button
                                    className="description-cancel-btn"
                                    onClick={handleCommonCreateCancel}
                                >취소
                                </button>
                            </div>
                        }
                    </div>

                    <div className="event-detail-prop">
                        <div className="description-header">
                            <h2>
                                <strong>일정 개별 메모</strong>
                            </h2>
                            {!hasDetailDescription && !isDetailDescriptionCreateMode && !isItemEditMode &&
                                <div className="description-btns">
                                    <div className="description-btns">
                                        <p className="description-btn fixed" onClick={handleCreateDetailDescription}>수정</p>
                                    </div>
                                </div>
                            }
                        </div>
                        <hr className="event-details-contour"/>
                        {isItemEditMode || isDetailDescriptionCreateMode ? (
                            <textarea
                                className="description-textarea fixed"
                                value={detailDescription}
                                onChange={handleDetailDescriptionChange}
                                placeholder={"일정 개별 메모를 입력해주세요."}
                                cols="30"
                                rows="3"
                            />
                        ) : hasDetailDescription ? (
                            <p>{detailDescription}</p>
                        ) : (
                            <p className="no-description">등록된 일정 개별 메모가 없습니다.</p>
                        )}
                        {isDetailDescriptionCreateMode &&
                            <div className="description-edit-btns">
                                <button
                                    className="description-edit-btn fixed"
                                    onClick={handleDetailDescriptionSubmit}
                                >등록
                                </button>
                                <button
                                    className="description-cancel-btn"
                                    onClick={handleDetailCreateCancel}
                                >취소
                                </button>
                            </div>
                        }
                    </div>
                    {(!isItemEditMode && !(isDetailDescriptionCreateMode || isCommonDescriptionCreateMode)) &&
                        <>
                            <button
                                className="fixed-edit-btn"
                                onClick={handleItemEditButtonClick}>
                                수정
                            </button>
                            <button
                                className="fixed-edit-cancel-btn"
                                onClick={handleDeleteButtonClick}>
                                삭제
                            </button>
                        </>

                    }
                    {isItemEditMode &&
                        <>
                            <button
                                className="fixed-edit-btn"
                                onClick={handleItemEditSubmit}
                                disabled={!!titleError || !!dateError}>
                                수정
                            </button>

                            <button
                                className="fixed-edit-cancel-btn"
                                onClick={handleEditCancel}>
                                취소
                            </button>
                        </>
                    }

                </div>
            </div>
        </>
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