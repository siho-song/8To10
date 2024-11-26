const generateEventIdWithParentId = (event) => {
    if (event.parentId) {
        return `${event.parentId}-${event.id}`;
    }
    return event.id;
};

const parseEventIdWithParentId = (event) => {
    if (event.parentId) {
        return event.id.split("-").pop();
    }
    return event.id;
}

export const formatNormalSchedule = (event) => {
    return {
        id: generateEventIdWithParentId(event),
        groupId: event.parentId,
        title: event.title,
        start: event.start,
        end: event.end,
        color: event.color,
        extendedProps: {
            type: event.type,
            parentId: event.parentId,
            commonDescription: event.commonDescription,
            detailDescription: "",
            bufferTime : event.bufferTime,
            completeStatus: event.completeStatus,
            dailyAmount : event.dailyAmount,
        }
    };
}

export const formatVariableSchedule = (event) => {
    return {
        id: event.id,
        title: event.title,
        start: event.start,
        end: event.end,
        color: event.color,
        extendedProps: {
            type: event.type,
            commonDescription: event.commonDescription,
        },
    };
}

export const formatFixedSchedule = (event) => {
    return {
        id: generateEventIdWithParentId(event),
        groupId: event.parentId,
        title: event.title,
        start: event.start,
        end: event.end,
        color: event.color,
        extendedProps: {
            type: event.type,
            commonDescription: event.commonDescription,
            detailDescription: "",
        },
    };
}
