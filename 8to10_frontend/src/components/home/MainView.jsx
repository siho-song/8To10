import FullcalendarInit from "./FullcalendarInit.jsx";
import Header from "../Header.jsx";

import "@/styles/home/Fullcalendar.css";

function MainView() {
    return (
        <div>
            <Header />
            <FullcalendarInit />
        </div>
    );
}

export default MainView;
