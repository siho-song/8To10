import PropTypes from "prop-types";
import "@/styles/home/ScheduleForm.css";

function TimeEditForm({ type, date, setDate, isDisabled }) {
    const handleDateChange = (e) => {
        setDate({
            ...date,
            date: e.target.value,
        });
    };

    const handlePeriodChange = (e) => {
        setDate({
            ...date,
            period: e.target.value,
        });
    };

    const handleHourChange = (e) => {
        setDate({
            ...date,
            hour: parseInt(e.target.value, 10),
        });
    };

    const handleMinuteChange = (e) => {
        setDate({
            ...date,
            minute: parseInt(e.target.value, 10),
        });
    };

    return (
        <div>
            <div className="form-group">
                <label>{type === "start" ? "시작" : "종료"} 날짜</label>
                <input
                    type="date"
                    id={"schedule-" + type + "-date"}
                    name={type + "Date"}
                    value={date.date}
                    onChange={handleDateChange}
                    disabled={isDisabled}
                />
            </div>
            <div className="form-group">
                <label>{type === "start" ? "시작" : "종료"} 시간</label>
                <div className="time-input-group">
                    <select
                        id={"schedule-" + type + "-time"}
                        value={date.period}
                        onChange={handlePeriodChange}
                        disabled={isDisabled}>
                        <option value="AM">오전</option>
                        <option value="PM">오후</option>
                    </select>
                    <select
                        id={"schedule-" + type + "-hour"}
                        value={date.hour}
                        onChange={handleHourChange}
                        disabled={isDisabled}>
                        {Array.from({length: 12}, (_, i) => i + 1).map((hour) => (
                            <option key={hour} value={hour}>
                                {hour}시
                            </option>
                        ))}
                    </select>
                    <select
                        id={"schedule-" + type + "-minute"}
                        value={date.minute}
                        onChange={handleMinuteChange}
                        disabled={isDisabled}>
                        {Array.from({length: 60}, (_, i) => i).map((minute) => (
                            <option key={minute} value={minute}>
                                {minute}분
                            </option>
                        ))}
                    </select>
                </div>
            </div>
        </div>
    );
}

TimeEditForm.propTypes = {
    type: PropTypes.string.isRequired,
    date: PropTypes.shape({
        date: PropTypes.string.isRequired,
        period: PropTypes.string.isRequired,
        hour: PropTypes.number.isRequired,
        minute: PropTypes.number.isRequired,
    }),
    setDate: PropTypes.func.isRequired,
    isDisabled: PropTypes.bool.isRequired,

}

export default TimeEditForm;
