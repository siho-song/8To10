import ReactDOM from 'react-dom';
import "@/styles/modal/modal.css";

function Modal({ isOpen, onClose, children }) {
    if (!isOpen) return null;

    return ReactDOM.createPortal(
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <button className="modal-close" onClick={onClose}>X</button>
                {children}
            </div>
        </div>,
        document.getElementById('modal-root')
    );
}

export default Modal;