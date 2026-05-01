import React, { createContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "./api/axiosConfig";

export const UserContext = createContext();

export const UserContextProvider = ({ children }) => {
  const [user, setUser] = useState({});
  
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const getUser = async () => {
    try {
      const response = (await api.get(`/api/v1/user`));
      setUser(response.data);
    } catch (error) {
      console.error(error);
      console.error("Error getUser details:", error.response ?  error.response.data : error.message);
    }
  };

  const register = async (userID, password) => {
    try {
      await api.post(`/api/v1/user/register`, { userID, password });
    } catch (err) {
      setError("Either the username or password was invalid.");
      console.error("Registration error:", err);
      console.error("Error register details:", err.response ? err.response.data : err.message);
      return; 
    }

    try {
      await getUser();
      navigate(`/home`);
    } catch (err) {
      setError("Something went wrong. Please try again.");
      console.error("Error fetching user after registration:", err);
    }
  };

  const login = async (userID, password) => {
    try {
      await api.post(`/api/v1/user/login`, { userID, password });
    } catch (err) {
      setError("Either the username or password was incorrect.");
      console.error("Login error:", err);
      console.error("Error login details:", err.response ? err.response.data : err.message);
      return; 
    }

    try {
      await getUser();
      navigate(`/home`);
    } catch (err) {
      setError("Something went wrong. Please try again.");
      console.error("Error fetching user after login:", err.message);
    }
  };

  const userID = user.userID; // Extract userID from user object for easier access
  return (
    <UserContext.Provider value={{ user, userID, setUser, getUser,login, register, error }}>
      {children}
    </UserContext.Provider>
  );
};
