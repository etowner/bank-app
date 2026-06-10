
import { Navbar } from "react-bootstrap";
import { Nav } from "react-bootstrap";
import Container from "react-bootstrap/Container";
import ProfileManager from "./ProfileManager/ProfileManager";

const Header = () => {
  return (
    <div id="#home" style={{ background: "#ffffff" }}>
      <Navbar bg="dark" variant="dark" className="mb-5">
        <Container>
          <Navbar.Brand> Bank App</Navbar.Brand>
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto"></Nav>
          </Navbar.Collapse>
          <Navbar.Collapse className="justify-content-end">
            <Navbar.Text>
              Signed in as:
              <ProfileManager/>
            </Navbar.Text>
          </Navbar.Collapse>
        </Container>
      </Navbar>
    </div>
  );
};
export default Header;
