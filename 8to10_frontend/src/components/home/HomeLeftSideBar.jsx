import "@/styles/home/LeftSideBar..css"
import UserStatsCard from "@/components/home/userStatsCard/UserStatsCard.jsx";
import TodoList from "@/components/home/todoList/TodoList.jsx";

function HomeSidebarLeft() {
    return (
        <div className="sidebar" id="left-sidebar">
            <UserStatsCard />
            <TodoList/>
        </div>
    );
}

export default HomeSidebarLeft;
