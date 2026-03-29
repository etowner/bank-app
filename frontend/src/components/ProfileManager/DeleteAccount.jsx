import { useState } from "react";
import { Button, Modal, Alert } from "react-bootstrap";
import api from "../../api/axiosConfig";
import { useNavigate } from "react-router-dom";

export default function DeleteAccount({ userID }) {
  const [showD, setShowD] = useState(false);
  const handleCloseD = () => setShowD(false);
  const handleShowD = () => setShowD(true);
  const navigate = useNavigate();

  const handleYes = (event) => {
    event.preventDefault();
    api
      .delete(`/api/v1/user/${userID}/closeAll`)
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
    <div>
      <Button onClick={handleShowD}> Delete Account</Button>
      <Modal show={showD} onHide={handleCloseD}>
        <Modal.Header closeButton></Modal.Header>
        <Modal.Body className="justify-content-md-center">
          <Alert variant="danger">
            Are you sure you want to delete this account? This cannot be undone!
            <hr />
            <Button variant="secondary" onClick={handleYes}>
              Yes
            </Button>
            <Button variant="outline-danger" onClick={handleCloseD}>
              No
            </Button>
          </Alert>
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
    </div>
  );
}
