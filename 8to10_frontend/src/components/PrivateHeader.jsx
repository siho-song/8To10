import "@/styles/home/Header.css";

import {Link} from "react-router-dom";

import HouseIcon from '@mui/icons-material/House';
import PeopleIcon from '@mui/icons-material/People';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';

import logoIcon from '@/assets/images/logo.png';
import Notification from "@/components/notification/Notification.jsx";
import Modal from "@/components/modal/Modal.jsx";
import MyPage from "@/components/myPage/MyPage.jsx";
import {useState} from "react";

function PrivateHeader() {

    const [isModalOpen, setIsModalOpen] = useState(false);

    const openModal = () => setIsModalOpen(true);
    const closeModal = () => setIsModalOpen(false);

    return (
        <div className="main-header">
            <div className="logo">
                <Link to="/home" className="image-link"
                      style={{display: 'flex', flexDirection: 'row', alignItems: 'center'}}>
                    <img src={logoIcon} alt="로고"/>
                    <p className="logo" style={{marginLeft: '10px'}}>8TO10</p>
                </Link>
            </div>
            <div className="navigation">
                <nav>
                    <ul className="header-image-box">
                        <Notification />
                        <div className="image-container" data-alt="마이페이지">
                            <button className="image-link" onClick={openModal}>
                                <AccountCircleIcon/>
                            </button>
                        </div>
                        <div className="image-container" data-alt="커뮤니티">
                            <Link to="/community/board" className="image-link">
                                <PeopleIcon/>
                            </Link>
                        </div>
                        <div className="image-container" data-alt="홈">
                            <Link to="/home" className="image-link">
                                <HouseIcon/>
                            </Link>
                        </div>
                    </ul>
                </nav>
            </div>
            <Modal isOpen={isModalOpen} onClose={closeModal}>
                <MyPage
                    closeModal={closeModal}
                />
            </Modal>
        </div>
    );
}

export default PrivateHeader;
