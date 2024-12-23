import '@/styles/modal/ScheduleDeleteModal.css';
import PropTypes from "prop-types";
import Modal from "./Modal.jsx";

function ScheduleDeleteModal({ isOpen, onDeleteSingle, onDeleteAllFromNow, onDeleteAll, onClose }) {
    return (
        <Modal isOpen={isOpen} onClose={onClose}>
            <div className="delete-schedule-modal">
                <h2 className="modal-title">일정을 삭제하는 중입니다.</h2>
                <p className="modal-description">
                    이 일정과 이 일정의 모든 내용을 삭제하시겠습니까,<br/>
                    아니면 선택한 일정만 삭제하시겠습니까?
                </p>
                <div className="modal-actions">
                    <button className="btn-delete-single" onClick={onDeleteSingle}>
                        이 일정만 삭제
                    </button>
                    <button className="btn-delete-all" onClick={onDeleteAllFromNow}>
                        이후의 모든 일정 삭제
                    </button>
                    <button className="btn-delete-all" onClick={onDeleteAll}>
                        전체 일정 삭제
                    </button>
                    <div className="btn-group">
                        <button className="btn-cancel" onClick={onClose}>
                            취 소
                        </button>
                    </div>
                </div>
            </div>
        </Modal>
    );
}

ScheduleDeleteModal.propTypes = {
    isOpen: PropTypes.bool.isRequired,
    onDeleteSingle: PropTypes.func.isRequired,
    onDeleteAllFromNow: PropTypes.func.isRequired,
    onDeleteAll: PropTypes.func.isRequired,
    onClose: PropTypes.func.isRequired,
}

export default ScheduleDeleteModal;
