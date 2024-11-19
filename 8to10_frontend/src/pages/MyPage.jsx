import PrivateHeader from "../components/PrivateHeader.jsx";

import "@/styles/myPage/MyPage.css";
import MyPageInit from "@/components/myPage/MyPageInit.jsx";

function MyPage() {

    return (
        <div>
            <PrivateHeader />
            <MyPageInit />
        </div>
    );
}

export default MyPage;
