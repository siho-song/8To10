import PropTypes from 'prop-types';
import NScheduleDetails from "@/components/home/eventDetails/NScheduleDetails.jsx";
import FScheduleDetails from "@/components/home/eventDetails/FScheduleDetails.jsx";
import VScheduleDetails from "@/components/home/eventDetails/VScheduleDetails.jsx";

function EventDetails({ selectedEvent, onClose }) {
    if (!selectedEvent) return null;

    return (
        <>
            {selectedEvent.extendedProps.type === "normal" &&
                <NScheduleDetails
                    selectedEvent={selectedEvent}
                    onClose={onClose} />
            }

            {selectedEvent.extendedProps.type === "variable" &&
                <VScheduleDetails
                    selectedEvent={selectedEvent}
                    onClose={onClose} />
            }

            {selectedEvent.extendedProps.type === "fixed" &&
                <FScheduleDetails
                    selectedEvent={selectedEvent}
                    onClose={onClose} />
            }
        </>
    );
}


EventDetails.propTypes = {
    selectedEvent: PropTypes.shape({
        id: PropTypes.string.isRequired,
        title: PropTypes.string.isRequired,
        start: PropTypes.instanceOf(Date).isRequired,
        end: PropTypes.instanceOf(Date).isRequired,
        extendedProps: PropTypes.shape({
            type: PropTypes.string.isRequired,
            commonDescription: PropTypes.string,
            detailDescription: PropTypes.string,
            bufferTime:PropTypes.string,
            completeStatus: PropTypes.bool,
            isComplete: PropTypes.bool,
            dailyAmount: PropTypes.number,
            achievedAmount: PropTypes.number,
            parentId: PropTypes.number,
            originId: PropTypes.number,
        }),
    }),
    onClose: PropTypes.func.isRequired,
}

export default EventDetails;