import React, { useContext, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../../api/axiosConfig";
import { Button, Form, Row, Col, Alert } from "react-bootstrap";
import { UserContext } from "../../UserContext";

export default function Withdraw({ balance, updateAccount }) {
  const [amount, setAmount] = useState(0);
  const { accountID } = useParams();
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleWithdrawClick = async (event) => {
    event.preventDefault();
    if (isNaN(amount) || amount <= 0) {
        setError("Invalid withdrawal amount. Please enter a valid amount.");
        return;
    }
    if (balance - amount < 0) {
        setError("Insufficient funds.");
        return;
    }
    setLoading(true);
    try {
        const response = await api.put(
            `/api/v1/account/${accountID}/withdraw`,
            parseFloat(amount),
            { headers: { "Content-Type": "application/json" } }
        );
        setError(null);
        setAmount(0);
        updateAccount(response.data);
    } catch (error) {
        setError("Withdrawal failed. Please try again.");
        console.error(error);
    } finally {
        setLoading(false);
    }
};

  return (
    <div>
      <Form.Group as={Row} className="mb-3 ">
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
          <Button onClick={handleWithdrawClick} className="mb-3">
            {loading ? <>Loading..</> : <>Submit</>}
          </Button>
        </Col>
      </Form.Group>
      <Row className="justify-content-md-center">
        <Col sm={6}>{error && <Alert variant="danger">{error}</Alert>}</Col>
      </Row>
    </div>
  );
}
