import FullcalendarInit from "./FullcalendarInit.jsx";
import Header from "../Header.jsx";

import "@/styles/home/Fullcalendar.css";
import {useAuth} from "@/components/context/UseAuth.jsx";

function MainView() {
    const { isAuthenticated, loading} = useAuth();

    console.log("isAuthenticated in Main view : ", isAuthenticated);
    console.log("loading in Main view : ", loading);

    return (
        <div>
            <Header />
            <FullcalendarInit />
        </div>
    );
}

export default MainView;
