function submitAddScheduleForm(calendar) {
    // 공통 입력에 대한 처리
    const commonProps = initCommonProperties();
    const { type, title, startDate, endDate, commonDescription } = commonProps;

    let event = {
        title: title,
        commonDescription: commonDescription,
    };

    if (type === 'variable') {
        const variableProperties = initFixAndVariableProperties(startDate, endDate);
        const { startDateTime, endDateTime } = variableProperties;
        a = 10;

        event.start = startDateTime;
        event.end = endDateTime;

        console.log(event);
        // 서버로 이벤트 객체 전송
        fetch('http://localhost:8080/schedule/variable', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(event)
        })
            .then(response => response.json())
            .then(data => {
                console.log('Success:', data);
                calendar.addEvent(data);
            })
            .catch((error) => {
                console.error('Error:', error);
            });

        console.log(event)

    }

    else if (type === 'fixed') {

        const events = timeslots.map(slot => {
            return {
                startTime: slot.startTime,
                duration: `${slot.duration}:00`,
                frequency: slot.frequency,
                days: slot.days
            };
        });
        const fixedEvents = {
            title:title,
            commonDescription:commonDescription,
            startDate:startDate,
            endDate:endDate,
            events:events
        }
        console.log(fixedEvents);


        fetch('http://localhost:8080/schedule/fixed', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(fixedEvents)
        })
            .then(response => response.json())
            .then(data => {
                console.log('Success:', data);
                if (data.events && typeof data.events === 'object') {
                    Object.keys(data.events).forEach(key => {
                        const e = data.events[key];
                        calendar.addEvent(e);
                        console.log(e);
                    });
                } else {
                    console.error('Expected an object but got:', data);
                }
            })
            .catch((error) => {
                console.error('Error:', error);
            });

    }

    else if (type === 'normal') {

        const bufferHour = document.getElementById('schedule-buffer-hour').value;
        const bufferMinute = document.getElementById('schedule-buffer-minute').value;
        const performHour = document.getElementById('schedule-perform-hour').value;
        const performMinute = document.getElementById('schedule-perform-minute').value;

        const bufferTime = `${bufferHour}:${bufferMinute}:00`;
        const totalAmount = document.getElementById('schedule-total-amount').value;
        const performInDay = `${performHour}:${performMinute}:00`;
        const performInWeek = document.getElementById('schedule-perform-in-week').value;
        const isIncludeSaturday = document.getElementById('include-saturday').checked;
        const isIncludeSunday = document.getElementById('include-sunday').checked;

        const event = {
            title: title,
            commonDescription: commonDescription,
            startDate: startDate,
            endDate: endDate,
            bufferTime: bufferTime,
            totalAmount: totalAmount,
            performInDay: performInDay,
            performInWeek: performInWeek,
            isIncludeSaturday: isIncludeSaturday,
            isIncludeSunday: isIncludeSunday
        };

        console.log(event);

        fetch('http://localhost:8080/schedule/normal', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(event)
        })
            .then(response => response.json())
            .then(data => {
                if (data.events && typeof data.events === 'object') {
                    Object.keys(data.events).forEach(key => {
                        const e = data.events[key];
                        calendar.addEvent(e);
                        console.log(e);
                    });
                } else {
                    console.error('Expected an object but got:', data);
                }
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    }
    document.getElementById('schedule-form-header').textContent = "";

    hideScheduleForm();
}

function hideScheduleForm() {
    document.getElementById('schedule-form-container').style.display = 'none';
    document.getElementById('schedule-form').reset();
}

function initCommonProperties() {
    const form = document.getElementById('schedule-form');
    const type = form.dataset.type; //"fixed", "variable", "normal"
    const title = document.getElementById('schedule-title').value;
    const startDate = document.getElementById('schedule-start-date').value;
    const endDate = document.getElementById('schedule-end-date').value;
    const commonDescription = document.getElementById('schedule-description').value;

    return {
        type,
        title,
        startDate,
        endDate,
        commonDescription
    };
}

function initFixAndVariableProperties(startDate, endDate) {
    const startHour = document.getElementById('schedule-start-hour').value;
    const startMinute = document.getElementById('schedule-start-minute').value;
    const endHour = document.getElementById('schedule-end-hour').value;
    const endMinute = document.getElementById('schedule-end-minute').value;

    const startDateTime = convertTo24HourFormat(startDate, startHour, startMinute);
    const endDateTime = convertTo24HourFormat(endDate, endHour, endMinute);

    return {
        startDateTime,
        endDateTime
    };
}