import PropTypes from "prop-types";
import {formatDateTime} from "@/helpers/TimeFormatter.js";

const VScheduleDetails = ({selectedEvent, onClose}) => {

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
        id: PropTypes.number,
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