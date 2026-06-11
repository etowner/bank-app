import { use, useEffect, useState } from "react";
import { Navigate, Outlet } from "react-router-dom";
import { UserContext } from "./UserContext";

// eslint-disable-next-line no-unused-vars
const ProtectedRoute = ({ children }) => {
  const { user, fetchUser } = use(UserContext);
  // const [loading, setLoading] = useState(true);
  const [isResolving, setIsResolving] = useState(true);

  useEffect(() => {
    fetchUser().finally(() => setIsResolving(false) );
  },[fetchUser]);
  
  if (isResolving) {
    return <div>Loading...</div>;
  }

  if (!user) {
    return <Navigate to="/" replace />;
  }
  
  // return user ? children : <Navigate to="/" />;
  return <Outlet />;
};

export default ProtectedRoute;