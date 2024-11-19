import { useState, useEffect } from 'react';
import BoardList from './BoardList.jsx';
import BoardHeader from "./BoardHeader.jsx";
import PaginationComponent from "./Pagination.jsx";

import '@/styles/community/Board.css';
import LeftSideBar from "@/components/community/LeftSideBar.jsx";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";


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
            const url = `/community/board?${params.toString()}`;
            const response = await authenticatedApi.get(url, {
                apiEndPoint: API_ENDPOINT_NAMES.GET_BOARD_PAGING,
            });
            const data = response.data;

            setPosts(data.content);
            setBoardField('totalPages', data.totalPages);
        } catch (error) {
            console.error("Error : \n", error.toString());
            console.error(error);
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
