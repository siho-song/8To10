function setDefaultDate() {
    var today = new Date().toISOString().split('T')[0];
    document.getElementById('schedule-start-date').value = today;
    document.getElementById('schedule-end-date').value = today;
}
