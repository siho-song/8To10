import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import "./styles/styles.css";

import Login from './components/login/Login';
import SignUp from './components/signUp/SignUp';
import AuthProvider from "./context/auth/AuthProvider.jsx";

import PrivateRoute from "./components/PrivateRoute.jsx";

import Home from "./pages/Home.jsx";
import {FullCalendarProvider} from "@/context/fullCalendar/FullCalendarProvider.jsx";

import Community from "@/pages/Community.jsx";
import Post from "@/pages/Post.jsx";
import BoardPost from "@/pages/BoardPost.jsx";
import SignUpComplete from "@/components/signUp/SignUpComplete.jsx";


function App() {
    return (
        <AuthProvider>
            <Router>
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route path="/signup" element={<SignUp />} />
                    <Route path="/signup/complete" element={<SignUpComplete />} />
                    <Route path="/" element={<Login />} />
                    <Route element={<PrivateRoute />}>
                        <Route
                            path="/home"
                            element={
                                <FullCalendarProvider>
                                    <Home />
                                </FullCalendarProvider>
                            }
                        />
                        <Route path="/community/board" element={<Community />}/>
                        <Route path="/community/board/add" element={<Post isEditMode={false}/>} />
                        <Route path="/community/board/edit/:postId" element={<Post isEditMode={true}/>}/>
                        <Route path="/community/board/:id" element={<BoardPost />}/>
                    </Route>
                </Routes>
            </Router>
        </AuthProvider>
    );
}

export default App;
