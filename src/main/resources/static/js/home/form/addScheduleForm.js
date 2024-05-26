function submitAddScheduleForm(calendar) {

    const commonProps = initCommonProperties();
    const { type, title, startDate, endDate, description } = commonProps;

    let event = {
        title: title,
        description: description,
        type: type
    };

    if (type === 'fixed') {

        event = timeslots.map(slot => {
            return {
                title: title,
                rrule: {
                    freq: slot.frequency,
                    interval: 1,
                    byweekday: slot.days,
                    dtstart:`${startDate}T${slot.startTime}`,
                    until: endDate
                },
                duration: slot.duration,
                description:description,
                color:"#3788d8"
            }
        });

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
            commonDescription:description,
            startDate:startDate,
            endDate:endDate,
            events:events
        }

        console.log(fixedEvents);



        event.forEach(e => calendar.addEvent(e));
        timeslots = [];
        renderTimeslots();
        hideScheduleForm();
    } else {
        // 기존 일반 일정 및 변동 일정 로직 유지
        if (type === 'variable') {
            const variableProperties = initFixAndVariableProperties(startDate);
            const { startDateTime, endDateTime } = variableProperties;

            event.start = startDateTime;
            event.end = endDateTime;

            calendar.addEvent(event);
            addToDoItem(event);
        } else if (type === 'normal') {
            const normalProperties = initFixAndVariableProperties(startDate);
            const { startDateTime, endDateTime } = normalProperties;

            const frequency = document.getElementById('schedule-frequency').value;
            const bufferTime = document.getElementById('schedule-buffer-time').value;
            const weekdays = Array.from(document.querySelectorAll('input[name="weekdays"]:checked')).map(checkbox => checkbox.value);

            event.start = startDateTime;
            event.end = endDateTime;
            event.rrule = {
                freq: frequency.toUpperCase(),
                interval: 1,
                byweekday: weekdays,
                dtstart: startDateTime,
                until: endDateTime
            };
            event.duration = '01:00';
            event.bufferTime = bufferTime;

            calendar.addEvent(event);
            addToDoItem(event);
        }
    }
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
    const description = document.getElementById('schedule-description').value;

    return {
        type,
        title,
        startDate,
        endDate,
        description
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


// //TODO 구현해야함
// function initNormalProperties(){
//     // const startHour = document.getElementById('schedule-start-hour').value;
//     // const startMinute = document.getElementById('schedule-start-minute').value;
//     // const endHour = document.getElementById('schedule-end-hour').value;
//     // const endMinute = document.getElementById('schedule-end-minute').value;
//
//     const startDateTime = convertTo24HourFormat(startDate, startHour, startMinute);
//     const endDateTime = convertTo24HourFormat(endDate, endHour, endMinute);
//
//     return {
//         startDateTime,
//         endDateTime
//     };
// }
//
//
//

