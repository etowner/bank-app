import "./App.css";
import { Routes, Route } from "react-router-dom";
import FrontPage from "./components/FrontPage/FrontPage";
import Home from "./components/Home/Home";
import Account from "./components/Account/Account";
import { UserContextProvider } from "./UserContext";

function App() {
  return (
    <div className="App">
      <UserContextProvider>
        <Routes>
          <Route path="/" element={<FrontPage />} />
          <Route path="/home/:userID" element={<Home />} />
          <Route path="/:userID/:accountID" element={<Account />} />
        </Routes>
      </UserContextProvider>
    </div>
  );
}

export default App;
