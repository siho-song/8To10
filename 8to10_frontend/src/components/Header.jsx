import "@/styles/home/Header.css";

import {Link} from "react-router-dom";

import HouseIcon from '@mui/icons-material/House';
import PeopleIcon from '@mui/icons-material/People';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';

import logoIcon from '@/assets/images/logo.png';

function Header() {
    return (
        <div className="main-header">
            <div className="logo">
                <Link to="/home" className="image-link"
                      style={{display: 'flex', flexDirection: 'row', alignItems: 'center'}}>
                    <img src={logoIcon} alt="로고"/>
                    <p style={{marginLeft: '10px'}}>EightToTen</p>
                </Link>
            </div>
            <div className="navigation">
                <nav>
                    <ul className="header-image-box">
                        <div className="image-container" data-alt="마이페이지">
                            <Link to="/mypage" className="image-link">
                                <AccountCircleIcon/>
                            </Link>
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
        </div>
    );
}

export default Header;
