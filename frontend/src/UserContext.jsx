import { createContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getUser, registerUser, loginUser, logoutUser } from "./api/userApi";

export const UserContext = createContext();

export const UserContextProvider = ({ children }) => {

  const [user, setUser] = useState(null);
  
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const fetchUser = async () => {
    setError(null);
    try {
      const user = await getUser();
      setUser(user);
      console.log("Fetched user:", user);
    } catch (err) {
      console.error("Error getting user:", err.response ? err.response : err.request ? err.request : err.message);
    }
  };

//    useEffect(() => {
// +    getUser().finally(() => setLoading(false));
// +  }, []);

  const register = async (username, password) => {
    setError(null);
    try {
      await registerUser(username, password);
    } catch (err) {
      setError("Either the username or password was invalid.");
      console.error("Registration error:",  err.response ? err.response : err.request ? err.request : err.message);
      return; 
    }

    try {
      await fetchUser();
      navigate(`/home`);
    } catch (err) {
      setError("Something went wrong. Please try again.");
      console.error("Error fetching user after registration:", err.response ? err.response : err.request ? err.request : err.message);
    }
  };

  const login = async (username, password) => {
    setError(null);
    try {
      await loginUser(username, password );
    } catch (err) {
      setError("Either the username or password was incorrect.");
      console.error("Login error:", err.response ? err.response : err.request ? err.request : err.message);
      return; 
    }

    try {
      await fetchUser();
      navigate(`/home`);
    } catch (err) {
      setError("Something went wrong. Please try again.");
      console.error("Error fetching user after login:", err.response ? err.response : err.request ? err.request : err.message );
    }
  };

  const logout = async() => {
    logoutUser();
    setUser(null);
    navigate('/');
  };

  const username = user?.username; // Extract username from user object for easier access
  return (
    <UserContext value={{ user, username, setUser, fetchUser,login, register, error, setError, logout, loading }}>
      {children}
    </UserContext>
  );
};
