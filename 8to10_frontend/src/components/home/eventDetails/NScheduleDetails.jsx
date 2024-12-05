import PropTypes from "prop-types";
import {formatBufferTime, formatDateTime} from "@/helpers/TimeFormatter.js";
import {useState} from "react";

const NScheduleDetails = ({selectedEvent, onClose}) => {

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
                <div className="event-detail-prop">
                    <h2>
                        <strong>공통 일정 메모</strong>
                    </h2>
                    <hr className="event-detail-contour"/>
                    <p>{selectedEvent.extendedProps.commonDescription}</p>
                </div>
                <div className="event-detail-prop">
                    <div className="detail-description-header">
                        <h2>
                            <strong>개별 일정 메모</strong>
                        </h2>
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