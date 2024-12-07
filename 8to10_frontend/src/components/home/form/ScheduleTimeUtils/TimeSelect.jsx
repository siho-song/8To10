import {useState} from 'react';

// 시간 선택 컴포넌트
export function TimeSelect({ selectType, handleChange, errorMessage }) {
    const getHours = () => {
        const hours = [];
        const maxHour = 23;
        const suffix = '시간';

        for (let i = 0 ; i <= maxHour; i++) {
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

    return (
        <div className="time-input-group" id={selectType+"-time-input-group"}>
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