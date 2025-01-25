import {useEffect, useState} from "react";
import {Bar} from "react-chartjs-2";
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
} from 'chart.js';
import PropTypes from "prop-types";

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const DailyAchievement = ({year, month, daysInMonth, dates, achievementRates}) => {

    const [days, setDays] = useState([]);
    const [index, setIndex] = useState(0);
    const [limit, setLimit] = useState(achievementRates.length);

    const [increaseButtonDisabled, setIncreaseButtonDisabled] = useState(false);
    const [decreaseButtonDisabled, setDecreaseButtonDisabled] = useState(false);

    const [loading, setLoading] = useState(true);


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

    const formatDailyDate = () => {
        return `${year}-${month}-${index + 1}`;
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
                                border: day === index ?  '2px solid #4caf50' : '2px solid #f2f2f2',
                                color: textColor,
                            }}
                            onClick={() => setIndex(day)}
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
                        i === index ? 'rgba(76, 175, 80, 0.6)' : 'rgba(200, 200, 200, 0.6)'
                    ),
                    borderColor: achievementRates.map((v, i) =>
                        i === index ? 'rgba(76, 175, 80, 1)' : 'rgba(200, 200, 200, 1)'
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
                    setIndex(elements[0].index);
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
                        <h2>일간 성취도</h2>
                        <div className="date-navigation">
                            <button className="nav-button"
                                    onClick={decreaseIndex}
                                    disabled={decreaseButtonDisabled}
                            >&lt;</button>
                            <span>{formatDailyDate()}</span>
                            <button className="nav-button"
                                    onClick={increaseIndex}
                                    disabled={increaseButtonDisabled}
                            >&gt;</button>
                        </div>
                    </div>
                    <div className="progress-section">

                        <div className="progress-container">
                            <div className="progress-bar">
                                <div className="progress-fill" style={{width: `${achievementRates[index]}%`}}>
                                    {achievementRates[index]}%
                                    <img src="src/assets/images/penguin-running.gif" alt="running" style={{width: "30px", height: "auto"}}/>
                                </div>
                            </div>
                        </div>
                        <div>
                            달성율 : {achievementRates[index]}%
                        </div>
                    </div>
                </div>
            </>

        );
    };

    useEffect(() => {
        setIndex(0);
        setDays(Array.from({length: achievementRates.length}, (_, i) => i + 1));
        setLimit(achievementRates.length);
    }, [year, month, achievementRates]);

    useEffect(() => {
        if (days.length > 0) {
            setLoading(false);
        } else {
            setLoading(true);
        }
    }, [days]);

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

DailyAchievement.propTypes = {
    year: PropTypes.number,
    month: PropTypes.number,
    daysInMonth: PropTypes.number,
    dates: PropTypes.array,
    achievementRates: PropTypes.array,
}

export default DailyAchievement;