import {useState} from 'react';
import { AuthContext } from './AuthContext.jsx';

function AuthProvider({ children }) {

    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState('');
    const [email, setEmail] = useState('');

    return (
        <AuthContext.Provider value={{ isAuthenticated, setIsAuthenticated, loading, setLoading, email, setEmail, errorMessage }}>
            {children}
        </AuthContext.Provider>
    );
}


export default AuthProvider;
