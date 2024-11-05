import { createContext } from 'react';

export const AuthContext = createContext();

//
// import { createContext, useState, useEffect } from 'react';
// import PropTypes from 'prop-types'; // PropTypes를 import
//
// export const AuthContext = createContext();
//
// export const AuthProvider = ({ children }) => {
//
//     // 모의 인증 플래그 (개발 단계에서만 true로 설정, 실제 배포 시 false로 설정)
//     const ENABLE_MOCK_AUTH = true;
//
//     const [isAuthenticated, setIsAuthenticated] = useState(false);
//     const [loading, setLoading] = useState(true);
//     const [email, setEmail] = useState("");
//
//     useEffect(() => {
//         if (ENABLE_MOCK_AUTH) {
//             // 모의 인증 로직: 개발 단계에서는 항상 인증된 상태로 처리
//             setIsAuthenticated(true);
//             setLoading(false);
//             return;
//         }
//
//         fetch('/api/auth/validate', {
//             method: 'GET',
//             credentials: 'include',
//         })
//             .then((response) => {
//                 setIsAuthenticated(response.ok);
//             })
//             .catch(() => setIsAuthenticated(false))
//             .finally(() => setLoading(false));
//     }, []);
//
//     return (
//         <AuthContext.Provider value={{ isAuthenticated, setIsAuthenticated, email, setEmail, loading }}>
//             {children}
//         </AuthContext.Provider>
//     );
// };
//
// // PropTypes 정의 추가
// AuthProvider.propTypes = {
//     children: PropTypes.node.isRequired, // children의 타입을 node로 설정하고 필수로 지정
// };
//
// export default AuthProvider;
