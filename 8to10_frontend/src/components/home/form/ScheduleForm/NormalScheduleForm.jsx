import { InitializeTimeOptions, } from "@/components/home/form/ScheduleTimeUtils/TimeOptions.jsx";
import {
    formatDuration
} from "@/helpers/TimeFormatter.js";
import {useState} from "react";
import {useCalendar} from "@/context/FullCalendarContext.jsx";
import PropTypes from "prop-types";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {formatNormalSchedule} from "@/helpers/ScheduleFormatter.js";

function NormalScheduleForm({ onClose }) {

    const { addEvent } = useCalendar();

    const today = new Date();

    const [formData, setFormData] = useState({
        title: '',
        commonDescription: '',
        startDate: `${today.toISOString().split('T')[0]}`,
        endDate: `${new Date(today.getTime() + 24 * 60 * 60 * 1000).toISOString().split('T')[0]}`,
        bufferHour: 0,
        bufferMinute: 0,
        totalAmount: '0',
        performHour: 0,
        performMinute: 0,
        performInWeek: '1',
        includeSaturday: false,
        includeSunday: false
    });

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: type === "checkbox" ? checked : value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const finalData = {
            title: formData.title,
            commonDescription: formData.commonDescription,
            startDate: formData.startDate,
            endDate: formData.endDate,
            bufferTime: formatDuration(formData.bufferHour, formData.bufferMinute),
            totalAmount: formData.totalAmount,
            performInDay: formatDuration(formData.performHour, formData.performMinute),
            performInWeek: formData.performInWeek,
            isIncludeSaturday: formData.includeSaturday,
            isIncludeSunday: formData.includeSunday
        };

        try {
            const url = '/schedule/normal';
            const response = await authenticatedApi.post(
                url,
                finalData,
                {
                    apiEndPoint: API_ENDPOINT_NAMES.CREATE_N_SCHEDULE,
                });
            const data = response.data;

            data.items.forEach(event => {
                const formattedEvent = formatNormalSchedule(event);
                addEvent(formattedEvent);
            });
            onClose();

        } catch (error) {
            console.error(error.toString());
            console.error(error);
        }
    };

    return (
        <div id="schedule-form-container" style={{padding: '20px'}}>
            <h2 id="schedule-form-header">일반 일정 생성</h2>
            <form id="schedule-form" data-type="normal" onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="schedule-title">제목</label>
                    <input
                        type="text"
                        id="schedule-title"
                        name="title"
                        placeholder="일정 제목"
                        maxLength="80"
                        value={formData.title}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="schedule-description">설명</label>
                    <textarea
                        id="schedule-description"
                        name="commonDescription"
                        placeholder="일정 설명"
                        value={formData.commonDescription}
                        onChange={handleChange}
                    ></textarea>
                </div>

                <div className="form-group">
                    <label htmlFor="schedule-start-date">시작 날짜</label>
                    <input
                        type="date"
                        id="schedule-start-date"
                        name="startDate"
                        value={formData.startDate}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="schedule-end-date">종료 날짜</label>
                    <input
                        type="date"
                        id="schedule-end-date"
                        name="endDate"
                        value={formData.endDate}
                        onChange={handleChange} // 추가된 onChange 핸들러
                    />
                </div>

                <InitializeTimeOptions labelText="희망 여유 시간" selectType="buffer"  handleChange={handleChange}/>

                <div className="form-group">
                    <label htmlFor="schedule-total-amount">목표 달성 총량</label>
                    <input type="text" id="schedule-total-amount" name="totalAmount" placeholder="달성하고 싶은 양" onChange={handleChange}/>
                </div>

                <InitializeTimeOptions labelText="희망 하루 수행시간" selectType="perform" handleChange={handleChange}/>

                <div className="form-group">
                    <label htmlFor="schedule-perform-in-week">희망 주간 수행빈도</label>
                    <select
                        id="schedule-perform-in-week"
                        name="performInWeek"
                        onChange={handleChange}
                    >
                        <option value="1">1회</option>
                        <option value="2">2회</option>
                        <option value="3">3회</option>
                        <option value="4">4회</option>
                        <option value="5">5회</option>
                        <option value="6">6회</option>
                        <option value="7">7회</option>
                    </select>
                </div>
                <div className="form-group">
                    <label htmlFor="include-weekend">주말 포함 여부</label>
                    <div>
                        <label>
                            <input
                                type="checkbox"
                                id="include-saturday"
                                name="includeSaturday"
                                checked={formData.includeSaturday}
                                onChange={handleChange}
                            /> 토요일 포함
                        </label>
                        <label>
                            <input
                                type="checkbox"
                                id="include-sunday"
                                name="includeSunday"
                                checked={formData.includeSunday}
                                onChange={handleChange}
                            /> 일요일 포함
                        </label>
                    </div>
                </div>
                <button type="submit" id="submit-button">
                    저장
                </button>
                <button type="button" id="cancel-btn" onClick={onClose}>
                    취소
                </button>

            </form>
        </div>
    );
}

NormalScheduleForm.propTypes = {
    onClose: PropTypes.func.isRequired,
}

export default NormalScheduleForm;
