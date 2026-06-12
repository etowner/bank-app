import { useEffect, useState } from "react";
import { Navigate, Outlet } from "react-router-dom";
import { useUserContext } from "./UserContext";

const ProtectedRoute = ({ }) => {
  const { user, fetchUser } = useUserContext();
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