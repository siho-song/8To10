import PropTypes from "prop-types";
import {formatBufferTime, formatDateTime} from "@/helpers/TimeFormatter.js";
import {useEffect, useState} from "react";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {useCalendar} from "@/context/fullCalendar/useCalendar.jsx";

const NScheduleDetails = ({selectedEvent, onClose}) => {

    const {updateExtendedProps} = useCalendar();

    const [detailDescription, setDetailDescription] = useState("");
    const [hasDetailDescription, setHasDetailDescription] = useState(false);
    const [hasCommonDescription, setHasCommonDescription] = useState(false);

    const [isDescriptionEditMode, setIsDescriptionEditMode] = useState(false);
    const [isDescriptionCreateMode, setIsDescriptionCreateMode] = useState(false);

    useEffect(() => {
        setDetailDescription(selectedEvent.extendedProps.detailDescription);
        setHasDetailDescription(selectedEvent.extendedProps.detailDescription.length > 0);
        setHasCommonDescription(selectedEvent.extendedProps.commonDescription.length > 0);setDetailDescription(selectedEvent.extendedProps.detailDescription);
        setIsDescriptionCreateMode(false);
        setIsDescriptionEditMode(false);
    }, [selectedEvent]);

    const handleInputChange = (e) => {
        const inputValue = e.target.value;
        setDetailDescription(inputValue)
    }

    const handleCreateDetailDescription = async () => {
        setHasDetailDescription(false);
        setIsDescriptionCreateMode(true);
    }

    const handleEditDetailDescription = async () => {
        setIsDescriptionEditMode(true);
    }

    const handleCreateCancel = () => {
        setDetailDescription(selectedEvent.extendedProps.detailDescription);
        setIsDescriptionCreateMode(false);
    }

    const handleEditCancel = () => {
        setDetailDescription(selectedEvent.extendedProps.detailDescription);
        setIsDescriptionEditMode(false);
    }

    const handleDetailDescriptionSubmit = async () => {
        try {
            const url = "/schedule/normal/detail";
            const response = await authenticatedApi.put(
                url,
                {
                    id: selectedEvent.extendedProps.originId,
                    detailDescription: detailDescription,
                },
                {apiEndPoint: API_ENDPOINT_NAMES.EDIT_N_SCHEDULE_ITEM,},
            );

            updateExtendedProps(selectedEvent.id, ['detailDescription'], [detailDescription]);
            setHasDetailDescription(true);
            setIsDescriptionEditMode(false);
        } catch (error) {
            console.error(error.toString());
            console.error(error);
        }
    }

    const handleDelete = async () => {
        try {
            const url = "/schedule/normal/detail";
            const response = await authenticatedApi.put(
                url,
                {
                    id: selectedEvent.extendedProps.originId,
                    detailDescription: "",
                },
                {apiEndPoint: API_ENDPOINT_NAMES.EDIT_N_SCHEDULE_ITEM,},
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
        <div id="event-details-container-normal">
            <div className="event-details-header-normal">
                <h2>일정 상세보기</h2>
                <button className="close-event-details" onClick={onClose}>&times;</button>
            </div>
            <div className="event-detail-props">
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
                    <p>{formatDateTime(selectedEvent.start)}</p>
                </div>
                <div className="event-detail-prop">
                    <h2>
                        <strong>종료 시간</strong>
                    </h2>
                    <hr className="event-detail-contour"/>
                    <p>{formatDateTime(selectedEvent.end)}</p>
                </div>
                {hasCommonDescription &&
                    <div className="event-detail-prop">
                        <h2>
                            <strong>공통 일정 메모</strong>
                        </h2>
                        <hr className="event-detail-contour"/>
                        <p>{selectedEvent.extendedProps.commonDescription}</p>
                    </div>
                }
                {(hasDetailDescription && (!isDescriptionEditMode && !isDescriptionCreateMode)) &&
                    <div className="event-detail-prop">
                        <div className="detail-description-header">
                            <h2>
                                <strong>개별 일정 메모</strong>
                            </h2>
                            <div className="detail-description-btns">
                                <p className="detail-description-btn" onClick={handleEditDetailDescription}>수정</p>
                                <p className="detail-description-delete-btn" onClick={handleDelete}>삭제</p>
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
                            className="detail-description-textarea"
                            value={detailDescription}
                            onChange={handleInputChange}
                            cols="30"
                            rows="5"
                        />
                        <div className="description-edit-btns">
                            <button
                                className="description-edit-btn"
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
                {isDescriptionEditMode &&
                    <div className="event-detail-prop">
                        <h2>
                            <strong>개별 메모 수정</strong>
                        </h2>
                        <hr className="event-details-contour"/>
                        <textarea
                            className="detail-description-textarea"
                            value={detailDescription}
                            onChange={handleInputChange}
                            cols="30"
                            rows="5"
                        />
                        <div className="description-edit-btns">
                            <button
                                className="description-edit-btn"
                                onClick={handleDetailDescriptionSubmit}
                            >등록
                            </button>
                            <button
                                className="description-cancel-btn"
                                onClick={handleEditCancel}
                            >취소
                            </button>
                        </div>
                    </div>
                }
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
                {!hasDetailDescription &&
                    <button
                        className="create-detail-description"
                        onClick={handleCreateDetailDescription}
                    >개별 메모 추가</button>
                }
            </div>
        </div>
    );
}

NScheduleDetails.propTypes = {
    selectedEvent: PropTypes.shape({
        id: PropTypes.string.isRequired,
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