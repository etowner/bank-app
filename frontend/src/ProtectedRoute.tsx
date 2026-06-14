import { useEffect, useState } from "react";
import { Navigate, Outlet } from "react-router-dom";
import { useUserContext } from "./UserContext";

const ProtectedRoute = () => {
  const { user, fetchUser } = useUserContext();
  
  const [isResolving, setIsResolving] = useState(true);

  useEffect(() => {
    void fetchUser().finally(() => setIsResolving(false) );
  },[fetchUser]);

  useEffect(() => {
    if (!isResolving && !user) {
      console.log("User is not authenticated, redirecting to login.");
    } 
  }, [isResolving, user]);

  
  if (isResolving) {
//     console.log("isResolving at", new Intl.DateTimeFormat('en-US', {
//   timeStyle: 'medium'
// }).format(new Date()));
    return <div>Loading...</div>;
  }

  if (!user) {
    return <Navigate to="/" replace />;
  }
  
  return <Outlet />;
};

export default ProtectedRoute;