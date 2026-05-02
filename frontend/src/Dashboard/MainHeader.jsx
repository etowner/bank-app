import React from "react";
import { Nav, Navbar, Container } from "react-bootstrap";
import ProfileManager from "../components/ProfileManager/ProfileManager";
import { NavLink, Link } from "react-router-dom";
import MainPage from "./MainPage";

const MainHeader = ({ userID }) => {
  return (
    <div>
      <Navbar style={{ padding: "1rem", background: "#282c34" }} className="mb-5">
        <Container>
          <Navbar.Brand as={Link} to="/home" style={{ color: "#c9a84c", letterSpacing: "1px" }}>
            Bank Application
          </Navbar.Brand>
          <Navbar.Collapse id="navMenu">
            <Nav  variant="underline" defaultActiveKey="/home" className="me-auto">
              <Nav.Link as={Link} to="/dash" style={{ color: "rgba(255,255,255,0.5)"}}>Dashboard</Nav.Link>
              {/* <Nav.Link as={Link} to="/accounts" style={{ color: "rgba(255,255,255,0.5)", fontSize: "16px" }}>Accounts</Nav.Link> */}
            </Nav>
    
          </Navbar.Collapse>
          <Navbar.Collapse className="justify-content-end">
            <Navbar.Text style={{ color: "rgba(255,255,255,0.5)", fontSize: "16px" }}>
              <ProfileManager />
            </Navbar.Text>
          </Navbar.Collapse>
        </Container>
      </Navbar>
    </div>
  );
};
export default MainHeader;
