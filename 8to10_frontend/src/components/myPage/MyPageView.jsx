import Header from "../Header.jsx";

import "@/styles/myPage/MyPage.css";
import MyPageInit from "@/components/myPage/MyPageInit.jsx";

function MyPageView() {

    return (
        <div>
            <Header />
            <MyPageInit />
        </div>
    );
}

export default MyPageView;
