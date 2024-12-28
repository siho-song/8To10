import PropTypes from "prop-types";
import {
    extractDateInfo,
    formatBufferTime,
    formatDate,
    formatDateTime,
    formatDateToLocalDateTime
} from "@/helpers/TimeFormatter.js";
import {useEffect, useState} from "react";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {useCalendar} from "@/context/fullCalendar/UseCalendar.jsx";
import TimeEditForm from "@/components/home/eventDetails/TimeEditForm.jsx";
import {validateTitle} from "@/components/home/eventDetails/ValidateEventDetails.js";
import ScheduleDeleteModal from "@/components/modal/ScheduleDeleteModal.jsx";

const NScheduleDetails = ({selectedEvent, onClose}) => {

    const {deleteEvent, deleteEventsByGroupId, deleteEventsAfterDateByGroupId, updateExtendedProps, updatePropsByGroupId, updateExtendedPropsByGroupId, countEventsByGroupId, getEarliestStartByGroupId} = useCalendar();

    const [detailDescription, setDetailDescription] = useState("");
    const [hasDetailDescription, setHasDetailDescription] = useState(false);
    const [commonDescription, setCommonDescription] = useState("");
    const [hasCommonDescription, setHasCommonDescription] = useState(false);

    const [isDetailDescriptionEditMode, setIsDetailDescriptionEditMode] = useState(false);
    const [isCommonDescriptionEditMode, setIsCommonDescriptionEditMode] = useState(false);

    const [title, setTitle] = useState(selectedEvent.title);
    const [isItemEditMode, setIsItemEditMode] = useState(false);

    const [titleError, setTitleError] = useState("");

    const [isModalOpen, setIsModalOpen] = useState(false);

    useEffect(() => {
        setDetailDescription(selectedEvent.extendedProps.detailDescription);
        setCommonDescription(selectedEvent.extendedProps.commonDescription);

        setHasDetailDescription(selectedEvent.extendedProps.detailDescription.length > 0);
        setHasCommonDescription(selectedEvent.extendedProps.commonDescription.length > 0);

        setIsDetailDescriptionEditMode(false);
        setIsCommonDescriptionEditMode(false);
        setIsItemEditMode(false);

        setIsModalOpen(false);

        setTitleError("");

        setTitle(selectedEvent.title);
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

    const handleEditDetailDescription = () => {
        setIsDetailDescriptionEditMode(true);
    }

    const handleEditCommonDescription = () => {
        setIsCommonDescriptionEditMode(true);
    }

    const handleDetailEditCancel = () => {
        setDetailDescription(selectedEvent.extendedProps.detailDescription);
        setIsDetailDescriptionEditMode(false);
    }

    const handleCommonEditCancel = () => {
        setCommonDescription(selectedEvent.extendedProps.commonDescription);
        setIsCommonDescriptionEditMode(false);
    }

    const handleItemEditButtonClick = () => {
        setIsItemEditMode(true);
    }

    const handleEditCancel = () => {
        setDetailDescription(selectedEvent.extendedProps.detailDescription);
        setCommonDescription(selectedEvent.extendedProps.commonDescription);
        setTitle(selectedEvent.title);
        setTitleError("");
        setIsItemEditMode(false);
    }

    const handleDetailDescriptionSubmit = async () => {
        const url = "/schedule/normal/detail";
        const data = {
            id: selectedEvent.extendedProps.originId,
            detailDescription: detailDescription,
        }
        const response = await authenticatedApi.put(
            url,
            data,
            {apiEndPoint: API_ENDPOINT_NAMES.EDIT_N_SCHEDULE_ITEM,},
        );

        updateExtendedProps(selectedEvent.id, ['detailDescription'], [detailDescription]);
        setHasDetailDescription(detailDescription.length > 0);
        setIsDetailDescriptionEditMode(false);
    }

    const handleCommonDescriptionSubmit = async () => {
        const url = "/schedule/normal";
        const data = {
            id:selectedEvent.extendedProps.parentId,
            title: title,
            commonDescription: commonDescription,
        }
        const response = await authenticatedApi.put(
            url,
            data,
            {apiEndPoint: API_ENDPOINT_NAMES.EDIT_N_SCHEDULE},
        );

        updateExtendedPropsByGroupId(parseInt(selectedEvent.groupId), ['commonDescription'], [commonDescription]);
        setHasCommonDescription(commonDescription.length > 0);
        setIsCommonDescriptionEditMode(false);
    }

    const handleItemEditSubmit = async () => {
        if (!validateTitle(title, setTitleError)) {
            return;
        }

        const urlOfItemData = "/schedule/normal/detail";
        const urlOfTotalData = "/schedule/normal";
        const itemData = {
            id: selectedEvent.extendedProps.originId,
            detailDescription: detailDescription,
        }
        const totalData = {
            id: selectedEvent.extendedProps.parentId,
            title: title,
            commonDescription: commonDescription,
        }

        const responseOfItemData = await authenticatedApi.put(
            urlOfItemData,
            itemData,
            {apiEndPoint: API_ENDPOINT_NAMES.EDIT_N_SCHEDULE_ITEM,},
        );
        updateExtendedProps(selectedEvent.id, ['detailDescription'], [detailDescription]);
        setHasDetailDescription(detailDescription.length > 0);

        const responseOfTotalData = await authenticatedApi.put(
            urlOfTotalData,
            totalData,
            {apiEndPoint: API_ENDPOINT_NAMES.EDIT_N_SCHEDULE,},
        );
        updateExtendedPropsByGroupId(parseInt(selectedEvent.groupId), ['commonDescription'], [commonDescription]);
        setHasCommonDescription(commonDescription.length > 0);
        updatePropsByGroupId(parseInt(selectedEvent.groupId), ['title'], [title]);

        setIsItemEditMode(false);
    }

    const handleDeleteButtonClick = () => {
        openModal();
    }

    const openModal = () => {
        setIsModalOpen(true);
    };
    const closeModal = () => {
        setIsModalOpen(false);
    };

    const handleTotalDelete = async () => {
        try {
            const url = `/schedule/${selectedEvent.extendedProps.parentId}`;
            const response = await authenticatedApi.delete(
                url,
                {apiEndPoint: API_ENDPOINT_NAMES.DELETE_SCHEDULE,},
            );

            alert("일정을 성공적으로 삭제했습니다.");
            deleteEventsByGroupId(parseInt(selectedEvent.groupId));
            closeModal();
            onClose();
        } catch(e) {
            alert("일정을 삭제하지 못했습니다. 다시시도 해주세요.");
        }
    }

    const handleTotalDeleteFromNow = async () => {
        const earliestStart = getEarliestStartByGroupId(parseInt(selectedEvent.groupId));

        if (formatDateToLocalDateTime(selectedEvent.start) === earliestStart) {
            await handleTotalDelete();
            return;
        }

        try {
            const url = `/schedule/normal/detail?parentId=${selectedEvent.extendedProps.parentId}&startDate=${formatDateToLocalDateTime(selectedEvent.start)}`;
            const response = await authenticatedApi.delete(
                url,
                {apiEndPoint: API_ENDPOINT_NAMES.DELETE_N_SCHEDULE_FROM_NOW,},
            );
            alert("일정을 성공적으로 삭제했습니다.");
            deleteEventsAfterDateByGroupId(parseInt(selectedEvent.groupId), selectedEvent.start);
            closeModal();
            onClose();
        } catch (e) {
            alert("일정을 삭제하지 못했습니다. 다시시도 해주세요.");
        }
    }

    const handleItemDelete = async () => {
        const currentEventCounts = countEventsByGroupId(parseInt(selectedEvent.groupId));

        if (currentEventCounts === 1) {
            await handleTotalDelete();
            return;
        }

        try {
            const url = `/schedule/normal/detail/${selectedEvent.extendedProps.originId}`;
            const response = await authenticatedApi.delete(
                url,
                {apiEndPoint: API_ENDPOINT_NAMES.DELETE_N_SCHEDULE_FROM_NOW,},
            );

            alert("일정을 성공적으로 삭제했습니다.");
            deleteEvent(selectedEvent.id);
            closeModal();
            onClose();
        } catch (e) {
            alert("일정을 삭제하지 못했습니다. 다시시도 해주세요.");
        }
    }

    return (
        <div id="event-details-container-normal">
            <div className="event-details-header-normal">
                <h2>일정 상세보기</h2>
                <button className="close-event-details" onClick={onClose}>&times;</button>
            </div>
            <div className="event-detail-props">
                {!isItemEditMode ? (
                    <div className="event-detail-prop">
                        <h2>일정 제목</h2>
                        <hr className="event-detail-contour"/>
                        <p>{title}</p>
                    </div>
                ) : (
                    <div className="event-detail-prop">
                        <h2>일정 제목</h2>
                        <hr className="event-detail-contour"/>
                        <input
                            type="text"
                            id="edit-schedule-title-normal"
                            name="title"
                            placeholder="일정 제목"
                            maxLength="80"
                            value={title}
                            onChange={handleTitleChange}
                        />
                        {titleError && <p className="error-message">{titleError}</p>}
                    </div>
                )}
                <div className="event-detail-prop">
                    <h2>
                        <strong>시작 시간</strong>
                    </h2>
                    <hr className="event-detail-contour"/>
                    <TimeEditForm
                        date={extractDateInfo(selectedEvent.start)}
                        setDate={() => {}}
                        type={"start"}
                        isDisabled={true}/>
                </div>
                <div className="event-detail-prop">
                    <h2>
                        <strong>종료 시간</strong>
                    </h2>
                    <hr className="event-detail-contour"/>
                    <TimeEditForm
                        date={extractDateInfo(selectedEvent.end)}
                        setDate={() => {}}
                        type={"end"}
                        isDisabled={true}/>
                </div>
                <div className="event-detail-prop">
                    <div className="description-header">
                        <h2>
                            <strong>일정 공통 메모</strong>
                        </h2>
                        {!hasCommonDescription && !isCommonDescriptionEditMode && !isItemEditMode &&
                            <div className="description-btns">
                                <div className="description-btns">
                                    <p className="description-btn normal"
                                       onClick={handleEditCommonDescription}>수정</p>
                                </div>
                            </div>
                        }
                    </div>
                    <hr className="event-details-contour"/>
                    {isItemEditMode || isCommonDescriptionEditMode ? (
                        <textarea
                            className="description-textarea normal"
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
                    {isCommonDescriptionEditMode &&
                        <div className="description-edit-btns">
                            <button
                                className="description-edit-btn normal"
                                onClick={handleCommonDescriptionSubmit}
                            >등록
                            </button>
                            <button
                                className="description-cancel-btn"
                                onClick={handleCommonEditCancel}
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
                        {!hasDetailDescription && !isDetailDescriptionEditMode && !isItemEditMode &&
                            <div className="description-btns">
                                <div className="description-btns">
                                    <p className="description-btn normal"
                                       onClick={handleEditDetailDescription}>수정</p>
                                </div>
                            </div>
                        }
                    </div>
                    <hr className="event-details-contour"/>
                    {isItemEditMode || isDetailDescriptionEditMode ? (
                        <textarea
                            className="description-textarea normal"
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
                    {isDetailDescriptionEditMode &&
                        <div className="description-edit-btns">
                            <button
                                className="description-edit-btn normal"
                                onClick={handleDetailDescriptionSubmit}
                            >등록
                            </button>
                            <button
                                className="description-cancel-btn"
                                onClick={handleDetailEditCancel}
                            >취소
                            </button>
                        </div>
                    }
                </div>
                <div className="event-detail-prop">
                    <h2><strong>일일 목표 달성 량</strong></h2>
                    <hr className="event-details-contour"/>
                    <p>{selectedEvent.extendedProps.dailyAmount}</p>
                </div>
                <div className="event-detail-prop">
                    <h2><strong>일정 시작 전 여유시간</strong></h2>
                    <hr className="event-details-contour"/>
                    <p>{formatBufferTime(selectedEvent.extendedProps.bufferTime)}</p>
                </div>
                {(!isItemEditMode && !(isDetailDescriptionEditMode || isCommonDescriptionEditMode)) &&
                    <>
                        <button
                            className="normal-edit-btn"
                            onClick={handleItemEditButtonClick}>
                            수정
                        </button>
                        <button
                            className="edit-cancel-btn"
                            onClick={handleDeleteButtonClick}>
                            삭제
                        </button>
                    </>

                }
                {isItemEditMode &&
                    <>
                        <button
                            className="normal-edit-btn"
                            onClick={handleItemEditSubmit}
                            disabled={!!titleError}>
                            수정
                        </button>

                        <button
                            className="edit-cancel-btn"
                            onClick={handleEditCancel}>
                            취소
                        </button>
                    </>
                }
            </div>
            <ScheduleDeleteModal
                onClose={closeModal}
                isOpen={isModalOpen}
                onDeleteSingle={handleItemDelete}
                onDeleteAllFromNow={handleTotalDeleteFromNow}
                onDeleteAll={handleTotalDelete}
            />
        </div>
    );
}

NScheduleDetails.propTypes = {
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
            bufferTime: PropTypes.string,
            completeStatus: PropTypes.bool,
            isComplete: PropTypes.bool,
            dailyAmount: PropTypes.number,
            achievedAmount: PropTypes.number,
            parentId: PropTypes.number,
            originId: PropTypes.number,
        }),
    }),
    onClose: PropTypes.func.isRequired,
}

export default NScheduleDetails;