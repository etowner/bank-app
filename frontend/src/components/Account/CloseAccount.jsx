import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Button, Modal } from "react-bootstrap";
import api from "../../api/axiosConfig";

export default function DeleteAccount() {
  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);
  const navigate = useNavigate();
  const { accountID, userID } = useParams();

  const closeAcc = (event) => {
    event.preventDefault();
    api
      .delete(`/api/v1/user/${userID}/${accountID}/close`)
      .then((response) => {
        console.log(response.data);
      })
      .catch((error) => {
        console.error(error);
      });
    navigate(`/Home/${userID}`);
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
        <Modal.Footer>
          <Button variant="secondary" onClick={closeAcc}>
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
