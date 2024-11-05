import { useState, useEffect } from 'react';
import BoardList from './BoardList.jsx';
import BoardHeader from "./BoardHeader.jsx";
import PaginationComponent from "./Pagination.jsx";

import '@/styles/community/Board.css';
import LeftSideBar from "@/components/community/LeftSideBar.jsx";

const Board = () => {

    const [posts, setPosts] = useState([]);

    const [boardState, setBoardState] = useState({
        sortCondition: 'DATE',
        searchKeyword: '',
        searchCondition: 'TITLE',
        postsPerPage: 10,
        pageNum: 0,
        totalPages: 0
    });

    const setBoardField = (field, value) => {
        if (field === 'sortCondition' || field === 'postsPerPage') {
            setBoardField('pageNum', 0);
        }

        setBoardState(prevState => ({
            ...prevState,
            [field]: value
        }));
    };

    useEffect(() => {
        const fetchBoardData = async() => {
            await loadBoardData();
        }
        fetchBoardData();
    }, [boardState.pageNum, boardState.postsPerPage, boardState.sortCondition]);

    const handleSearch = () => {
        loadBoardData();
    };

    const loadBoardData = async () => {

        const params = new URLSearchParams({
            keyword: boardState.searchKeyword,
            pageNum: boardState.pageNum,
            pageSize: boardState.postsPerPage,
            searchCond: boardState.searchCondition,
            sortCond: boardState.sortCondition,
        });

        try {
            const response = await fetch(`/api/community/board?${params.toString()}`, {
                method: 'GET',
            });

            if (!response.ok) {
                throw new Error('서버와의 통신에 실패했습니다.');
            }

            const data = await response.json();

            setPosts(data.content);
            setBoardField('totalPages', data.totalPages);

        } catch (error) {
            console.error('Error:', error);
        }
    };

    return (
        <div className="container" id="board-container">
            <LeftSideBar />

            <div className="board-main-content">
                <BoardHeader boardState={boardState} setBoardField={setBoardField} handleSearch={handleSearch}/>
                <BoardList posts={posts}/>
                <PaginationComponent
                    boardState={boardState}
                    setBoardField={setBoardField}
                />
            </div>
        </div>
    );
};

export default Board;
