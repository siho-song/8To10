import Header from "@/components/Header.jsx";
import CreateOrEditPost from "@/components/community/post/CreateOrEditPost.jsx";
import PropTypes from "prop-types";
import LeftSideBar from "@/components/community/LeftSideBar.jsx";

function CreatePostView({ isEditMode=false }) {

    return (
        <div>
            <Header />
            <CreateOrEditPost isEditMode={isEditMode} />
        </div>
    );
}

CreatePostView.propTypes = {
    isEditMode: PropTypes.bool,
}

export default CreatePostView;