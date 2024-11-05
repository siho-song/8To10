import PropTypes from 'prop-types';

function EventDetails({ selectedEvent, onClose }) {
    if (!selectedEvent) return null;

    return (
        <div
            id={selectedEvent.type==="fixed" ? "event-details-container-fixed"
                : (selectedEvent.type==="normal" ? "event-details-container-normal"
                : "event-details-container-variable")}>
            <div
                className={selectedEvent.type==="fixed" ? "event-details-header-fixed"
                : (selectedEvent.type==="normal" ? "event-details-header-normal"
                : "event-details-header-variable")}>
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
                    <p>{selectedEvent.start}</p>
                </div>
                <div className="event-detail-prop">
                    <h2>
                        <strong>종료 시간</strong>
                    </h2>
                    <hr className="event-detail-contour"/>
                    <p>{selectedEvent.end}</p>
                </div>
                <div className="event-detail-prop">
                    <h2>
                        <strong>{selectedEvent.type==="variable" ? "일정 메모" : "공통 일정 메모"}</strong>
                    </h2>
                    <hr className="event-detail-contour"/>
                    <p>{selectedEvent.commonDescription}</p>
                </div>
                {selectedEvent.type!=="variable" &&
                    <div className="event-detail-prop">
                        <h2>
                            <strong>개별 일정 메모</strong>
                        </h2>
                        <hr className="event-details-contour"/>
                        <p>{selectedEvent.detailDescription || ""}</p>
                    </div>
                }

                {selectedEvent.type==="variable" &&
                    <div className="handle-variable">
                        <button
                            className="edit-variable"
                            onClick={() => {}}
                        >수정</button>
                        <button
                            className="delete-variable"
                            onClick={() => {}}
                        >삭제</button>
                    </div>

                }
            </div>
        </div>
    );
}


EventDetails.propTypes = {
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
        commonDescription: PropTypes.string,
        detailDescription: PropTypes.string,
        type: PropTypes.string,
    }),
    onClose: PropTypes.func.isRequired,
}

export default EventDetails;