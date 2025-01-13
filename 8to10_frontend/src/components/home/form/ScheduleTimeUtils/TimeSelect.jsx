import {useState} from 'react';

export function TimeSelect({ selectType, handleChange, errorMessage }) {
    const [selectedHour, setSelectedHour] = useState(null);

    const getHours = () => {
        const hours = [];
        const maxHour = (selectType === "perform" || selectType === "buffer") ? 14 : 23;
        const suffix = '시간';

        for (let i = 0; i <= maxHour; i++) {
            hours.push(
                <option key={i} value={i < 10 ? `0${i}` : i}>
                    {i}
                    {suffix}
                </option>
            );
        }

        return hours;
    };

    const getMinutes = () => {
        const minutes = [];
        const maxMinute = (selectType === "perform" || selectType === "buffer") && selectedHour === "14" ? 0 : 59;

        for (let i = 0; i <= maxMinute; i++) {
            minutes.push(
                <option key={i} value={i < 10 ? `0${i}` : i}>
                    {i}분
                </option>
            );
        }

        return minutes;
    };

    const handleHourChange = (event) => {
        const value = event.target.value;
        setSelectedHour(value);
        handleChange(event);
    };

    const handleMinuteChange = (event) => {
        if (!((selectType === "perform" || selectType === "buffer") && selectedHour === "14")) {
            handleChange(event);
        }
    };

    return (
        <div className="time-input-group" id={selectType + "-time-input-group"}>
            <select
                id={"schedule-" + selectType + "-hour"}
                name={selectType + "Hour"}
                onChange={handleHourChange}
            >
                {getHours()}
            </select>
            <select
                id={"schedule-" + selectType + "-minute"}
                name={selectType + "Minute"}
                onChange={handleMinuteChange}
                disabled={(selectType === "perform" || selectType === "buffer") && selectedHour === "14"}
            >
                {getMinutes()}
            </select>
        </div>
    );
}


export function TimeSelectWithPeriod({ selectType, handleChange }) {
    const [period, setPeriod] = useState('AM');

    const getHours = () => {
        const hours = [];
        const maxHour = 12;
        const suffix = '시';

        for (let i = 1; i <= maxHour; i++) {
            hours.push(
                <option key={i} value={i < 10 ? `0${i}` : i}>
                    {i}
                    {suffix}
                </option>
            );
        }

        return hours;
    };


    const getMinutes = () => {
        const minutes = [];
        for (let i = 0; i < 60; i++) {
            minutes.push(
                <option key={i} value={i < 10 ? `0${i}` : i}>
                    {i}분
                </option>
            );
        }

        return minutes;
    };

    const handlePeriodChange = (e) => {
        setPeriod(e.target.value);
        handleChange(e);
    };

    return (
        <div className="time-input-group">
            <select
                id={"schedule-"+selectType+"-time"}
                name={selectType+"Time"}
                value={period}
                onChange={handlePeriodChange}
                className="time-select"
            >
                <option value="AM">오전</option>
                <option value="PM">오후</option>
            </select>
            <select
                id={"schedule-"+selectType+"-hour"}
                name={selectType + "Hour"}
                onChange={handleChange}
            >
                {getHours()}
            </select>
            <select
                id={"schedule-"+selectType+"-minute"}
                name={selectType + "Minute"}
                onChange={handleChange}
            >
                {getMinutes()}
            </select>
        </div>
    );
}