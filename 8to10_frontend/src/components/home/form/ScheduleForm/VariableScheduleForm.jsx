import {
    InitializeTimeOptionsWithPeriod
} from "@/components/home/form/ScheduleTimeUtils/TimeOptions.jsx";
import {useState} from "react";
import {
    convertPeriodTimeToLocalTimeFormat,
} from "@/components/home/form/ScheduleTimeUtils/TimeUtils.jsx";
import {useCalendar} from "@/components/context/FullCalendarContext.jsx";
import PropTypes from "prop-types";

function VariableScheduleForm({ onClose }) {

    const { addEvent } = useCalendar();
    const [formData, setFormData] = useState({
        title: '',
        commonDescription: '',
        startDate: '',
        endDate: '',
        startTime: 'AM',
        startHour: '1',
        startMinute: '0',
        endTime: 'AM',
        endHour: '1',
        endMinute: '0',
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

        const startDateTime = formData.startDate +'T'+ convertPeriodTimeToLocalTimeFormat(
            formData.startTime,
            formData.startHour,
            formData.startMinute
        );

        const endDateTime = formData.endDate + 'T' + convertPeriodTimeToLocalTimeFormat(
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
            const response = await fetch('/api/schedule/variable', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(finalData),
            });

            if (!response.ok) {
                throw new Error('서버와의 통신에 실패했습니다.');
            } else {
                const data = await response.json();
                console.log("new event : ", data);
                addEvent(data);
                onClose();
            }
        } catch (error) {
            console.error('폼 제출 중 오류:', error);
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
