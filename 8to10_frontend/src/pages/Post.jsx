import PrivateHeader from "@/components/PrivateHeader.jsx";
import CreateOrEditPost from "@/components/community/post/CreateOrEditPost.jsx";
import PropTypes from "prop-types";
import LeftSideBar from "@/components/community/LeftSideBar.jsx";

function Post({ isEditMode=false }) {

    return (
        <div>
            <CreateOrEditPost isEditMode={isEditMode} />
        </div>
    );
}

Post.propTypes = {
    isEditMode: PropTypes.bool,
}

export default Post;