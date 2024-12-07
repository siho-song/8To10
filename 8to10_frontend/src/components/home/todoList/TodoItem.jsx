import PropTypes from "prop-types";
import {formatLocalDateTimeToTime} from "@/helpers/TimeFormatter.js";
import {useState} from "react";
import {useCalendar} from "@/context/fullCalendar/UseCalendar.jsx";

const TodoItem = ({event}) => {

    const {updateExtendedProps} = useCalendar();

    const {extendedProps} = event;
    const [achievedAmount, setAchievedAmount] = useState(extendedProps.achievedAmount || 0);

    const handleInputChange = (e) => {
        const inputValue = parseInt(e.target.value, 10) || 0;
        if (inputValue >= 0 && inputValue <= extendedProps.dailyAmount) {
            if (inputValue === extendedProps.dailyAmount) {
                updateExtendedProps(event.id, ["isComplete", "achievedAmount"], [true, inputValue]);
            } else {
                updateExtendedProps(event.id, ["isComplete", "achievedAmount"], [false, inputValue]);
            }
            setAchievedAmount(inputValue);
        }
    };

    const handleCheck = () => {
        updateExtendedProps(event.id, ["isComplete"], [!event.extendedProps.isComplete]);
    }

    return (
        <div className={`todo-item ${extendedProps.isComplete ? "completed" : ""}`}>
            <div className="todo-item-content">
                <h3 className="todo-title">{event.title}</h3>
                {extendedProps.dailyAmount > 0 && (
                    <p className="todo-amount">목표 달성 량: <span>{extendedProps.dailyAmount}</span></p>
                )}
                <p className="todo-time">{formatLocalDateTimeToTime(event.start)} ~ {formatLocalDateTimeToTime(event.end)}</p>
            </div>
            {extendedProps.dailyAmount > 0 ? (
                <input
                    type="number"
                    className="achievement-input"
                    value={achievedAmount}
                    onChange={handleInputChange}
                    placeholder="달성량 입력"
                    min={"0"}
                    max={extendedProps.dailyAmount}
                />
            ) : (
                <button
                    className="check-button"
                    onClick={handleCheck}
                    aria-label="Mark as completed"
                >
                    {extendedProps.isComplete ? "✓" : "☐"}
                </button>
            )}
        </div>
    );

};

TodoItem.propTypes = {
    event: PropTypes.shape({
        id: PropTypes.string.isRequired,
        groupId: PropTypes.number,
        title: PropTypes.string.isRequired,
        start: PropTypes.string.isRequired,
        end: PropTypes.string.isRequired,
        color: PropTypes.string,
        extendedProps: PropTypes.shape({
            type: PropTypes.string.isRequired,
            parentId: PropTypes.number,
            commonDescription: PropTypes.string,
            detailDescription: PropTypes.string,
            bufferTime: PropTypes.string,
            completeStatus: PropTypes.bool,
            isComplete: PropTypes.bool,
            dailyAmount: PropTypes.number,
            achievedAmount: PropTypes.number,
            originId: PropTypes.number,
        }).isRequired,
    }),
};

export default TodoItem;
