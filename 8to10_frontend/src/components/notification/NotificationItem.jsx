import {useState} from "react";
import CloseIcon from "@mui/icons-material/Close";
import {useNavigate} from "react-router-dom";
import {BASE_URL, NOTIFICATION_TYPES} from "@/constants/NotificationTypes.js";
import PropTypes from "prop-types";

const NotificationItem = ({notification, onRemove, setUnreadCount}) => {
    const navigate = useNavigate();
    const [isRead, setIsRead] = useState(notification.isRead);

    // TODO 서버에 읽음 확인 요청
    const handleNotificationItemClick = () => {
        const { notificationType, targetUrl, relatedEntityId } = notification;

        let basePath = "";
        if (notificationType === NOTIFICATION_TYPES.REPLY_ADD) {
            basePath = BASE_URL.REPLY_ADD;
        } else if (notificationType === NOTIFICATION_TYPES.NESTED_REPLY_ADD) {
            basePath = BASE_URL.NESTED_REPLY_ADD;
        }

        setUnreadCount((prevCount) => prevCount - 1);
        setIsRead(!isRead);

        if (basePath) {
            const finalPath = `${basePath}${targetUrl}`;
            navigate(finalPath, {state: {relatedEntityId:relatedEntityId}});
        }
    };

    return (
        <div
            className={`notification-item ${isRead ? "read" : "unread"}`}
            onClick={() => handleNotificationItemClick(notification)}
        >
            <div>
                <p><strong>{notification.message}</strong></p>
                <p>{notification.receivedAt}</p>
            </div>
            <button
                className="notification-remove-button"
                onClick={(e) => {
                    e.stopPropagation();
                    onRemove(notification.relatedEntityId);
                }}
            >
                <CloseIcon/>
            </button>
        </div>
    );
}

NotificationItem.propTypes = {
    notification: PropTypes.shape({
        message: PropTypes.string,
        notificationType: PropTypes.string,
        relatedEntityId: PropTypes.number,
        targetUrl: PropTypes.string,
        isRead: PropTypes.bool,
        receivedAt: PropTypes.string,
    }),
    onRemove: PropTypes.func,
    setUnreadCount: PropTypes.func,
}

export default NotificationItem;