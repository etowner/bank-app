import "./styles/App.css";
import { Routes, Route } from "react-router-dom";
import FrontPage from "./components/FrontPage/FrontPage";
import Home from "./components/Home/Home";
import Account from "./components/Account/Account";
import { UserContextProvider } from "./UserContext";
import ProtectedRoute from "./ProtectedRoute";

function App() {
  return (
    <div className="App">
      <UserContextProvider>
        <Routes>
          <Route path="/" element={<FrontPage />} />
          <Route path="/home" element={
            <ProtectedRoute><Home /></ProtectedRoute>
          } />
          <Route path="/account/:accountID" element={
            <ProtectedRoute><Account /></ProtectedRoute>
          } />
        </Routes>
      </UserContextProvider>
    </div>
  );
}

export default App;
