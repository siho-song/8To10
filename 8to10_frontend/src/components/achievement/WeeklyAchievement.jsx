import {useEffect, useState} from "react";
import {Bar} from "react-chartjs-2";
import PropTypes from "prop-types";
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
} from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const WeeklyAchievement = ({year, month, daysInMonth, dates, achievementRates, weeklyAchievementRates}) => {

    const [weeks, setWeeks] = useState([]);
    const [index, setIndex] = useState(0);
    const [limit, setLimit] = useState(0);

    const [increaseButtonDisabled, setIncreaseButtonDisabled] = useState(false);
    const [decreaseButtonDisabled, setDecreaseButtonDisabled] = useState(false);

    const [loading, setLoading] = useState(true);

    const weeksSetting = (daysLength) => {
        const w = [];
        for (let i = 0; i < daysLength; i+=7) {
            if (i + 6 < daysLength) {
                w.push([i, i + 6]);
            } else {
                w.push([i, daysLength - 1]);
            }
        }
        setLimit(w.length);
        return w;
    }

    const increaseIndex = () => {
        if (index + 1 >= limit) {
            return;
        }
        setIndex(index+1);
    }

    const decreaseIndex = () => {
        if (index - 1 < 0) {
            return;
        }
        setIndex(index-1);
    }

    useEffect(() => {
        if (index === 0) {
            setDecreaseButtonDisabled(true);
        } else {
            setDecreaseButtonDisabled(false);
        }

        if (index === limit - 1) {
            setIncreaseButtonDisabled(true);
        } else {
            setIncreaseButtonDisabled(false);
        }
    }, [index]);

    const formatWeeklyDate = () => {
        return `${year}-${month}-${weeks[index][0] + 1} ~ ${year}-${month}-${weeks[index][1] + 1}`;
    }

    const renderHeatmap = () => {
        const daysInMonth = dates.length;
        return (
            <div className="heatmap">
                {[...Array(daysInMonth)].map((_, day) => {
                    const achievement = achievementRates[day];
                    const intensity = achievement ? `rgba(76, 175, 80, ${achievement / 100})` : '#f2f2f2';
                    const textColor = achievement ? '#ffffff' : '#000000';
                    return (
                        <div
                            key={day}
                            className="heatmap-day"
                            style={{
                                backgroundColor: intensity,
                                border: day >= weeks[index][0] && day <= weeks[index][1] ? '2px solid #4caf50' : '2px solid #f2f2f2',
                                color: textColor,
                            }}
                            onClick={()=>handleDateClicked(day)}
                        >
                            {day + 1}
                        </div>
                    );
                })}
            </div>
        );
    };



    const renderBarChart = () => {

        const data = {
            labels: dates,
            datasets: [
                {
                    label: '성취도',
                    data: achievementRates,
                    backgroundColor: achievementRates.map((v, i) =>
                        i >= weeks[index][0] && i <= weeks[index][1] ? 'rgba(76, 175, 80, 0.6)' : 'rgba(200, 200, 200, 0.6)'
                    ),
                    borderColor: achievementRates.map((v, i) =>
                        i >= weeks[index][0] && i <= weeks[index][1]     ? 'rgba(76, 175, 80, 1)' : 'rgba(200, 200, 200, 1)'
                    ),
                    borderWidth: 1
                }
            ]
        };

        const options = {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false,
                    position: 'top'
                },
                tooltip: {
                    callbacks: {
                        label: function (context) {
                            return `성취도: ${context.raw}%`;
                        }
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    max: 100,
                    ticks: {
                        stepSize: 20
                    }
                }
            },
            onClick: (event, elements, chart) => {
                if (elements.length > 0) {
                    handleDateClicked(elements[0].index);
                }
            }
        };

        return <Bar data={data} options={options} />;
    };

    const renderProgressBar = () => {
        if (!achievementRates.length) {
            return <div className="progress-message">아직 일정 달성 정보가 없습니다.</div>;
        }

        return (
            <>
                <div className="achievement-section">
                    <div className="achievement-box-header">
                        <h2>주간 성취도</h2>
                        <div className="date-navigation">
                            <button
                                onClick={decreaseIndex}
                                disabled={decreaseButtonDisabled}
                            >&lt;</button>
                            <span className="achievement-date">{formatWeeklyDate()}</span>
                            <button
                                onClick={increaseIndex}
                                disabled={increaseButtonDisabled}
                            >&gt;</button>
                        </div>
                    </div>
                    <div className="progress-section">

                        <div className="progress-container">
                            <div className="progress-bar">
                                <div className="progress-fill" style={{width: `${weeklyAchievementRates[index]}%`}}>
                                    {weeklyAchievementRates[index]}%
                                    <img src="src/assets/images/penguin-running.gif" alt="running" style={{width: "30px", height: "auto"}}/>
                                </div>
                            </div>
                        </div>
                        <div>
                            달성율 : {weeklyAchievementRates[index]}%
                        </div>
                    </div>
                </div>
            </>

        );
    };

    const handleDateClicked = (idx) => {
        for (let i = 0; i < limit; i++){
            if (weeks[i][0] <= idx && idx <= weeks[i][1]) {
                setIndex(i);
                break;
            }
        }
    }

    useEffect(() => {
        setIndex(0);
        setWeeks(weeksSetting(daysInMonth));
    }, [year, month, daysInMonth]);

    useEffect(() => {
        if (weeks.length > 0) {
            setLoading(false);
        } else {
            setLoading(true);
        }
    }, [weeks]);

    return (
        <>
            {!loading &&
                <>
                    <div className="heatmap-container">
                        {renderHeatmap()}
                    </div>

                    <div className="barplot">
                        {renderBarChart()}
                    </div>

                    <div className="achievement-rate-container">
                        {renderProgressBar()}
                    </div>
                </>
            }
        </>
    );
}

WeeklyAchievement.propTypes = {
    year: PropTypes.number,
    month: PropTypes.number,
    daysInMonth: PropTypes.number,
    dates: PropTypes.array,
    achievementRates: PropTypes.array,
    weeklyAchievementRates: PropTypes.array,
}

export default WeeklyAchievement;

