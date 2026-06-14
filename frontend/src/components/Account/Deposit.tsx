import { useState } from "react";
import { useParams } from "react-router-dom";
import { Button, Col, Form, Row, Alert } from "react-bootstrap";
import { deposit } from "../../api/transactionApi";
import { Account } from "../../lib/types";

interface DepositProps {
  setAccount: (account: Account) => void;
  fetchAccountData: () => Promise<void>;
}

export default function Deposit({ setAccount, fetchAccountData }: DepositProps) {
  const [amount, setAmount] = useState(0);
  const { accountNumber } = useParams<{ accountNumber: string }>();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

   if (!accountNumber) { 
    return  <div>Loading...</div>;
  }

  const handleDepositClick = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();

    if (isNaN(amount) || amount <= 0) {
      setError("Invalid deposit amount. Please enter a valid amount.");
      return;
    }

    setLoading(true);

    try {
      const updatedAccount = await deposit(accountNumber, amount);
      setAmount(0);
      setError(null);
      setAccount(updatedAccount); 
    } catch (error) {
      setError("Deposit failed. Please try again later.");
      console.error(error);
    } finally {
      setLoading(false);
    }

    await fetchAccountData();
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
            onChange={(e) => setAmount(parseFloat(e.target.value))}
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
