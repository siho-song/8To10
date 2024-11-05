import Header from "@/components/Header.jsx";
import PostDetail from "@/components/community/post/PostDetail.jsx";
import LeftSideBar from "@/components/community/LeftSideBar.jsx";

function PostDetailView() {

    return (
        <div>
            <Header />
            <div className="container">
                <LeftSideBar/>
                <PostDetail />
            </div>
        </div>
    );
}

export default PostDetailView;