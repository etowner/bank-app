import { use, useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { UserContext } from "./UserContext";

const ProtectedRoute = ({ children }) => {

  const { user, loading, fetchUser } = use(UserContext);
  const [isResolvingUser, setIsResolvingUser] = useState(true);
  
  // useEffect(() => {
  //   let isMounted = true;
    
  //   if (user) {
  //     setIsResolvingUser(false);
  //     return () => {
  //       isMounted = false;
  //     };
  //   }

  //   if (typeof fetchUser !== "function") {
  //     setIsResolvingUser(false);
  //     return () => {
  //       isMounted = false;
  //     };
  //   }

  //   Promise.resolve(fetchUser()).finally(() => {
  //     if (isMounted) {
  //       setIsResolvingUser(false);
  //     }
  //   });

  //   return () => {
  //     isMounted = false;
  //   };
  // }, [user, fetchUser]);
  
  // if (isResolvingUser) {
  //   return null;
  // }
  
  // if (loading) return <div>Loading...</div>;
  return user ? children : <Navigate to="/" />;
};

export default ProtectedRoute;