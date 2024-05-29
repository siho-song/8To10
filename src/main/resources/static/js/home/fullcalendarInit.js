document.addEventListener('DOMContentLoaded', function() {
    let timeslots = [];

    const calendarEl = document.getElementById('calendar');
    const calendar = new FullCalendar.Calendar(calendarEl, {
        initialDate: '2024-05-12',
        initialView: 'dayGridMonth',
        nowIndicator: true,
        locale: 'ko',
        headerToolbar: getHeaderToolbarOptions(),
        dayMaxEvents: true,
        // events: loadInitialEvents(),
        events: {},
        eventLimit: 3,
        titleRangeSeparator: ' - ',
        windowResize: () => handleWindowResize(calendar),
        dateClick: (info) => handleDateClick(calendar, info),
        eventClick: (info) => displayEventDetailsInSidebar(info.event),
        eventDidMount: function(info) {
            addToDoItem(info.event);  // 이벤트가 렌더링될 때 To-Do 리스트에 추가
        }
    });
    calendar.render();



    document.getElementById('toggle-add-schedule-btn').addEventListener('click', function() {
        document.getElementById('schedule-type-popup').style.display = 'block';  /* 팝업 표시 */
    });

    document.querySelector('.close-button').addEventListener('click', function() {
        document.getElementById('schedule-type-popup').style.display = 'none';  /* 팝업 숨기기 */
    });

    document.getElementById('schedule-form').addEventListener('submit', function(e) {
        e.preventDefault();
        submitAddScheduleForm(timeslots, calendar);
        toggleFormFields(timeslots);
    });

    document.getElementById('add-timeslot-btn').addEventListener('click', function() {

        createTimeSlot(timeslots); // timeslots 배열을 매개변수로 전달
        renderTimeslots(timeslots);

        console.log(calendar.events);
    });

    const titleInput = document.getElementById('schedule-title');
    const saveBtn = document.getElementById('submit-button');

    // 이름 글자 수 제한 검증
    titleInput.addEventListener('input', function () {
        const titleLength = titleInput.value.length;

        if (titleLength > 80) {
            showTooltip(titleInput, '글자 제한은 80자 입니다.');
            titleInput.value = titleInput.value.substring(0, 80);
        }

        if(document.getElementById('schedule-form').dataset.type === 'fixed') {
            saveBtn.disabled = (titleLength === 0) || timeslots.length === 0;
        } else {
            saveBtn.disabled = (titleLength === 0);
        }
    });

});

function handleWindowResize(calendar) {
    if (window.innerWidth < 768) {
        calendar.changeView('dayGridDay');
    } else if (window.innerWidth >= 768 && window.innerWidth < 1024) {
        calendar.changeView('dayGridWeek');
    } else {
        calendar.changeView('dayGridMonth');
    }
}

function getHeaderToolbarOptions() {
    if (window.innerWidth < 480) {
        return { left: 'prev,next', center: 'title', right: 'dayGridMonth,timeGridDay' };
    } else {
        return { left: 'prev,next today', center: 'title', right: 'dayGridMonth,timeGridWeek,dayGridDay' };
    }
}

function handleDateClick(calendar, info) {
    calendar.changeView('timeGridWeek', info.dateStr);
}

function loadInitialEvents() {
    return [
        { title: 'Business Lunch', start: '2023-01-03T13:00:00', constraint: 'businessHours' },
        { title: 'Business Lunch', start: '2023-01-03T14:00:00', constraint: 'businessHours' },
        { title: 'Business Lunch', start: '2023-01-03T15:00:00', constraint: 'businessHours' },
        { title: 'Business Lunch', start: '2023-01-03T16:00:00', constraint: 'businessHours' },
        { title: 'Business Lunch', start: '2023-01-03T17:00:00', constraint: 'businessHours' },
        { title: 'Business Lunch', start: '2023-01-03T18:00:00', constraint: 'businessHours' },
        {
            title: 'Biweekly Check-in',
            rrule: {
                freq: 'weekly',
                // 반복 주기 ('daily', 'weekly', 'monthly', 'yearly' 등)
                interval: 1,
                // 반복 간격 (예: 매일, 매주, 매월)
                byweekday: 'mo',
                // 'mo', 'tu', 'we', 'th', 'fr', 'sa', 'su'
                dtstart: '2023-01-02T10:00:00',
                //시작 날짜 및 시간
                until: '2023-12-31'
                //반복 종료 날짜
            },
            duration: '01:00'
            // duration: 이벤트 지속 시간
        },
        {
            title: 'Biweekly Check-in',
            rrule: {
                freq: 'WEEKLY',
                interval: 2, // 2주마다
                byweekday: 'FR', // 격주 금요일
                dtstart: '2023-01-06T10:00:00',
                until: '2023-12-31T23:59:59'
            }
        },
        {
            id: '1',
            title: '알바 출근',
            duration: '01:30', // 지속 시간 1시간 30분
            rrule: {
                freq: 'weekly',
                interval: 1,
                byweekday: ['mo'], // 월요일
                dtstart: '2024-05-17T13:00:00', // 시작 시간
                until: '2024-06-30T14:30:00' // 종료 날짜
            },
            description: "asdfaskldnfalksndf"
        },
        {
            id: '2',
            title: '알바 출근',
            duration: '01:30', // 지속 시간 1시간 30분
            rrule: {
                freq: 'weekly',
                interval: 1,
                byweekday: ['th'], // 목요일
                dtstart: '2024-05-17T15:00:00', // 시작 시간
                until: '2024-06-30T16:30:00' // 종료 날짜
            },
            description: "asdfaskldnfalksndf"

        },
        {
            id: 27,
            title : "aefwa",
            start: "2024-07-13T07:00:00",
            end: "2024-07-13T22:00:00",
            commonDescription: "awfewe",
            color: "FIXED",
            parentId: 10,
            type: "fixed",
            completeStatue: false
        }
    ];


}

// To-Do 리스트에 이벤트 추가 함수 -> 로직 잘못되어있음
function addToDoItem(event) {
    const eventDate = new Date(event.start);
    const today = new Date();

    // 이벤트가 오늘의 일정인지 확인
    if (eventDate.toDateString() === today.toDateString()) {
        const todoList = document.getElementById('todo-list');
        const todoItem = document.createElement('div');
        todoItem.className = 'todo-item';

        const eventTitle = document.createElement('span');
        eventTitle.textContent = event.title + ': 하루 수행량';
        todoItem.appendChild(eventTitle);

        const checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkbox.className = 'todo-checkbox';
        checkbox.addEventListener('change', function() {
            if (this.checked) {
                todoItem.classList.add('completed');
            } else {
                todoItem.classList.remove('completed');
            }
        });
        todoItem.appendChild(checkbox);

        todoList.appendChild(todoItem);
    }
}


// TODO 풀캘린더 이벤트를 식별하기 위해 자동으로 id를 부여해주는 로직이 있어야 작동할 수 있음 -> 지금은 event.id가 null로 유지되기 때문에 투두리스트에 반영이 안된다.
// // To-Do 리스트에 오늘 날짜 이벤트 추가 함수
// function addToDoItem(event) {
//     const eventDate = new Date(event.start);
//     const today = new Date();
//
//     // 이벤트가 오늘의 일정인지 확인
//     if (eventDate.toDateString() === today.toDateString()) {
//         const toDoList = document.getElementById('to-do-list');
//
//         // 이벤트 ID가 없으면 고유 ID 할당
//         if (!event.id) {
//             event.id = 'event-' + Math.random().toString(36).substr(2, 9);
//         }
//
//         // 이벤트 ID를 기반으로 중복 체크
//         if (!toDoList.querySelector(`[data-event-id="${event.id}"]`)) {
//             const listItem = document.createElement('li');
//             listItem.className = 'todo-item';
//             listItem.setAttribute('data-event-id', event.id);
//
//             // 체크박스 생성
//             const checkbox = document.createElement('input');
//             checkbox.type = 'checkbox';
//             checkbox.className = 'todo-checkbox';
//             checkbox.addEventListener('change', function() {
//                 if (this.checked) {
//                     listItem.classList.add('completed');
//                 } else {
//                     listItem.classList.remove('completed');
//                 }
//             });
//
//             // 텍스트 노드 생성
//             const text = document.createTextNode(`${event.title} : 하루 수행량`);
//
//             listItem.appendChild(text);
//             listItem.appendChild(checkbox);
//             toDoList.appendChild(listItem);
//         }
//     }
// }
//
// // To-Do 리스트에 기존 이벤트 추가 함수
// function addExistingToDoItem(event) {
//     const eventDate = new Date(event.start);
//     const today = new Date();
//
//     // 이벤트가 오늘의 일정인지 확인
//     if (eventDate.toDateString() === today.toDateString()) {
//         const toDoList = document.getElementById('to-do-list');
//
//         // 이벤트 ID가 없으면 고유 ID 할당
//         if (!event.id) {
//             event.id = 'event-' + Math.random().toString(36).substr(2, 9);
//         }
//
//         // 이벤트 ID를 기반으로 중복 체크
//         if (!toDoList.querySelector(`[data-event-id="${event.id}"]`)) {
//             const listItem = document.createElement('li');
//             listItem.className = 'todo-item';
//             listItem.setAttribute('data-event-id', event.id);
//
//             // 체크박스 생성
//             const checkbox = document.createElement('input');
//             checkbox.type = 'checkbox';
//             checkbox.className = 'todo-checkbox';
//             checkbox.addEventListener('change', function() {
//                 if (this.checked) {
//                     listItem.classList.add('completed');
//                 } else {
//                     listItem.classList.remove('completed');
//                 }
//             });
//
//             // 텍스트 노드 생성
//             const text = document.createTextNode(`${event.title} : 하루 수행량`);
//
//             listItem.appendChild(text);
//             listItem.appendChild(checkbox);
//             toDoList.appendChild(listItem);
//         }
//     }
// }