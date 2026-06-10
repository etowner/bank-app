import { use } from "react";
import { Navigate } from "react-router-dom";
import { UserContext } from "./UserContext";

const ProtectedRoute = ({ children }) => {
  const { user } = use(UserContext);
  
  return user ? children : <Navigate to="/" />;
};

export default ProtectedRoute;