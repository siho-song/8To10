import {Link} from "react-router-dom";
import logoIcon from "@/assets/images/logo.png";

function PublicHeader() {
    return (
        <div className="main-header">
            <div className="logo">
                <Link to="/" className="image-link"
                      style={{display: 'flex', flexDirection: 'row', alignItems: 'center'}}>
                    <img src={logoIcon} alt="로고"/>
                    <p style={{marginLeft: '10px'}}>EightToTen</p>
                </Link>
            </div>
        </div>
    );
}

export default PublicHeader;