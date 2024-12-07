import PrivateHeader from "@/components/PrivateHeader.jsx";
import PostView from "@/components/community/post/PostView.jsx";
import LeftSideBar from "@/components/community/LeftSideBar.jsx";

function BoardPost() {

    return (
        <div>
            <div className="container">
                <LeftSideBar/>
                <PostView />
            </div>
        </div>
    );
}

export default BoardPost;