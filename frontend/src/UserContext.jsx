import React, { createContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getUser, registerUser, loginUser, logoutUser } from "./api/userApi";
import PropTypes from "prop-types";

export const UserContext = createContext();

export const UserContextProvider = ({ children }) => {
  UserContextProvider.propTypes = {
    children: PropTypes.node.isRequired,
  };

  const [user, setUser] = useState(null);
  
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const fetchUser = async () => {
    setError(null);
    try {
      const user = await getUser();
      setUser(user);
    } catch (err) {
      console.error("Error getting user:", err.response ? err.response : err.request ? err.request : err.message);
    }
  };

  const register = async (username, password) => {
    setError(null);
    try {
      await registerUser(username, password);
    } catch (err) {
      setError("Either the username or password was invalid.");
      //console.error("Registration error:", err);
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
    <UserContext.Provider value={{ user, username, setUser, fetchUser,login, register, error, setError, logout }}>
      {children}
    </UserContext.Provider>
  );
};
