import { useEffect, useState } from 'react';

import { InitializeTimeOptions, InitializeTimeOptionsWithPeriod } from "../ScheduleTimeUtils/TimeOptions.jsx";
import {
    convertPeriodTimeToLocalTimeFormat,
    convertToDuration
} from "@/components/home/form/ScheduleTimeUtils/TimeUtils.jsx"
import { useCalendar } from "@/components/context/FullCalendarContext.jsx";

import "@/styles/home/scheduleForm.css";
import PropTypes from "prop-types";

import * as Yup from 'yup';

const validationSchema = Yup.object().shape({
    title: Yup.string()
        .required('일정 제목을 입력해주세요.(1자~80자)')
        .min(1, '일정 제목을 입력해주세요.(1자~80자)')
        .max(80, '일정 제목을 입력해주세요.(1자~80자)'),
    startDate: Yup.date()
        .required('시작 날짜가 필요합니다')
        .max(Yup.ref('endDate'), '시작 날짜는 종료 날짜 이전이어야 합니다.'),
    endDate: Yup.date()
        .required('종료 날짜가 필요합니다')
        .min(Yup.ref('startDate'), '종료 날짜는 시작 날짜 이후여야 합니다'),
    days: Yup.array()
        .min(1, '최소 하나의 요일을 선택해야 합니다'),
})
    .test('duration', '지속 시간은 0시간 0분 이상이어야 합니다.', (values) => {
        const {durationHour, durationMinute} = values;
        return durationHour > 0 || durationMinute > 0;
    })
;

function FixedScheduleForm({ onClose }) {

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
        durationHour: 0,
        durationMinute: 0,
        frequency: 'daily',
        days: [],
    });
    const [errors, setErrors] = useState({});
    const [isFormValid, setIsFormValid] = useState(false);

    const validateField = async (name, value) => {
        try {
            await validationSchema.validateAt(name, { ...formData, [name]: value });
            setErrors(prevErrors => ({ ...prevErrors, [name]: '' }));
        } catch (error) {
            setErrors(prevErrors => ({ ...prevErrors, [name]: error.message }));
        }
    };

    const validateForm = async () => {
        try {
            console.log("폼 검증 로직 시작");
            await validationSchema.validate(formData, { abortEarly: false });
            console.log("폼 검증 로직 끝");
            setErrors({});
            console.log("폼 검증 로직 세팅");
            setIsFormValid(true);
        } catch (error) {
            const newErrors = error.inner.reduce((acc, err) => {
                acc[err.path] = err.message;
                return acc;
            }, {});
            setErrors(newErrors);
            setIsFormValid(false);
        }
    };

    useEffect(() => {
        handleFrequencyChange({ target: { value: formData.frequency } });
    }, [formData.frequency]);

    const handleFrequencyChange = (e) => {
        const { value } = e.target;
        setFormData((prevData) => ({ ...prevData, frequency: value }));

        const weekdayCheckboxes = document.querySelectorAll('input[name="days"]');

        if (value === 'daily') {
            weekdayCheckboxes.forEach((checkbox) => {
                checkbox.checked = true;
                checkbox.disabled = true;
            });

            setFormData((prevData) => ({
                ...prevData,
                days: Array.from(weekdayCheckboxes).map((checkbox) => checkbox.value),
            }));
        } else {
            weekdayCheckboxes.forEach((checkbox) => {
                checkbox.checked = false;
                checkbox.disabled = false;
            });

            setFormData((prevData) => ({ ...prevData, days: [] }));
        }
    };

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;

        if (type === 'checkbox') {
            setFormData((prevData) => ({
                ...prevData,
                days: checked ? [...prevData.days, value] : prevData.days.filter((day) => day !== value),
            }));
        } else {
            setFormData({
                ...formData,
                [name]: value,
            });
        }
        validateField(name, type === 'checkbox' ? formData.days : value);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        await validateForm();
        if (!isFormValid) return;

        const finalData = {
            title: formData.title,
            commonDescription: formData.commonDescription,
            startDate: formData.startDate,
            endDate: formData.endDate,
            startTime: convertPeriodTimeToLocalTimeFormat(formData.startTime, formData.startHour, formData.startMinute),
            duration: convertToDuration(formData.durationHour, formData.durationMinute),
            frequency: formData.frequency,
            days: formData.days,
        };

        try {
            const accessToken = localStorage.getItem('authorization');
            const response = await fetch('/api/schedule/fixed', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'authorization': `Bearer ${accessToken}`,
                },
                body: JSON.stringify(finalData),
            });

            if (!response.ok) {
                throw new Error('서버와의 통신에 실패했습니다.');
            } else {
                const data = await response.json();
                data.items.forEach(event => {
                    console.log("new event : ", event);
                    addEvent(event);
                });
                onClose();
            }
        } catch (error) {
            console.error('폼 제출 중 오류:', error);
        }
    };

    return (
        <div id="schedule-form-container" style={{padding: '20px'}}>
            <h2 id="schedule-form-header">고정 일정 생성</h2>
            <form id="schedule-form" data-type="fixed" onSubmit={handleSubmit}>
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
                        onBlur={(e) => validateField(e.target.name, e.target.value)}
                    />
                    {errors.title && <p className="error-message">{errors.title}</p>}
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
                        onChange={handleChange}
                        onBlur={(e) => validateField(e.target.name, e.target.value)}
                    />
                    {errors.startDate && <p className="error-message">{errors.startDate}</p>}
                </div>

                <div className="form-group">
                    <label htmlFor="schedule-end-date">종료 날짜</label>
                    <input
                        type="date"
                        id="schedule-end-date"
                        name="endDate"
                        value={formData.endDate}
                        onChange={handleChange}
                    />
                </div>

                <InitializeTimeOptionsWithPeriod
                    labelText="시작 시간"
                    selectType="start"
                    handleChange={handleChange}
                />
                <InitializeTimeOptions
                    labelText="지속 시간"
                    selectType="duration"
                    handleChange={handleChange}
                />

                <div className="form-group">
                    <label htmlFor="schedule-frequency">빈도</label>
                    <select
                        id="schedule-frequency"
                        name="frequency"
                        value={formData.frequency}
                        onChange={handleFrequencyChange}
                    >
                        <option value="daily">매일</option>
                        <option value="weekly">매주</option>
                    </select>
                </div>

                <div className="form-group">
                    <label>수행 요일</label>
                    <div className="weekday-checkbox-group">
                        {['mo', 'tu', 'we', 'th', 'fr', 'sa', 'su'].map((day, index) => (
                            <label key={day} htmlFor={`weekday-${day}`}>
                                <input
                                    type="checkbox"
                                    id={`weekday-${day}`}
                                    name="days"
                                    value={day}
                                    checked={formData.days.includes(day)}
                                    onChange={handleChange}
                                    onBlur={() => validateField('days', formData.days)}
                                    disabled={formData.frequency === 'daily'}
                                />
                                {['월', '화', '수', '목', '금', '토', '일'][index]}
                            </label>
                        ))}
                    </div>
                    {errors.days && <p className="error-message">{errors.days}</p>}
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

FixedScheduleForm.propTypes = {
    onClose: PropTypes.func.isRequired,
}

export default FixedScheduleForm;
