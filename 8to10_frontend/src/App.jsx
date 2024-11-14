import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import "./styles/styles.css";

import Login from './components/login/Login';
import SignUp from './components/signUp/SignUp';
import AuthProvider from "./context/AuthProvider.jsx";

import PrivateRoute from "./components/PrivateRoute.jsx";

import MainView from "./components/home/MainView.jsx";
import {FullCalendarContext} from "@/context/FullCalendarContext.jsx";

import MyPageView from "@/components/myPage/MyPageView.jsx";

import BoardView from "@/components/community/board/BoardView.jsx";
import CreatePostView from "@/components/community/post/CreatePostView.jsx";
import PostDetailView from "@/components/community/post/PostDetailView.jsx";


function App() {
    return (
        <AuthProvider>
            <Router>
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route path="/signup" element={<SignUp />} />
                    <Route path="/" element={<Login />} />
                    <Route element={<PrivateRoute />}>
                        <Route
                            path="/home"
                            element={
                                <FullCalendarContext>
                                    <MainView />
                                </FullCalendarContext>
                            }
                        />

                        <Route path="/mypage" element={<MyPageView />}/>

                        <Route path="/community/board" element={<BoardView />}/>
                        <Route path="/community/board/add" element={<CreatePostView isEditMode={false}/>} />
                        <Route path="/community/board/edit/:postId" element={<CreatePostView isEditMode={true}/>}/>
                        <Route path="/community/board/:id" element={<PostDetailView />}/>
                    </Route>
                </Routes>
            </Router>
        </AuthProvider>
    );
}

export default App;
