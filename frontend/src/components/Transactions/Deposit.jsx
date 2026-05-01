import React, { useState } from "react";
import { useParams } from "react-router-dom";
import api from "../../api/axiosConfig";
import { Button, Col, Form, Row, Alert } from "react-bootstrap";

export default function Deposit({ updateAccount }) {
  const [amount, setAmount] = useState(0);
  const { accountID } = useParams();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleDepositClick = async (event) => {
    event.preventDefault();
    if (isNaN(amount) || amount <= 0) {
      setError("Invalid deposit amount. Please enter a valid amount.");
      return;
    }
    setLoading(true);
    try {
      const response = await api.put(
        `/api/v1/account/${accountID}/deposit`,
        parseFloat(amount),
        { headers: { "Content-Type": "application/json" } },
      );
      setAmount(0);
      setError(null);
      updateAccount(response.data);
    } catch (error) {
      setError("Deposit failed. Please try again later.");
      console.error(error);
    } finally {
      setLoading(false); 
    }
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
