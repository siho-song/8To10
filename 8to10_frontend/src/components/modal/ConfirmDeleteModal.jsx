import Modal from './Modal';
import "@/styles/modal/ConfirmDeleteModal.css"
function ConfirmDeleteModal({ isOpen, onClose, onConfirm, itemName }) {
    const handleConfirm = () => {
        onConfirm();
        onClose();
    };

    const handleCancel = () => {
        onClose();
    };

    return (
        <Modal isOpen={isOpen} onClose={onClose}>
            <div className="confirm-delete-modal-container">
                <h2 className="confirm-delete-modal-title">{itemName}을(를) 정말로 삭제하시겠습니까?</h2>
                <div className="confirm-delete-modal-buttons">
                    <button className="confirm-delete-modal-delete-btn" onClick={handleConfirm}>삭제</button>
                    <button className="confirm-delete-modal-cancel-btn" onClick={handleCancel}>취소</button>
                </div>
            </div>
        </Modal>
    );
}

export default ConfirmDeleteModal;
