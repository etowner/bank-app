import React, { useState } from "react";
import { useParams } from "react-router-dom";
import api from "../../api/axiosConfig";
import { Button, Col, Form, Row, Alert } from "react-bootstrap";

export default function Deposit(props) {
  const [amount, setAmount] = useState(0);
  const { accountID } = useParams();
  const { userID } = useParams();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleDepositClick = (event) => {
    event.preventDefault();

    if (isNaN(amount) || amount <= 0) {
      setError("Invalid deposit amount. Please enter a valid amount.");
      return;
    }

    const depositAmount = parseFloat(amount);
    setLoading(true);
    api
      .put(`/api/v1/user/${userID}/${accountID}/deposit`, depositAmount, {
        headers: { "Content-Type": "application/json" },
      })
      .then((response) => {
        console.log("Deposit successful");
        setAmount(0);
        setError(null);
        props.updateAccount(response.data);
        setLoading(false);
      })
      .catch((error) => {
        console.error(error);
        setError("Deposit failed. Please try again later.");
        setLoading(false);
      });
  };

  return (
    <div>
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm={5}>
          Enter an amount:
        </Form.Label>
        <Col sm={3}>
          <Form.Control
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
          />
        </Col>
        <Col sm={2}>
          <Button onClick={handleDepositClick} className="mb-3">
            {loading ? <>Loading..</> : <>Submit</>}
          </Button>
        </Col>
      </Form.Group>
      <Row className="justify-content-md-center">
        <Col sm={4}>{error && <Alert variant="danger">{error}</Alert>}</Col>
      </Row>
    </div>
  );
}
