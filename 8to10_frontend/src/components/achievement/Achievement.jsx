import { useEffect, useState } from 'react';
import authenticatedApi from '@/api/AuthenticatedApi.js';
import { API_ENDPOINT_NAMES } from '@/constants/ApiEndPoints.js';
import { getMonth } from '@/constants/AchievementOptions.js';
import '@/styles/achievement/Achievement.css';
import WeeklyAchievement from "@/components/achievement/WeeklyAchievement.jsx";
import DailyAchievement from "@/components/achievement/DailyAchievement.jsx";

const Achievement = () => {
    const nickname = localStorage.getItem('nickname');

    const currentDate = new Date();
    const currentYear = currentDate.getFullYear();
    const currentMonth = currentDate.getMonth() + 1;

    const [viewMode, setViewMode] = useState('weekly');

    const [year, setYear] = useState(currentYear);
    const [month, setMonth] = useState(currentMonth);

    const [achievementRates, setAchievementRates] = useState([]);
    const [weeklyAchievementRates, setWeeklyAchievementRates] = useState([]);
    const [monthlyAchievementRate, setMonthlyAchievementRate] = useState(0);

    const [dates, setDates] = useState([]);
    const [daysInMonth, setDaysInMonth] = useState(0);

    const [increaseButtonDisabled, setIncreaseButtonDisabled] = useState(false);

    const [loading, setLoading] = useState(true);

    const increaseMonth = () => {
        if (month === 12) {
            setMonth(1);
            setYear(year + 1);
        } else {
            setMonth(month + 1);
        }
    }

    const decreaseMonth = () => {
        if (month === 1) {
            setMonth(12);
            setYear(year - 1);
        } else {
            setMonth(month - 1);
        }
    }

    const loadAchievements = async (y, m) => {
        if (y === 0 && m === 0) {
            return;
        }

        const url = `/achievement/${y}/${m}`;
        const response = await authenticatedApi.get(
            url,
            { apiEndPoint: API_ENDPOINT_NAMES.GET_ACHIEVEMENTS }
        );

        const datesLength = getMonth(y, m).length;
        const achievements = Array(datesLength).fill(0);
        if (response.data.items.length > 0) {
            for (let date of response.data.items) {
                const d = parseInt(date.achievementDate.split('-')[2]);
                achievements[d - 1] = Math.floor(date.achievementRate * 100);
            }
        }

        setAchievementRates(achievements);
        setDaysInMonth(datesLength);
        setDates(getMonth(y, m));

        const {monthlyOutput, weeklyOutput} = calculateAchievementRates(achievements);
        setMonthlyAchievementRate(monthlyOutput);
        setWeeklyAchievementRates(weeklyOutput);

        return response.data.items;
    };

    const calculateAchievementRates = (achievements) => {
        let monthlySum = 0;
        let weeklySum = 0;

        let monthlyOutput;

        let weeklyCount = 0;
        const weeklyOutput = [];

        for (let i = 0; i < achievements.length; i++) {
            monthlySum += achievements[i];
            weeklySum += achievements[i];
            weeklyCount++;
            if ((i > 0 && i % 7 === 6) || i === achievements.length - 1) {
                weeklyOutput.push(Math.floor(weeklySum / weeklyCount));
                weeklySum = 0;
                weeklyCount = 0;
            }
        }
        monthlyOutput = Math.floor(monthlySum / achievements.length);

        return {monthlyOutput, weeklyOutput};
    }

    useEffect(() => {

        if (year >= currentYear && month > currentMonth) {
            setYear(currentYear);
            setMonth(currentMonth);
            return;
        }

        const loadData = async (y, m) => {
            await loadAchievements(y, m);
        };

        loadData(year, month);

        if (currentYear === year && currentMonth === month) {
            setIncreaseButtonDisabled(true);
        } else {
            setIncreaseButtonDisabled(false);
        }
    }, [year, month]);

    useEffect(() => {
        if (achievementRates.length > 0) {
            setLoading(false);
        } else {
            setLoading(true);
        }
    }, [achievementRates]);

    return (
        <>

            <div className="achievement-container">
                <div className="top-controls">
                    <div>
                        <button onClick={() => setViewMode('weekly')}
                                className={"weekly-btn" + (viewMode === 'weekly' ? ' active' : '')}>주간
                        </button>
                        <button onClick={() => setViewMode('daily')}
                                className={"daily-btn" + (viewMode === 'daily' ? ' active' : '')}>일간
                        </button>
                    </div>
                    <div className="y-m-navigation">
                        <button className="y-m-nav-btn"
                                onClick={decreaseMonth}>&lt;</button>
                        <select className="custom-select-year" onChange={(e) => setYear(Number(e.target.value))}
                                value={year}>
                            {Array.from({length: 10}, (_, i) => currentYear - i).map(yr => <option key={yr}
                                                                                                   value={yr}>{yr}</option>)}
                        </select>
                        <select className="custom-select-month" onChange={(e) => setMonth(Number(e.target.value))}
                                value={month}>
                            {/*{[...Array(12)].map((_, idx) => (*/}
                            {/*    <option key={idx} value={idx + 1}>{idx + 1}월</option>*/}
                            {/*))}*/}
                            {currentYear === year
                                ? [...Array(currentMonth)].map((_, idx) => (
                                    <option key={idx} value={idx + 1}>
                                        {idx + 1}월
                                    </option>
                                ))
                                : [...Array(12)].map((_, idx) => (
                                    <option key={idx} value={idx + 1}>
                                        {idx + 1}월
                                    </option>
                                ))}
                        </select>
                        <button className="y-m-nav-btn"
                                onClick={increaseMonth}
                                disabled={increaseButtonDisabled}
                        >&gt;</button>
                    </div>
                </div>

                <div className="achievement-section">
                    <h2>{nickname} 님의 월간 성취도 점수는 <strong>{monthlyAchievementRate}점</strong> 입니다.</h2>
                </div>

                {!loading ? (viewMode === "weekly" ? (
                    <WeeklyAchievement
                        year={year}
                        month={month}
                        daysInMonth={daysInMonth}
                        dates={dates}
                        achievementRates={achievementRates}
                        weeklyAchievementRates={weeklyAchievementRates}
                    />
                ) : (
                    <DailyAchievement
                        year={year}
                        month={month}
                        daysInMonth={daysInMonth}
                        dates={dates}
                        achievementRates={achievementRates}
                    />
                )) : (
                    <div><p>로딩중 ...</p></div>
                )}
            </div>
        </>
    );
};

export default Achievement;
