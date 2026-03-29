import { useState } from "react";
import { Button, Modal, Offcanvas, ListGroup } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import api from "../../api/axiosConfig";

export default function ProfileManage({ userID, password, ...props }) {
  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const [showM, setShowM] = useState(false);
  const handleCloseM = () => setShowM(false);
  const handleShowM = () => setShowM(true);

  const [showD, setShowD] = useState(false);
  const handleCloseD = () => setShowD(false);
  const handleShowD = () => setShowD(true);

  const navigate = useNavigate();

  const handleLogOut = (event) => {
    event.preventDefault();
    navigate("/");
  };

  const handleYes = (event) => {
    event.preventDefault();
    api
      .delete(`/api/v1/user/${userID}/deleteAll`)
      .then((response) => {})
      .catch((error) => {
        console.error(error);
        setShowD(false);
      });

    api
      .delete(`/api/v1/user/${userID}`)
      .then((response) => {})
      .catch((error) => {
        console.error(error);
        setShowD(false);
      });

    navigate("/");
  };

  return (
    <>
      <a href="#link" onClick={handleShow}>
        {userID}
      </a>
      <Offcanvas show={show} onHide={handleClose} placement="end">
        <Offcanvas.Header closeButton>
          <Offcanvas.Title>Profile</Offcanvas.Title>
        </Offcanvas.Header>
        <Offcanvas.Body>
          <ListGroup>
            <ListGroup.Item>
              User: {userID} {password}
            </ListGroup.Item>

            {/* Delete Account */}
            <ListGroup.Item action onClick={handleShowD}>
              Delete Account
            </ListGroup.Item>
            <Modal show={showD} onHide={handleCloseD}>
              <Modal.Header closeButton>
                {" "}
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
