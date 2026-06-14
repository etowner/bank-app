import { useState } from "react";
import { useParams } from "react-router-dom";
import { Button, Col, Form, Row, Alert } from "react-bootstrap";
import { deposit } from "../../api/transactionApi";
import type { Account } from "../../lib/types";

interface DepositProps {
  setAccount: (account: Account) => void;
  fetchAccountData: () => Promise<void>;
}

export default function Deposit({ setAccount, fetchAccountData }: DepositProps) {
  const [amount, setAmount] = useState('');
  const { accountNumber } = useParams<{ accountNumber: string }>();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);


  const handleDepositClick = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
    const parsedAmount = parseFloat(amount);

    if (isNaN(parsedAmount) || parsedAmount <= 0) {
      setError("Invalid deposit amount. Please enter a valid amount.");
      return;
    }

    setLoading(true);

    try {
      const updatedAccount = await deposit(accountNumber!, parsedAmount);
      setAmount('');
      setError(null);
      setAccount(updatedAccount); 
    } catch (error) {
      setError("Deposit failed. Please try again later.");
      console.error(error);
    } finally {
      setLoading(false);
    }

    void await fetchAccountData();
  };


  return (
    <div>
      <Form.Group as={Row} className="mb-3">
        <Form.Label column sm={5}>
          Enter an amount:
        </Form.Label>
        <Col sm={3}>
          <Form.Control
            type="number"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            placeholder="Enter amount"
            min="0"
            step="0.01"
          />
        </Col>
        <Col sm={2}>
          <Button onClick={(e) => void handleDepositClick(e)} className="mb-3">
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
