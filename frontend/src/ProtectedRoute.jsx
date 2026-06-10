import { use, useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { UserContext } from "./UserContext";

const ProtectedRoute = ({ children }) => {

  const { user, loading, getUser } = use(UserContext);
  const [isResolvingUser, setIsResolvingUser] = useState(true);
  
  useEffect(() => {
    let isMounted = true;
    
    if (user) {
      setIsResolvingUser(false);
      return () => {
        isMounted = false;
      };
    }

    if (typeof getUser !== "function") {
      setIsResolvingUser(false);
      return () => {
        isMounted = false;
      };
    }

    Promise.resolve(getUser()).finally(() => {
      if (isMounted) {
        setIsResolvingUser(false);
      }
    });

    return () => {
      isMounted = false;
    };
  }, [user, getUser]);
  
  if (isResolvingUser) {
    return null;
  }
  
  // if (loading) return <div>Loading...</div>;
  return user ? children : <Navigate to="/" />;
};

export default ProtectedRoute;