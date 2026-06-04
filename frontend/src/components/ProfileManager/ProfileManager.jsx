import { useState, useContext } from "react";
import { Button, Modal, Offcanvas, ListGroup } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import api from "../../api/axiosConfig";
import { UserContext } from "../../UserContext";
import ChangePassword from "./ChangePassword";
import ChangeUserID from "./ChangeUserID";

export default function ProfileManager() {
  const { userID, logout, setUser, user } = useContext(UserContext);
  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const [showChangePassword, setShowChangePassword] = useState(false);
  const [showChangeUserID, setShowChangeUserID] = useState(false);

  const [showM, setShowM] = useState(false);
  const handleCloseM = () => setShowM(false);
  const handleShowM = () => setShowM(true);

  const [showD, setShowD] = useState(false);
  const handleCloseD = () => setShowD(false);
  const handleShowD = () => setShowD(true);

  const navigate = useNavigate();

  const handleLogOut = (event) => {
    event.preventDefault();
    logout();
  };

  const handleYes = async (event) => {
    event.preventDefault();
    try {
        await api.delete(`/api/v1/account/closeAll`);
        await api.delete(`/api/v1/user`);
        setUser(null);
        navigate("/");
    } catch (error) {
        console.error(error);
        setShowD(false);
    }
  };

  const handleForgotPassword = () => {
    console.log("Forgot password feature - Coming soon");
    // Placeholder for future forgot password functionality
  };

  const handlePasswordChangeClose = () => {
    setShowChangePassword(false);
  };

  const handlePasswordChangeSuccess = () => {
    setShowChangePassword(false);
    handleClose();
    console.log(user);
  };

  const handleUserIDChangeClose = () => {
    setShowChangeUserID(false);
  };

  const handleUserIDChangeSuccess = () => {
    setShowChangeUserID(false);
    handleClose();
    console.log(user);
    logout();
  };

  return (
    <>
      <a href="#profile" onClick={handleShow}>
        {userID}
      </a>
      <Offcanvas show={show} onHide={handleClose} placement="end">
        <Offcanvas.Header closeButton>
          <Offcanvas.Title>Profile</Offcanvas.Title>
        </Offcanvas.Header>
        <Offcanvas.Body>
          <ListGroup>
            <ListGroup.Item>
              <strong>UserID:</strong> {userID}
            </ListGroup.Item>

            {/* Change UserID */}
            <ListGroup.Item action onClick={() => setShowChangeUserID(true)}>
              Change UserID
            </ListGroup.Item>
            <Modal show={showChangeUserID} onHide={handleUserIDChangeClose}>
              <Modal.Header closeButton>
                <Modal.Title>Change UserID</Modal.Title>
              </Modal.Header>
              <Modal.Body>
                <ChangeUserID onClose={handleUserIDChangeClose} onSuccess={handleUserIDChangeSuccess} />
              </Modal.Body>
            </Modal>

            {/* Change Password */}
            <ListGroup.Item action onClick={() => setShowChangePassword(true)}>
              Change Password
            </ListGroup.Item>
            <Modal show={showChangePassword} onHide={handlePasswordChangeClose}>
              <Modal.Header closeButton>
                <Modal.Title>Change Password</Modal.Title>
              </Modal.Header>
              <Modal.Body>
                <ChangePassword onClose={handlePasswordChangeClose} onSuccess={handlePasswordChangeSuccess} />
              </Modal.Body>
            </Modal>

            {/* Forgot Password */}
            <ListGroup.Item action onClick={handleForgotPassword}>
              Forgot Password?
            </ListGroup.Item>

            {/* Delete Account */}
            <ListGroup.Item action onClick={handleShowD}>
              Delete Account
            </ListGroup.Item>
            <Modal show={showD} onHide={handleCloseD}>
              <Modal.Header closeButton>
                <Modal.Title style={{ textAlign: "center" }}>
                  Delete Account
                </Modal.Title>
              </Modal.Header>
              <Modal.Body style={{ textAlign: "center" }}>
                Are you sure you want to delete this account? This cannot be
                undone!
              </Modal.Body>
              <Modal.Footer>
                <Button variant="secondary" onClick={handleYes}>
                  Yes
                </Button>
                <Button variant="primary" onClick={handleCloseD}>
                  No
                </Button>
              </Modal.Footer>
            </Modal>

            {/* Log Out function */}
            <ListGroup.Item action onClick={handleShowM}>
              Log Out
            </ListGroup.Item>
            <Modal show={showM} onHide={handleCloseM}>
              <Modal.Header closeButton></Modal.Header>
              <Modal.Body style={{ textAlign: "center" }}>
                Are you sure you want to log out?
              </Modal.Body>
              <Modal.Footer>
                <Button variant="primary" onClick={handleLogOut}>
                  Log Out
                </Button>
              </Modal.Footer>
            </Modal>
          </ListGroup>
        </Offcanvas.Body>
      </Offcanvas>
    </>
  );
}
