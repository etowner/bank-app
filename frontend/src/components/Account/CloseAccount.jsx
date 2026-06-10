import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Button, Modal, Alert } from "react-bootstrap";
import "../../api/axiosConfig";
import deleteAccount from "../../api/accountApi";

export default function DeleteAccount() {
  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);
  const navigate = useNavigate();
  const { accountNumber } = useParams();
  const [error, setError] = useState(null);

  const closeAccount = async (event) => {
    event.preventDefault();
    try {
      deleteAccount(accountNumber);
      navigate(`/home`);
    } catch (error) {
      setError("Failed to close account. Please try again.");
      console.error(error);
    }
  };

  return (
    <div>
      <Button variant="dark" onClick={handleShow}>
        Close Account
      </Button>
      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title style={{ textAlign: "center" }}>
            Close Account
          </Modal.Title>
        </Modal.Header>
        <Modal.Body style={{ textAlign: "center" }}>
          Are you sure you want to delete this account? This cannot be undone!
        </Modal.Body>
        {error && <Alert variant="danger">{error}</Alert>}
        <Modal.Footer>
          <Button variant="secondary" onClick={closeAccount}>
            Yes
          </Button>
          <Button variant="primary" onClick={handleClose}>
            No
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}
