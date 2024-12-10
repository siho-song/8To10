import {
    InitializeTimeOptionsWithPeriod
} from "@/components/home/form/ScheduleTimeUtils/TimeOptions.jsx";
import {useState} from "react";
import {
    formatPeriodTimeToLocalTimeFormat,
} from "@/helpers/TimeFormatter.js";
import {useCalendar} from "@/context/fullCalendar/UseCalendar.jsx";
import PropTypes from "prop-types";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {formatVariableSchedule} from "@/helpers/ScheduleFormatter.js";

function VariableScheduleForm({ onClose }) {

    const { addEvent } = useCalendar();

    const today = new Date();

    const [formData, setFormData] = useState({
        title: '',
        commonDescription: '',
        startDate: `${today.toISOString().split('T')[0]}`,
        endDate: `${new Date(today.getTime() + 24 * 60 * 60 * 1000).toISOString().split('T')[0]}`,
        startTime: 'AM',
        startHour: 1,
        startMinute: 0,
        endTime: 'AM',
        endHour: 1,
        endMinute: 0,
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value
        }));
    };

    const handleSubmit =  async (e) => {

        e.preventDefault();

        const startDateTime = formData.startDate +'T'+ formatPeriodTimeToLocalTimeFormat(
            formData.startTime,
            formData.startHour,
            formData.startMinute
        );

        const endDateTime = formData.endDate + 'T' + formatPeriodTimeToLocalTimeFormat(
            formData.endTime,
            formData.endHour,
            formData.endMinute
        );

        const finalData = {
            title: formData.title,
            commonDescription: formData.commonDescription,
            start: startDateTime,
            end: endDateTime
        };

        try {
            const url = '/schedule/variable';
            const response = await authenticatedApi.post(
                url,
                finalData,
                {
                    apiEndPoint: API_ENDPOINT_NAMES.CREATE_V_SCHEDULE,
            });
            const data = response.data;
            const formattedEvent = formatVariableSchedule(data);
            addEvent(formattedEvent);
            onClose();

        } catch (error) {
            console.error(error.toString());
            console.error(error);
        }
    };

    return (
        <div id="schedule-form-container" style={{padding: '20px'}}>
            <h2 id="schedule-form-header">변동 일정 생성</h2>
            <form id="schedule-form" data-type="variable" onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="schedule-title">제목</label>
                    <input
                        type="text"
                        id="schedule-title"
                        name="title"
                        placeholder="일정 제목"
                        maxLength="80"
                        value={formData.title}
                        onChange={handleChange} // 추가된 onChange 핸들러
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="schedule-description">설명</label>
                    <textarea
                        id="schedule-description"
                        name="commonDescription"
                        placeholder="일정 설명"
                        value={formData.commonDescription}
                        onChange={handleChange} // 추가된 onChange 핸들러
                    ></textarea>
                </div>

                <div className="form-group">
                    <label htmlFor="schedule-start-date">시작 날짜</label>
                    <input
                        type="date"
                        id="schedule-start-date"
                        name="startDate"
                        value={formData.startDate}
                        onChange={handleChange} // 추가된 onChange 핸들러
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

                <InitializeTimeOptionsWithPeriod
                    labelText="시작 시간"
                    selectType="start"
                    handleChange={handleChange}
                />

                <InitializeTimeOptionsWithPeriod
                    labelText="종료 시간"
                    selectType="end"
                    handleChange={handleChange}
                />

                <button type="submit" id="submit-button">
                    저장
                </button>
                <button type="button" id="cancel-btn" onClick={ onClose }>
                취소
            </button>
            </form>
        </div>
    );
}

VariableScheduleForm.propTypes = {
    onClose: PropTypes.func.isRequired,
}

export default VariableScheduleForm;
