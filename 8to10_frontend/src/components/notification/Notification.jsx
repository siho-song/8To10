import {useEffect, useRef, useState} from "react";
import { EventSourcePolyfill } from "event-source-polyfill";

import {buildBearerToken} from "@/helpers/TokenManager.js";
import NotificationsIcon from '@mui/icons-material/Notifications';
import "@/styles/notification/Notification.css";
import NotificationPopup from "@/components/notification/NotificationPopup.jsx";
import {formatDateTime} from "@/helpers/TimeFormatter.js";

const Notification = () => {

    const [notifications, setNotifications] = useState(JSON.parse(localStorage.getItem('notifications')) || []);
    const [unreadCount, setUnreadCount] = useState(notifications.length);
    const [isPopupVisible, setPopupVisible] = useState(false);
    const eventSourceRef = useRef(null);

    const startNotificationConnection = () => {
        const accessToken = localStorage.getItem('Authorization');
        const lastEventId = localStorage.getItem('Last-Event-ID') || null;

        eventSourceRef.current = new EventSourcePolyfill("/api/notification/subscribe", {
            headers: {
                Authorization: buildBearerToken(accessToken),
                ...(lastEventId && { "Last-Event-ID": lastEventId }),
            }
        });

        eventSourceRef.current.onopen = () => {
            console.log("SSE 연결 성공");
        }

        eventSourceRef.current.addEventListener("notification", (event) => {
            const newNotificationData = JSON.parse(event.data);

            const newNotification = {
                ...newNotificationData,
                receivedAt: formatDateTime(new Date()),
                isRead: false,
            };

            if (event.lastEventId) {
                localStorage.setItem("Last-Event-ID", event.lastEventId);
            }

            const currentNotifications = [...notifications];
            const isDuplicate = currentNotifications.some(
                (notification) => notification.relatedEntityId === newNotification.relatedEntityId
            );

            const updatedNotifications = isDuplicate
                ? currentNotifications
                : [...currentNotifications, newNotification];
            localStorage.setItem("notifications", JSON.stringify(updatedNotifications));

            setNotifications(updatedNotifications);
            setUnreadCount((prevCount) => prevCount + 1);
        });

        eventSourceRef.current.onerror = (e) => {
            console.error("SSE 연결 에러 : ", e);
            if (eventSourceRef.current) eventSourceRef.current.close();
            setTimeout(() => startNotificationConnection(), 5000);
        }
    }

    const handleNotificationClick = () => {
        setPopupVisible(!isPopupVisible);
    };

    const handlePopupClose = () => {
        setPopupVisible(false);
    };

    // TODO 알림에서 notificationId 값 받아오면 이거 가지고 서버에 요청 날리는 메소드 구현
    // 인자는 notificationId로 수정하고, updatedNotifications도 수정
    const handleNotificationRemove = (relatedEntityId) => {
        let isRead = false;

        const updatedNotifications = notifications.filter(
            (notification) => {
                const shouldInclude = notification.relatedEntityId !== relatedEntityId;
                if (!shouldInclude) {
                    isRead = notification.isRead;
                }
                return shouldInclude;
            }
        );

        setNotifications(updatedNotifications);
        localStorage.setItem("notifications", JSON.stringify(updatedNotifications));

        // try {
        //     const url = `/notification/${relatedEntityId}`
        // } catch (error) {
        //     console.log(error.toString());
        //     console.log(error);
        // }
        if (!isRead){
            setUnreadCount((prevCount) => prevCount - 1);
        }
    };

    useEffect(() => {
        startNotificationConnection();

        return () => {
            if (eventSourceRef.current) {
                eventSourceRef.current.close();
                console.log("SSE 연결 해제");
            }
        };
    }, []);

    return (
        <div className="image-container" data-alt="알림">
            <button
                className={`notification-button ${unreadCount > 0 ? "has-unread" : ""}`}
                onClick={handleNotificationClick}
            >
                <NotificationsIcon/>
                {unreadCount > 0 && <span className="notification-badge">{unreadCount}</span>}
            </button>
            {isPopupVisible && (
                <NotificationPopup
                    notifications={notifications}
                    onClose={handlePopupClose}
                    onRemove={handleNotificationRemove}
                    setUnreadCount={setUnreadCount}
                />
            )}
        </div>
    );
}

export default Notification;