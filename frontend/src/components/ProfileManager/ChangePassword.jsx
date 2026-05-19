import React from "react";
import { useState, useContext } from "react";
import { Offcanvas, ListGroup, Form } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import api from "../../api/axiosConfig";
import { UserContext } from "../../UserContext";

export default function ChangePassword() {
  const { userID, setUser } = useContext(UserContext);
  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);


  const handleClick = async (event, password) => {
    event.preventDefault();
    try {
      await api.put(`/api/v1/user/update`, { password });
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <>

        <Form onSubmit={(event) => handleClick(event, event.target.password.value)}>
            <Form.Group controlId="password">
            <Form.Label>New Password</Form.Label>
            <Form.Control autoComplete="current-password" type="password" placeholder="Enter new password" />
            </Form.Group>
            <button type="submit">Change Password</button>
        </Form>

    </>
  );
}