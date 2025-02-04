export const MONTH = {
    JAN: Array.from({ length: 31 }, (_, i) => `${i + 1}일`),
    FEB: (year) => Array.from({ length: isLeapYear(year) ? 29 : 28 }, (_, i) => `${i + 1}일`),
    MAR: Array.from({ length: 31 }, (_, i) => `${i + 1}일`),
    APR: Array.from({ length: 30 }, (_, i) => `${i + 1}일`),
    MAY: Array.from({ length: 31 }, (_, i) => `${i + 1}일`),
    JUN: Array.from({ length: 30 }, (_, i) => `${i + 1}일`),
    JUL: Array.from({ length: 31 }, (_, i) => `${i + 1}일`),
    AUG: Array.from({ length: 31 }, (_, i) => `${i + 1}일`),
    SEP: Array.from({ length: 30 }, (_, i) => `${i + 1}일`),
    OCT: Array.from({ length: 31 }, (_, i) => `${i + 1}일`),
    NOV: Array.from({ length: 30 }, (_, i) => `${i + 1}일`),
    DEC: Array.from({ length: 31 }, (_, i) => `${i + 1}일`),
};

export const getMonth = (year, month) => {
    if (month === 1) {
        return MONTH.JAN;
    } else if (month ===  2) {
        return MONTH.FEB(year);
    } else if (month === 3) {
        return MONTH.MAR;
    } else if (month === 4) {
        return MONTH.APR;
    } else if (month === 5) {
        return MONTH.MAY;
    } else if (month === 6) {
        return MONTH.JUN;
    } else if (month === 7) {
        return MONTH.JUL;
    } else if (month === 8) {
        return MONTH.AUG;
    } else if (month === 9) {
        return MONTH.SEP;
    } else if (month === 10) {
        return MONTH.OCT;
    } else if (month === 11) {
        return MONTH.NOV;
    } else if (month === 12) {
        return MONTH.DEC;
    }
}

function isLeapYear(year) {
    return (year % 4 === 0 && year % 100 !== 0) || year % 400 === 0;
}