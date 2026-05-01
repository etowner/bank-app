import React, { createContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "./api/axiosConfig";

export const UserContext = createContext();

export const UserContextProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const getUser = async () => {
    setError(null);
    const response = (await api.get(`/api/v1/user`));
    setUser(response.data);
  };

  const register = async (userID, password) => {
    setError(null);
    try {
      await api.post(`/api/v1/user/register`, { userID, password });
    } catch (err) {
      setError("Either the username or password was invalid.");
      //console.error("Registration error:", err);
      console.error("Registration error:",  err.response ? err.response : err.request ? err.request : err.message);
      return; 
    }

    try {
      await getUser();
      navigate(`/home`);
    } catch (err) {
      setError("Something went wrong. Please try again.");
      console.error("Error fetching user after registration:", err.response ? err.response : err.request ? err.request : err.message);
    }
  };

  const login = async (userID, password) => {
    setError(null);
    try {
      await api.post(`/api/v1/user/login`, { userID, password });
    } catch (err) {
      setError("Either the username or password was incorrect.");
      console.error("Login error:", err.response ? err.response : err.request ? err.request : err.message);
      return; 
    }

    try {
      await getUser();
      navigate(`/home`);
    } catch (err) {
      setError("Something went wrong. Please try again.");
      console.error("Error fetching user after login:", err.response ? err.response : err.request ? err.request : err.message );
    }
  };

  const logout = async() => {
    await api.post('/logout');
    setUser(null);
    navigate('/');
  };

  const userID = user?.userID; // Extract userID from user object for easier access
  return (
    <UserContext.Provider value={{ user, userID, setUser, getUser,login, register, error, setError, logout }}>
      {children}
    </UserContext.Provider>
  );
};
