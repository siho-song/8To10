document.addEventListener('DOMContentLoaded', () => {
    showAchievement('score');
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

