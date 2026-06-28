import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Button, Modal, Alert } from "react-bootstrap";
import { useUserContext } from "../../UserContext";
import { deleteAccount } from "../../api/accountApi";
import {getAxiosError} from "../../api/axiosConfig";

export default function DeleteAccount() {
  const { fetchUser } = useUserContext();
  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);
  const navigate = useNavigate();
  const { accountNumber } = useParams<{ accountNumber: string }>();
  const [error, setError] = useState<string | null>(null);


  const closeAccount =  (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
    try {
      void deleteAccount(accountNumber!);
      void navigate(`/home`);
      void fetchUser(); 
    } catch (err: unknown) {
      setError(getAxiosError(err));
      console.error("Error fetching account data:", err);
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
          <Button variant="secondary" onClick={(e) => closeAccount(e)}>
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
