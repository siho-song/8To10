document.addEventListener('DOMContentLoaded', function() {
    // 더미 데이터
    const dummyData = {
        "daily": {
            "page": 75,
            "lecture": 50,
            "chapter": 90,
            "exercise": 60,
            "project": 80,
            "none": 100
        },
        "weekly": {
            "page": 60,
            "lecture": 70,
            "chapter": 85,
            "exercise": 55,
            "project": 90,
            "none": 100
        },
        "monthly": {
            "page": 50,
            "lecture": 65,
            "chapter": 80,
            "exercise": 70,
            "project": 75,
            "none": 95
        }
    };

    // 성취도 업데이트 함수
    function updateProgressBars(containerId, data) {
        const progressBars = document.getElementById(containerId);
        progressBars.innerHTML = `
            <div class="progress-category">
                <div class="category-title">페이지 수</div>
                <div class="progress-bar">
                    <div class="progress-fill" style="width: ${data.page}%;"></div>
                </div>
                <div class="percentage">${data.page}%</div>
            </div>
            <div class="progress-category">
                <div class="category-title">강의 수</div>
                <div class="progress-bar">
                    <div class="progress-fill" style="width: ${data.lecture}%;"></div>
                </div>
                <div class="percentage">${data.lecture}%</div>
            </div>
            <div class="progress-category">
                <div class="category-title">챕터 수</div>
                <div class="progress-bar">
                    <div class="progress-fill" style="width: ${data.chapter}%;"></div>
                </div>
                <div class="percentage">${data.chapter}%</div>
            </div>
            <div class="progress-category">
                <div class="category-title">운동</div>
                <div class="progress-bar">
                    <div class="progress-fill" style="width: ${data.exercise}%;"></div>
                </div>
                <div class="percentage">${data.exercise}%</div>
            </div>
            <div class="progress-category">
                <div class="category-title">프로젝트</div>
                <div class="progress-bar">
                    <div class="progress-fill" style="width: ${data.project}%;"></div>
                </div>
                <div class="percentage">${data.project}%</div>
            </div>
            <div class="progress-category">
                <div class="category-title">해당 없음</div>
                <div class="progress-bar">
                    <div class="progress-fill" style="width: ${data.none}%;"></div>
                </div>
                <div class="percentage">${data.none}%</div>
            </div>
        `;
    }

    // 각 성취도 페이지 표시 함수
    function showDailyAchievement() {
        const achievementContent = document.getElementById('achievement-content');
        achievementContent.innerHTML = `
            <div id="daily-container">
                <h2>일간 성취도 상세</h2>
                <div id="daily-progress-bars"></div>
            </div>
        `;
        updateProgressBars('daily-progress-bars', dummyData.daily);
    }

    function showWeeklyAchievement() {
        const achievementContent = document.getElementById('achievement-content');
        achievementContent.innerHTML = `
            <div id="weekly-container">
                <h2>주간 성취도 상세</h2>
                <div id="weekly-progress-bars"></div>
            </div>
        `;
        updateProgressBars('weekly-progress-bars', dummyData.weekly);
    }

    function showMonthlyAchievement() {
        const achievementContent = document.getElementById('achievement-content');
        achievementContent.innerHTML = `
            <div id="monthly-container">
                <h2>월간 성취도 상세</h2>
                <div id="monthly-progress-bars"></div>
            </div>
        `;
        updateProgressBars('monthly-progress-bars', dummyData.monthly);
    }

    // 이벤트 리스너 등록
    document.getElementById('daily-achievement').addEventListener('click', showDailyAchievement);
    document.getElementById('weekly-achievement').addEventListener('click', showWeeklyAchievement);
    document.getElementById('monthly-achievement').addEventListener('click', showMonthlyAchievement);

    // 초기 로드 설정
    showDailyAchievement();
});

document.addEventListener('DOMContentLoaded', () => {
    showAchievement('score');

    // 사이드바 메뉴 클릭 이벤트 추가
    document.querySelectorAll('.achievement-menu li').forEach(item => {
        item.addEventListener('click', () => {
            const type = item.getAttribute('data-type');
            showAchievement(type);
        });
    });
});

function showAchievement(type) {
    const content = document.getElementById('achievement-content');
    const userName = 'USER_NAME';
    const userScore = 84; // 서버에서 받아온 사용자 점수
    const runningGifPath = '/images/running.gif';
    let currentDate = new Date();

    if (type === 'score') {
        content.innerHTML = generateScoreContent(userName, userScore, currentDate, runningGifPath);
    } else {
        content.innerHTML = generateAchievementContent(type, currentDate, runningGifPath);
    }
}

function generateScoreContent(userName, userScore, currentDate, runningGifPath) {
    return `
        <h2>(${userName}) 님의 점수는 <strong>${userScore}점</strong> 입니다.</h2>
        ${generateAchievementContent('daily', currentDate, runningGifPath)}
        ${generateAchievementContent('weekly', currentDate, runningGifPath)}
        ${generateAchievementContent('monthly', currentDate, runningGifPath)}
    `;
}

function generateAchievementContent(type, currentDate, runningGifPath) {
    const date = getFormattedDate(type, currentDate);
    const achievementRate = calculateAchievement(type, currentDate);

    return `
        <div class="achievement-section">
            <div class="achievement-box-header">
                <h3>${getAchievementTitle(type)}</h3>
                <div class="date-navigation">
                    <button class="nav-button" data-type="${type}" data-offset="-1" onclick="changeDate('${type}', -1, event)">&lt;</button>
                    <span id="${type}-date" data-date="${currentDate.toISOString()}">${date}</span>
                    <button class="nav-button" data-type="${type}" data-offset="1" onclick="changeDate('${type}', 1, event)">&gt;</button>
                </div>
            </div>
            
            <div class="progress-container">
                <div class="progress-bar">
                    <div class="progress-fill" style="width: ${achievementRate}%">
                         <div class="progress-gif-container" style="left: calc(${achievementRate}% - 15px);">
                            <img src="${runningGifPath}" class="progress-gif" alt="Progress GIF">
                        </div>
                        ${achievementRate}%
                    </div>
                </div>
            </div>
        </div>
    `;
}

function getAchievementTitle(type) {
    switch(type) {
        case 'daily':
            return '일간 성취도';
        case 'weekly':
            return '주간 성취도';
        case 'monthly':
            return '월간 성취도';
        default:
            return '';
    }
}

function getFormattedDate(type, currentDate) {
    const year = currentDate.getFullYear();
    const month = currentDate.getMonth() + 1;
    const day = currentDate.getDate();

    if (type === 'daily') {
        return `${year}년 ${month}월 ${day}일`;
    } else if (type === 'weekly') {
        const firstDayOfWeek = new Date(currentDate);
        firstDayOfWeek.setDate(firstDayOfWeek.getDate() - firstDayOfWeek.getDay() + 1); // assuming Monday as the start of the week
        const lastDayOfWeek = new Date(firstDayOfWeek);
        lastDayOfWeek.setDate(firstDayOfWeek.getDate() + 6);
        return `${firstDayOfWeek.toLocaleDateString('ko-KR')} - ${lastDayOfWeek.toLocaleDateString('ko-KR')}`;
    } else if (type === 'monthly') {
        return `${year}년 ${month}월`;
    }

    return '';
}

function calculateAchievement(type, currentDate) {
    const data = generateDummyData(); // 더미 데이터 생성 함수 호출

    let formattedDate;
    if (type === 'daily') {
        formattedDate = currentDate.toISOString().split('T')[0]; // 'YYYY-MM-DD' 형식으로 변환
    } else if (type === 'weekly') {
        const startOfWeek = new Date(currentDate);
        startOfWeek.setDate(currentDate.getDate() - currentDate.getDay() + 1); // assuming Monday as the start of the week
        formattedDate = startOfWeek.toISOString().split('T')[0];
    } else if (type === 'monthly') {
        formattedDate = `${currentDate.getFullYear()}-${String(currentDate.getMonth() + 1).padStart(2, '0')}`;
    }

    const filteredData = data[type].filter(item => item.date === formattedDate);
    const totalAchievement = filteredData.reduce((sum, item) => sum + item.achievement, 0) / filteredData.length || 0;

    return totalAchievement.toFixed(1);
}

function generateDummyData() {
    const data = { daily: [], weekly: [], monthly: [] };

    // Daily data
    for (let i = 1; i <= 30; i++) {
        const date = `2024-05-${String(i).padStart(2, '0')}`;
        data.daily.push({ date, achievement: Math.floor(Math.random() * 101) });
    }

    // Weekly data
    const weeklyDates = ['2024-04-29', '2024-05-06', '2024-05-13', '2024-05-20', '2024-05-27'];
    weeklyDates.forEach(date => {
        data.weekly.push({ date, achievement: Math.floor(Math.random() * 101) });
    });

    // Monthly data
    for (let i = 1; i <= 5; i++) {
        const date = `2024-${String(i).padStart(2, '0')}`;
        data.monthly.push({ date, achievement: Math.floor(Math.random() * 101) });
    }

    return data;
}

function changeDate(type, offset, event) {
    event.preventDefault();
    const currentDateElement = document.querySelector(`#${type}-date`);
    let currentDate = new Date(currentDateElement.dataset.date);

    if (type === 'daily') {
        currentDate.setDate(currentDate.getDate() + offset);
    } else if (type === 'weekly') {
        currentDate.setDate(currentDate.getDate() + (7 * offset));
    } else if (type === 'monthly') {
        currentDate.setMonth(currentDate.getMonth() + offset);
    }

    currentDateElement.dataset.date = currentDate.toISOString();
    currentDateElement.innerHTML = getFormattedDate(type, currentDate);

    const runningGifPath = '/images/running.gif';
    const newContent = generateAchievementContent(type, currentDate, runningGifPath);
    currentDateElement.closest('.achievement-section').outerHTML = newContent;
}
