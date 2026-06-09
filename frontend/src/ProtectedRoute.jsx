import React from "react";
import { useContext } from "react";
import { Navigate } from "react-router-dom";
import { UserContext } from "./UserContext";
import PropTypes from "prop-types";

const ProtectedRoute = ({ children }) => {
  ProtectedRoute.propTypes = {
    children: PropTypes.node.isRequired,
  };
  const { user } = useContext(UserContext);
  return user ? children : <Navigate to="/" />;
};

export default ProtectedRoute;