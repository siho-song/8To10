import "../../../styles/home/ScheduleTypePopup.css";

function ScheduleTypePopup({ onClose, onSelectType }) {
    return (
        <div className="popup" id="schedule-type-popup">
            <div className="popup-header">
                <span className="close-button" onClick={onClose}>&times;</span>
                <h1>일정 생성</h1>
            </div>
            <div className="popup-content">
                <div className="schedule-item">
                    <h3>고정 일정</h3>
                    <p>출근, 수업 등과 같이 항상 고정되어 일어나는 일정입니다.</p>
                    <button onClick={() => onSelectType('fixed')}>고정 일정 생성</button>
                </div>
                <div className="schedule-item">
                    <h3>일반 일정</h3>
                    <p>공부, 독서 등과 같이 계획을 세워 달성하고자 하는 일정입니다.</p>
                    <button onClick={() => onSelectType('normal')}>일반 일정 생성</button>
                </div>
                <div className="schedule-item">
                    <h3>변동 일정</h3>
                    <p>약속과 같은 자주와 갑자기 변경되거나 조정되는 일정입니다.</p>
                    <button onClick={() => onSelectType('variable')}>변동 일정 생성</button>
                </div>
            </div>
        </div>
    );
}

export default ScheduleTypePopup;