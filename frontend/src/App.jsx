import "./styles/App.css";
import { Routes, Route } from "react-router-dom";
import FrontPage from "./components/FrontPage/FrontPage";
import Home from "./components/Home/Home";
import Account from "./components/Account/Account";
import { UserContextProvider } from "./UserContext";
import ProtectedRoute from "./ProtectedRoute";
import MainPage from "./Dashboard/MainPage";

function App() {
  return (
    <div className="App">
      <UserContextProvider>
        <Routes>
          <Route path="/" element={<FrontPage />} />
          <Route path="/home" element={
            <Home />
          } />
          <Route path="/account/:accountID" element={
            <Account />
          } />
          <Route path="/dash" element={
            <MainPage />
            } />
        </Routes>
      </UserContextProvider>
    </div>
  );
}

export default App;
