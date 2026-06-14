import { createContext, useState, useCallback, use } from "react";
import { useNavigate } from "react-router-dom";
import { getUser, registerUser, loginUser, logoutUser } from "./api/userApi";
import { User } from "./lib/types";

interface UserContextType {
  username: string | null;
  user: User | null;
  setUser: React.Dispatch<React.SetStateAction<User | null>>;
  loading: boolean;
  setError: React.Dispatch<React.SetStateAction<string | null>>;
  error: string | null;
  fetchUser: () => Promise<void>;
  login: (username: string, password: string) => Promise<void>;
  register: (username: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
}

export const UserContext = createContext<UserContextType | null>(null);

export const useUserContext = () => {
    const context = use(UserContext);
    if (!context) throw new Error('useUserContext must be used within UserContextProvider');
    return context;
};

export const UserContextProvider = ({ children }: { children: React.ReactNode }) => {

  const [user, setUser] = useState<User | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const navigate = useNavigate();

  const fetchUser = useCallback(async () => {
    setError(null);
    setLoading(true);
    try {
      const user = await getUser();
      setUser(user);
      // console.log("Fetched user:", user);
    } catch (err: any) {
      setError(err.response?.data || "Failed to fetch user");
      console.error("Error getting user:", err.response ? err.response : err.request ? err.request : err.message);
    } finally {
      setLoading(false);
    }
  }, []);


  const register = async (username: string, password: string) => {
    setError(null);
    try {
      await registerUser(username, password);
    } catch (err: string | any) {
      setError(err.response?.data || "Either the username or password was invalid.");
      console.error("Registration error:",  err.response ? err.response : err.request ? err.request : err.message);
      return; 
    }
    navigate(`/home`);
  };

  const login = async (username: string, password: string) => {
    setError(null);
    try {
      await loginUser(username, password );
    } catch (err: string | any) {
      setError(err.response?.data || "Either the username or password was incorrect.");
      console.error("Login error:", err.response ? err.response : err.request ? err.request : err.message);
      return; 
    }
    navigate(`/home`);
  };

  const logout = async() => {
    logoutUser();
    setUser(null);
    navigate('/');
  };

  const username = user?.username || null; // Extract username from user object for easier access
  
  return (
    <UserContext value={{ user, username, setUser, fetchUser,login, register, error, setError, logout, loading }}>
      {children}
    </UserContext>
  );
};
